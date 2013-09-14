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
import static fpig.groovylist.asfunlist.functions.InAndOutOfFunLists.*
import static fpig.funlist.functions.BaseFL.*
import static fpig.funlist.functions.Infinity.*
import static fpig.common.functions.FromOperators.*
import static fpig.common.functions.Projections.*
import fpig.applicative.functions.BaseA;
import fpig.concepts.ApplicativeDescription;
import fpig.funlist.functions.FunListZipApplicative;
import fpig.funlist.functions.Infinity;
import fpig.funlist.types.FunList;
import fpig.funlist.types.LazyList;
import groovy.lang.Closure;

class FunctionalLists_ExpoTests extends GroovyTestCase{
				
	void testPrependingSytax() {
		 def list = e(1) << e(2) << e(3) << e(4) << empty()
		 assert [1,2,3,4] ==  funlistOut << list
	}

	void testConcatenatingStrings() {
		assert 1..10 == funlistOut << concat << [range(1,5), range(6,10)]
		assert 1..10 == funlistOut << concat << (e(range(1,5)) << e(range(6,10)) << empty())
		
		//try timing it
		def veryBigList = concat << [range(1,100000), range(200000,1000000)]
		
		assert 1..10 == funlistOutTake(10) << veryBigList		
	}
	
	void testAndShowRanges() {
		Date today = new Date()
		Date in5days = today + 5
		(today..in5days) == funlistOut << range(today, in5days) //range assumes only that next() is implemented and final value works as comparator
		assert ('a'..'z') == funlistOut << range('a', 'z')
	}
	
	void testListIsImmutable() {
		//list is immutable by design, but it does not prevent 
		//from changes to objects placed on it.
		def list = e(1) << e(2) << e(3) << e(4) << empty()
		def list2 = e(0) << list
		
		assert [1,2,3,4] ==  funlistOut << list
		assert [0,1,2,3,4] ==  funlistOut << list2
		
		def someObject = [:]
		def list3 = e(someObject) <<  empty()
		someObject.newData = 'test'
		assert list3.head.newData == 'test'
    }

	/**
	 * Test introduces a problem which comes back in other tests:
	 *    numbers in the form n^2 + 1 which can be divided by 5
	 */
    void testAndSeeFilterAndMapComposition() {
		
		//my favorite syntax takes advantage of Groovy << overloading for function composition as well as passing arg to a closure
        assert [5,10,50] == funlistOutTake(3) << filter {it % 5==0} << map(PLUS(1) << POWER(_,2)) << funlistIn << (1..10000)        
		//more explicit syntax
		assert [5,10,50] == (funlistOutTake(3) << filter {it % 5==0} << map {1 + it * it} << funlistIn) (1..10000)
		//more usign range
		assert [5,10,50] == (funlistOutTake(3) << filter {it % 5==0} << map {1 + it * it} ) << range(1,10000)

		//next version emphasises separation of manipulation from data								
        def manipulation = funlistOutTake(3) << filter {it % 5==0} << map {1 + it * it} << funlistIn
        assert manipulation instanceof Closure
        assert [5,10,50] == manipulation(1..10000)

        def fmanipulation = filter {it % 5==0} << map {1 + it * it}
        def finput = funlistIn(1..10000)
		
		//if you were to time this call it would take couple of milliseconds
        def foutput = fmanipulation (finput)
		
		//that is where the computing cost is
        assert [5,10,50] == funlistOutTake(3)(foutput)
    }

	/**
	 * This test shows off infinite list (stream) capability
	 */
    void testInfiniteList() {
        FunList naturalnumbers = Infinity.naturalNumbers()
        assert [5,10,50] == funlistOutTake(3) << filter {it % 5==0} << map {1 + it * it} << naturalnumbers
        assert [5,10,50] == (funlistOutTake(3) << filter {it % 5==0} << map {1 + it * it}) (naturalnumbers)

        def manipulation = funlistOutTake(3) << filter {it % 5==0} << map {1 + it * it}
        assert manipulation instanceof Closure
        assert [5,10,50] == manipulation(naturalnumbers)

        def fmanipulation = filter {it % 5==0} << map {1 + it * it}
        def finput = funlistIn(1..10000)
        def foutput = fmanipulation (naturalnumbers)
        assert [5,10,50] == funlistOutTake(3)(foutput)
    }

	void testFoldLAndReduceL() {
		assert 10 ==  foldL(PLUS, 0) << funlistIn << [2,5,3]
		
		//.. or
		Closure sumAll = withFunList (foldL (PLUS, 0))		
		assert sumAll([2,5,3]) == 10
		
		//..or simply
  		assert 10 == reduceL(PLUS) << funlistIn << [2,5,3]  
		assert 10 == reduceL(PLUS) << funlistIn << [2,8]
	}
	
	void testFoldRAndReduceR() {		
		assert 10 ==  foldR(PLUS, 0) << funlistIn << [2,5,3]
		
		//this version of foldR is designed to work on infinite lists
		def plusX = f {b, Closure a -> a() + b}
		assert 10 ==  foldXR(plusX, 0) << funlistIn << [2,5,3]		
	}
	
	void testRightFoldsAsFinders() {
		def findMyB = f {x, b, acc -> (b==x) ? b: acc}
		def findMyB_ = f {x, b, Closure acc -> (b==x) ? b: acc()}
		
		assert 100 == foldR(findMyB(100), 0) << funlistIn << [110,100,20,50,500,300]
		assert 100 == reduceR(findMyB(100)) << funlistIn << [110,100,20,50,500,300]
		
		
		assert 100 == foldXR(findMyB_(100), 0) << funlistIn << [110,100,20,50,500,300]
		assert 100 == reduceXR(findMyB_(100)) << funlistIn << [110,100,20,50,500,300]
		
		//NOTE: more finding on infinite lists with more interesting finding code in FpigletPlaysWithInfinity_ExpoTests 
	}

	void testFoldingInfiniteList() {
		def findMyB_ = f {x, b, Closure acc -> (b==x) ? b: acc()}
		//infinite list
		FunList naturalnumbers = FunList.funStreamFrom(1)
		assert 100 == foldXR(findMyB_(100), 0) << naturalnumbers
		assert 100 == reduceXR(findMyB_(100)) << naturalnumbers
	}

	void testAllFolds() {
		//FIRST = f {b, acc -> b}
		assert 2 == foldR(FIRST, 0) << funlistIn << [2,5,6,3]
		assert 0 == foldR(SECOND, 0) << funlistIn << [2,5,6,3]
		assert 2 == reduceR(FIRST) << funlistIn << [2,5,6,3]
		assert 3 == reduceR(SECOND) << funlistIn << [2,5,6,3]

		assert 0 == foldL(FIRST, 0) << funlistIn << [2,5,6,3]
		assert 3 == foldL(SECOND, 0) << funlistIn << [2,5,6,3]
		assert 2 == reduceL(FIRST) << funlistIn << [2,5,6,3]
		assert 3 == reduceL(SECOND) << funlistIn << [2,5,6,3]

		assert 2 == foldXR(FIRST, 0) << funlistIn << [2,5,6,3]
		assert 2 == reduceXR(FIRST) << funlistIn << [2,5,6,3]
		
		def secondX = f {b, Closure acc -> acc()}
		assert 0 == foldXR(secondX, 0) << funlistIn << [2,5,6,3]
		assert 3 == reduceXR(secondX) << funlistIn << [2,5,6,3]
		
		FunList naturalnumbers = Infinity.naturalNumbers()
		assert 1 == foldXR(FIRST, 0) << naturalnumbers //SECOND will obviously stack overflow
		assert 1 == reduceXR(FIRST) << naturalnumbers
	}
	

	void testSumsWithIntermediateResults() {
		//redoing this with monads will be fun
		//this example sums numbers using foldL and foldR but instead of outputing the number, the folding 
		//logic adds it at the beginning of accumulator list
		def summingFunction = {a, b ->
			e(a.head + b) << a  //prepends sum to the list instead of returning it
		}
		
		assert [4,3,2,1,0] == withFunList( foldL(summingFunction, (e(0) <<  empty())) ) << [1,1,1,1]
		assert [10,6,3,1,0] == withFunList( foldL(summingFunction, (e(0) <<  empty())) ) << [1,2,3,4]
		
		assert [4,3,2,1,0] == withFunList( foldR( flip(summingFunction), (e(0) <<  empty())) ) << [1,1,1,1]
		assert [10,9,7,4,0] == withFunList( foldR( flip(summingFunction), (e(0) <<  empty())) ) << [1,2,3,4]	
	}
	
	void testAndShowOtherFunFunctions() {
		def finput = funlistIn([2,5,3])
		def foutput = reverse(finput)
		assert [3,5,2] == funlistOut << foutput
		
		Closure reverseInGroovy = withFunList (reverse)
		assert [3,5,2] == reverseInGroovy([2,5,3])

	}

	void testAndShowZipFunction() {
		def input1 = f (['jon', 'tom', 'bob'])
		def input2 = f(['10', '20', '5', '30'])
		
		def res = zipWith(f {a, b -> "$a score= $b"}, input1, input2)
		assert ['jon score= 10', 'tom score= 20', 'bob score= 5'] == funlistOut << res		
	}
 	
	void testAndShowFunList_asZipApplicative() {
		ApplicativeDescription fa = FunListZipApplicative.instance
		
		def ops = f ([PLUS(10), MINUS(_, 1), TIMES(2)])
		def data = f ([1,2,3,4])
		
		//apply list of operations to list of elements
		def res = fa.ap (ops, data)
		assert [11, 1, 6] == funlistOut << res
		
		BaseA a = BaseA.getInstance(fa)

		//apply different operations to elements of 2 lists
		def ops_2arg = f ([PLUS, PLUS, TIMES])
		def data1 = f ([10, 1, 2])
		def data2 = f ([1,2,3,4])
		
		def res2 = a.ap2(ops_2arg, data1, data2)
		assert [11, 3, 6] == funlistOut << res2
		
		//add elements in 2 lists (since the same op is used, we can use zipWith applicative function:
		def res2b = a.zipWith(PLUS, data1, data2)
		assert [11, 3, 5] == funlistOut << res2b

		//functional programming often uses zip function which creates list of pairs. Here is how we can do that:
		def zipF = f {x, y-> [x,y]}
		def res2c = a.zipWith(zipF, data1, data2)
		assert [[10,1], [1,2], [2,3]] == funlistOut << res2c
				
		//add elements in 3 lists
		def add3 = f {x, y, z-> 
			x + y + z
		}
		FunList ops3 = repeat(add3) //infinite set repeating the same operation

		
		def res3 = a.apN(ops3, [f ([1,2,3]), f ([3,2,1]), f ([10,20,30])])
		assert [14, 24, 34] == funlistOut << res3
		
		//do the same using withNWith (since we are applying the same op to all elements we can use zipWith:		
		def res4 = a.zipNWith(add3, [f ([1,2,3]), f ([3,2,1]), f ([10,20,30])])
		assert [14, 24, 34] == funlistOut << res4
		
	}
}
