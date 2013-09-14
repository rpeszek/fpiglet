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
package fun.expressions

import static fpig.common.functions.FpigBase.f
import static fpig.common.functions.FpigBase._
import static fpig.common.functions.FpigBase.fH
import static fpig.common.functions.FpigBase.just
import static fpig.common.functions.FpigBase.unjust
import static fpig.common.functions.FpigBase.nothing
import static fpig.common.functions.FromOperators.*
import static fpig.expressions.MComprehensionSyntax.comprehend
import static fpig.expressions.MComprehensionSelectSyntax.select
import static fpig.expressions.MComprehensionSelectSyntax.selectP
import static fpig.groovylist.asfunlist.functions.InAndOutOfFunLists.*
import test.utils.GruesomePlus;
import fpig.common.functions.ClosureMonad;
import fpig.common.functions.FpigBase;
import fpig.common.functions.MaybeMonad;
import fpig.common.types.ClosureHolder;
import fpig.common.types.Maybe;
import fpig.funlist.functions.FunListMonad;
import gruesome.Gruesome;

class MComprehendSyntaxTests extends GroovyTestCase{


    void testMaybeComprehension() {
		def res = comprehend {
			x << from{ just(3) }
			y << from{ just(2) }
			output{ just(x+y) }
		}					
		
		assert 5 == unjust(res)	
 	}

	
	void testMaybeComprehension_outputPure() {
		def res = comprehend {
			x << from{ just(3) }
			y << from{ just(2) }
			outputP{ x+y }
		}
		assert res instanceof Maybe
		assert 5 == unjust(res)
	}

	void testMaybeComprehension2() {
		def res = comprehend {
			x << from{ just(3) }
			y << from{ just(x+1) }
			output{ just(y-x) }
		}
		
		assert 1 == unjust(res)
	 }
	
	void testMaybeComprehension3() {
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

	void testMaybeComprehension4() {
		Closure maybeDivide = f { Maybe num, Maybe den ->
			select{just(x/y)}.from {
				x << { num }
				y <<  { den }
				where { y != 0}
			}
		}
		
		
		def sixDividedBy = maybeDivide(just(6))
		assert just(2) == sixDividedBy(just(3))
		assert nothing() == sixDividedBy(just(0))
	}

	

	void testListComprehension() {
		def res = comprehend {
			x << from{ f ([1,2,3]) }
			y << from{ f ([10,100]) }
			output{ f([x*y]) }
		}
		
		def resList = funlistOut << res
		
		assert [10,100,20,200,30,300] == resList
	 }

	void testListComprehension_restrict1() {
		def res = comprehend {
			x << from{ f ([1,2,3]) }
			restrict { x < 3 }
			y << from{ f ([10,100]) }
			output{ f([x*y]) }
		}
				
		def resList = funlistOut << res
		
		assert [10,100,20,200] == resList

	 }

	void testListComprehension_restrict2() {
		def res = comprehend {
			x << from{ f ([1,2,3]) }
			y << from{ f ([10,100]) }
			restrict { x + y < 100 }			
			output { f([x*y]) }
		}
				
		def resList = funlistOut << res
		
		assert [10,20,30] == resList
	 }

	void testListComprehension_outputP() {
		def res = comprehend {
			x << from{ f ([1,2,3]) }
			y << from{ f ([10,100]) }
			restrict { x + y < 100 }
			outputP { x*y }
		}
				
		def resList = funlistOut << res
		
		assert [10,20,30] == resList
	 }

	void testIndempotenceOfFunlistOut() {
		//test for issue# 10
		def b = FpigBase.b
		def list1 = f ([1,2,3])
		def list2 = f ([10,100])
		def res = comprehend {
			x << from{  f ([1,2,3]) }
			y << from{  f ([10,100]) }
			output{
				f([x * y])
			}
		}
		
		//issue: the following produce different results!
		def out1 = funlistOut << res
		assert out1 == funlistOut << res  //issue #10 failing
		assert out1 == funlistOut << res
		
		
		//THIS is idempotent!
		def cx = {->
			b({z1->
			  def x=z1
			  return {->
				 b({z2-> def y=z2;
				   {zz-> f([x * y])}()}) << list2
			  }()
			}) << list1
		}()
		def toString1 =  cx.toString() //both will invoke funlistOut internally
		def toString2 =  cx.toString()
		println toString1
		println toString1
		assert toString1 == toString2
		
	}

	void testPrototypeSolutionToIssue10() {
		//the solution is to have the comprehension DSL mimic the following code.
		//note parameters are cloned as this goes up the stack. This mimics closures on a true call stack.
		def b = FpigBase.b
		def v0 = [:]
		def cx_ = {
		  {v1 ->
			b({z1->
			  v1.x=z1
			  return {v2->
				 b({z2-> v2.y=z2;
				   {zz-> f([v2.x * v2.y])}();
				   }) << f ([10,100])
			  }(v1.clone())
			}) << f ([1,2,3])
		  }(v0.clone())
		}()
		def toString1_ =  cx_.toString() //both will invoke funlistOut internally
		def toString2_ =  cx_.toString()
		println toString1_
		println toString2_
		assert toString1_ == toString2_

	}
	
	void testListComprehension_select() {
		def res = select { f([x*y]) }.from {
			x << { f ([1,2,3]) }
			y << { f ([10,100]) }
			where { x + y < 100}
		}
				
		def resList = funlistOut << res
		
		assert [10,20,30] == resList

	 }

	void testListComprehension4_selectPure() {
		def res = selectP { x*y }.from {
			x << { f ([1,2,3]) }
			y << { f ([10,100]) }
			where { x + y < 100}
		}
				
		def resList = funlistOut << res
		
		assert [10,20,30] == resList
	 }

	void testListComprehension_whereRemovesAll() {
		def res = select { f([x*y]) }.from {
			x << { f ([1,2,3]) }
			y << { f ([10,100]) }
			where { x + y > 1000}
		}
				
		def resList = funlistOut << res
		
		assert [] == resList

	 }
	
	void testClosureMonadComprehension() {
		def c1 = fH({ 50 })		
		def c2 = fH({ 20 })
				
		def res = select { fH({x + y}) }.from {
			x << { c1 }
			y << { c2 }
		}
		assert res.cVal() == 70		
	}
	
	void testClosureMonadComprehension_zeroArgsClosures() {
		def c1 = fH({ -> 50 })
		def c2 = fH({ -> 20 })
				
		def res = select { fH({x + y}) }.from {
			x << { c1 }  
			y << { c2 }
		}
		assert res.cVal() == 70		
	}

	void testClosureMonadComprehension_zeroArgsClosures_autoBox() {
		def c1 = { -> 50 }
		def c2 = { -> 20 }
				
		def res = selectP { x + y }.from {
			x << { c1 }
			y << { c2 }
		}
		assert res() == 70
	}

	
	void testClosureMonadComprehension3() {
		ClosureHolder res = selectP { a + b }.from {
			a << { fH(POWER(_,3)) }
			b << { fH(POWER(_,2)) }
		}
		assert res.cVal(2) == 2**3 + 2**2
	}

	void testClosureMonadComprehension4() {
		Closure res = selectP { a << b }.from {
			a << { PLUS }
			b << { TIMES }
		}
		def got= res(5, 1) // first 5 is multiplied by 1 giving it 5, then 5,5 are passed to plus
		assert got == 10
	}

	void testClosureMonadComprehension5() {
		def c1 = f {a, b-> println '1:a=' + a + ",b=" + b; 
			return a+b}
		def c2 = f {a, b-> println '2:a=' + a + ",b=" + b; 
			return a*b}
		Closure res = selectP { fc << gc }.from {
			fc << { c1 }
			gc << { c2 }
		}
		def got= res(5, 1)
		assert got == 10
	}

	
	void testClosureMonadComprehension6() {
		def fib
		fib = {n->
			if(n==0){
				0
			} else if (n==1){
			    1
			} else {
			   selectP { n1 + n2 }.from {
				   n1 << { fib << MINUS(_,1) }
				   n2 << { fib << MINUS(_,2) }
			   } (n)
			}
		}//.memoize()
		assert fib(5) + fib(6) == fib(7)
		
//		def start = new Date()
//		(1..30).each{ println fib(it) }		
//		println 'duration ' + (new Date().time - start.time)
		
	}

}
