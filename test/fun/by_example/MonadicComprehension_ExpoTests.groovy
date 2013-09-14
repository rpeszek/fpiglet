/*
	* Copyright 2013 Robert Peszek.
	*
	* Licensed under the Apache License, Version 2.0 (the "License");
	* you may not use this file except in compliance with the License.
	* You may obtain a copy of the License at
	*     http://www.apache.org/licenses/LICENSE-2.0
	* Unless required by applicable law or agreed to in writing, software
	* distributed under the License is distributed on an "AS IS" BASIS,
	* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	* See the License for the specific language governing permissions and
	* limitations under the License.
	*/
package fun.by_example

import static fpig.common.functions.FpigBase.*
import static fpig.common.functions.FromOperators.*
import static fpig.groovylist.asfunlist.functions.InAndOutOfFunLists.*
import test.utils.GruesomePlus;
import fpig.common.functions.MaybeMonad;
import fpig.common.types.ClosureHolder;
import fpig.common.types.Maybe;
import fpig.concepts.MonadDescription;
import fpig.concepts.MonadWithEmptyDescription;
import fpig.funlist.functions.FunListMonad;
import groovy.lang.Closure;
import gruesome.Gruesome;


/**
 * Shows examples of functional syntax converted to Groovy/Java Lists.
 *
 * Functor working under the hood is not shown.
 * 
 * @author Robert Peszek
 */
class MonadicComprehension_ExpoTests extends GroovyTestCase{
   
	void testAndShow_MaybeComprehension_comprehendSyntax() {
		Closure maybeDivide = f { Maybe num, Maybe den ->
			comprehend {
				x << from { num }
				y << from { den }
				restrict { y != 0}
				output{ just(x/y) }
			}
		}
		
		
		def sixDividedBy = maybeDivide(just(6))
		assert just(2) == sixDividedBy(just(3))
		assert nothing() == sixDividedBy(just(0))
	}

	void  testAndShow_MaybeComprehension_selectSyntax() {
		Closure maybeDivide = f { Maybe num, Maybe den ->
			select{ just(x/y) }.from {
				x << { num }
				y <<  { den }
				where { y != 0}
			}
		}
				
		def sixDividedBy = maybeDivide(just(6))
		assert just(2) == sixDividedBy(just(3))
		assert nothing() == sixDividedBy(just(0))
	}


	void testAndShow_knightExample(){
		//positions are 2D arrays.
		def knightMove = {pos ->
			def (x,y) = pos
			selectP{ newPos }.from{ 
				newPos << { f([[x+2,y+1], [x+2,y-1], [x-2,y+1], [x-2, y-1], [x+1,y+2], [x+1, y-2], [x-1,y+2], [x-1, y-2]]) }
				where {def (_x, _y) = newPos; _x in 0..7 && _y in 0..7}  
			}
		}
		
		def initPosition = [0,0]
		
		def after1 = funlistOut << knightMove(initPosition)
		assert after1 == [[2, 1], [1, 2]]
		
		def in3Moves = selectP { third }.from{
			first << { knightMove(initPosition) }
			second << { knightMove(first) }
			third  << { knightMove(second) }
		}
		
		def groovyList = funlistOut << in3Moves
		println 'knightAfter3Moves: ' +  groovyList
		
		assert groovyList.contains([5,2])
		assert groovyList.contains([6, 1])
		assert !groovyList.contains([5,-2])
		assert !groovyList.find { it[0] < 0 || it[1] < 0}
	
	}

	void testAndShow_ComposePolynomialFunction() {
	    //fH returns ClosureHolder with the closure
	    ClosureHolder res = selectP { 2*x3 + 4*x2 + x + 2 }.from {
				x3 << { fH(POWER(_,3)) }
				x2 << { fH(POWER(_,2)) }
				x  << { fH(POWER(_,1)) }
		}
		assert res.cVal(3) == 2*(3**3) + 4*(3**2) + 3 + 2
		
		//property test
		def poly = res.cVal
		def shouldEqual = {y-> poly(y) == 2*(y**3) + 4*(y**2) + y + 2}
		GruesomePlus.assertForAll(shouldEqual, [Gruesome.genInt])
	}
	
	void testAndShow_ComposePolynomialFunction2() {
		//Comprehensions are smart enough to auto-box (and un-box) Closures into ClosureHolders
		Closure poly = selectP { 2*x3 + 4*x2 + x + 2 }.from {
				x3 << { POWER(_,3) }
				x2 << { POWER(_,2) }
				x  << { POWER(_,1) }
		}
		//def poly = res.cVal
		assert poly(3) == 2*(3**3) + 4*(3**2) + 3 + 2
		
		//property test
		def shouldEqual = {y-> poly(y) == 2*(y**3) + 4*(y**2) + y + 2}
		GruesomePlus.assertForAll(shouldEqual, [Gruesome.genInt])
	}
	
	void testAndShowComprehensionWithUnregisteredMonad() {
		
		MonadWithEmptyDescription m = new MonadWithEmptyDescription(){
			public Closure getPure() {
				{v -> new MyMbox(v)}
			}
	
			public Closure getBind() {
				f {Closure fn, MyMbox mb -> fn(mb.value)}
			}

			@Override
			public Object getEmptyM() {
			 	new MyMbox()
			}			
		}
		
		Closure test1 = {MyMbox mbox ->
	        comprehend (m) {
				x << from { mbox }
				restrict {x<10}
				outputP{  x+1 }
			}
		}

		Closure test1b = {MyMbox mbox ->
			comprehend (m) {
				x << from { mbox }
				restrict {x<10}
				output{ new MyMbox(x+1) }
			}
		}

		Closure test2 = {MyMbox mbox ->
	        selectP { x+1 }.using(m).from{
				x << { mbox }
				where {x<10}
			}
		}

		Closure test2b = {MyMbox mbox ->
			select { new MyMbox(x+1) }.using(m).from{
				x << { mbox }
				where {x<10}
			}
		}

		//that would be easier with spock		
		def testAsserts = {Closure toTest ->
			assert toTest(new MyMbox(5)) instanceof MyMbox
			assert toTest(new MyMbox(5)).value == 6
			assert toTest(new MyMbox(11)).isEmpty == true	
		}
		
		testAsserts test1
		testAsserts test1b
		testAsserts test2
		testAsserts test2b
	}
	
	class MyMbox {
		boolean isEmpty = false
		MyMbox(){
			isEmpty = true
		}
		MyMbox(v){
			value = v
		}
		def value
	}
}
