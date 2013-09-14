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
package fun.lists

import static fpig.common.functions.FpigBase.*
import static fpig.groovylist.asfunlist.functions.InAndOutOfFunLists.*
import static fpig.funlist.functions.BaseFL.*
import static fpig.common.functions.FromOperators.*
import static fpig.common.functions.Projections.*
import fpig.funlist.functions.EmptyListException;
import fpig.funlist.functions.Infinity;
import fpig.funlist.types.FunList;
import fpig.funlist.types.LazyList;
import groovy.lang.Closure;

class FunListTests extends GroovyTestCase{


    void testGoryDetails() {
        assert funlistIn([1,5,6]) instanceof LazyList
        assert map {it} << funlistIn([1,5,6]) instanceof LazyList
        assert funlistOut << map {it} << funlistIn([1,5,6]) instanceof List
        assert ID.parameterTypes.size() == 1
    }

    void testConvertedList() {
        assert [1,5,6] == funlistOut << funlistIn([1,5,6]) //alternative notation 1
        assert [1,5,6] == funlistOut << funlistIn << [1,5,6] //alternative notation 2
        assert [1,5,6] == funlistOut << map {it} << funlistIn << [1,5,6]
        assert [1,5,6] == funlistOut << map(Closure.IDENTITY) << funlistIn << [1,5,6]
        assert [1,5,6] == funlistOut << map(ID) << funlistIn << [1,5,6]
        assert [1,5,6] == funlistOut << map {it-1} << map {it+1} << funlistIn << [1,5,6]
		assert [] == funlistOut << empty()
		assert [] == funlistOut << funlistIn << []
    }
				
    //moved test out to FunctionalLists_ExpoTests

	
    void testFoldLBoudaryCond(){
		assert 0 == withFunList(foldL(PLUS, 0)) << []
		assert 1 == withFunList(foldL(PLUS, 0)) << [1]
		assert 2 == withFunList(foldL(PLUS, 0)) << [1,1]
		assert null == withFunList(reduceL(PLUS)) << [] //may change to Maybe
		assert 1 == withFunList(reduceL(PLUS)) << [1] 
		assert 2 == withFunList(reduceL(PLUS)) << [1,1] 
		assert 3 == withFunList(reduceL(PLUS)) << [1,1,1] 
	}
	
	/*
	 * verifies right-left iteration on folds
	 */
	void testLFoldsInterationGoryUnit() {
		def aTester = []
		def bTester = []
		Closure tester = {a,b -> 
			aTester.add(a); 
			bTester.add(b); 
			10 * b
		}
   	    def input = funlistIn << (1..4)
		
		def res = foldL(tester, 0) << input
		
		assert aTester  == [0, 10, 20, 30]
		assert res == 40
		assert bTester == 1..4
		
		aTester = []; bTester = []
		res = reduceL(tester) << input
		
		assert aTester  == [1, 20, 30]
		assert res == 40
		assert bTester == 2..4	   
	}
	
	/*
	* verifies right-left iteration on folds
	*/
   void testRFoldsInterationGoryUnit() {
	   def aTester = []
	   def bTester = []
	   Closure tester = {b,a ->
		   aTester.add(a);
		   bTester.add(b);
		   10 * b
	   }
	   Closure testerX = {b,a ->
		   def a_ = a()
		   aTester.add(a_);
		   bTester.add(b);
		   10 * b
	   }

	   def input = funlistIn << (1..4)
	   
	   def res = foldR(tester, 0) << input
	   
	   assert aTester  == [0, 40, 30, 20]
	   assert res == 10
	   assert bTester == 4..1
	   
	   aTester = []; bTester = []
	   res = reduceR(tester) << input
	   
	   assert aTester  == [4, 30, 20]
	   assert res == 10
	   assert bTester == 3..1
	   
	   aTester = []; bTester = []
	   res = reduceXR(testerX) << input
	   
	   assert aTester  == [4, 30, 20]
	   assert res == 10
	   assert bTester == 3..1

    }
   
	
    void testTake() {
		FunList naturalnumbers = Infinity.naturalNumbers()
		FunList largeSet = funlistIn << (1..10000)
		
		assert (1..1000) ==  funlistOut << take(1000) << naturalnumbers
		assert (1..1000) ==  funlistOut << take(1000) << largeSet
		assert (1..3)   == funlistOut << take(1000) << funlistIn << (1..3)
		assert [] == funlistOut << take(1000) <<  empty()
	 }
	
	void testTakeWhile() {
		FunList naturalnumbers = Infinity.naturalNumbers()
		FunList largeSet = funlistIn << (1..10000)
		
		assert (1..1000) ==  funlistOut << takeWhile(SMALLER(1001)) << naturalnumbers
		assert (1..1000) ==  funlistOut << takeWhile(SMALLER(1001)) << largeSet
		assert (1..3)   == funlistOut << takeWhile(SMALLER(1001)) << funlistIn << (1..3)
		assert [] == funlistOut << takeWhile(SMALLER(1001)) <<  empty()
	}

	void testDrop() {
		FunList naturalnumbers = Infinity.naturalNumbers()
		FunList largeSet = funlistIn << (1..10000)
		
		assert [1001] ==  funlistOutTake(1) << drop(1000) << naturalnumbers
		assert (1001..10000) ==  funlistOut << drop(1000) << largeSet
		assert isEmpty << drop(1000) << funlistIn << (1..3)
		assert [] == funlistOut << drop(1000) <<  empty()
	}
	
	void testDropWhile() {
		FunList naturalnumbers = Infinity.naturalNumbers()
		FunList largeSet = funlistIn << (1..10000)
		
		assert [1001] ==  funlistOutTake(1) << dropWhile(SMALLER(1001)) << naturalnumbers
		assert (1001..10000) ==  funlistOut << dropWhile(SMALLER(1001)) << largeSet
		assert []  == funlistOut << dropWhile(SMALLER(1001)) << funlistIn << (1..3)
		assert [] == funlistOut << dropWhile(SMALLER(1001)) <<  empty()
	}

	void testFilteringForStackOverflow() {
		//filtering will stack overflow if it needs to evaluate too much
		FunList naturalnumbers = Infinity.naturalNumbers()
		
		def allLargerThan100 = filter(LARGER(100)) << naturalnumbers //no problem
		assert [101] == funlistOutTake(1) << allLargerThan100
		
		//will stack overflow (trampoline not working)
		def allLargerThan1000 = filter(LARGER(1000)) << naturalnumbers
		
		//
		def squaresPlusOnes =  map {1 + it * it} << funlistIn << (1..10000)
		def filtered = filter {it % 5==0} << squaresPlusOnes
		assert [5,10,50] == funlistOutTake(3) << filtered
		
	}
		
	void testFoldLForStackOverflow() {
		def largeSet = funlistIn << (1..10000)  //1000000 takes longer but still works
		def folded = foldL(MAX, 0) << largeSet
		assert folded == 10000
		
	}
	
	void testFoldR_Will_StackOverflow() {
		//NOTE flowR will stack overflow!  It is function composition by definition!
//		def largeSet = funlistIn << (1..10000)
//		def folded = foldR(MAX, 0) << largeSet
//		assert folded == 10000	
	}
	
	void testMapForStackOverflow() {
		def largeSet = funlistIn << (1..10000)
		def mapped = map (TIMES(2)) << largeSet
		assert [2,4,6] == funlistOutTake(3) << mapped
	}
	
	void testReverseForStackOverflow() {
		def largeSet = funlistIn << (1..10000)
		def reversed = reverse << largeSet
		assert (10000..1) == funlistOut << reversed
	}
	
	void testLengthStackOverflow() {
		def largeSet = funlistIn << (1..10000)  //1000000 takes longer but still works
		assert 10000 == length << largeSet
	}

	void testRangeFunction() {
		assert 1..5 == funlistOut << range(1,5)
	}
	
	void testConcatFunction() {
		assert 1..10 == funlistOut << concat([range(1,5), range(6,10)])
 		assert 1..10 == funlistOut << concat([range(1,5), empty(), empty(), range(6,10)])
 		assert 1..10 == funlistOut << concat([empty(), empty(), range(1,5), range(6,10)])    
		assert 1..10 == funlistOut << concat << (e(range(1,5)) << e(range(6,10)) << empty())
    }
	
	void testHeadFunctions() {
		assert head << f([1,2]) == 1
		assert headM << f([1,2]) == just(1)
		assert headM << empty() == nothing()
		shouldFail(EmptyListException){
			head << empty()
		}
	}
	
	void testIndempotenceOfFunlistOut() {
		//test doublechecking against issue# 10
		//issue #10 (google.code) is related to monad comprehension to these are not very relevant
		def list = funlistIn << (1..10)
		assert 1..10 == funlistOut << list
		assert 1..10 == funlistOut << list
		assert 1..10 == funlistOut << list
		assert 1..10 == funlistOut << list
		
	}
	
	void testEq() {
		assert eq(f([1,2,3])) << f(1..3)
		assert !(eq(f([1,2,3])) << f(1..4))
		assert !(eq(f([1,2,5,5])) << f(1..4))
		assert eq(f(1..500)) << f(1..500) //stack overflow test 
	}
	
	void testToString() {
		def list = [1,2,3,4,5,6,7,8,9,10]
		def expected = 'f(' + list.toString() + ')'
		def got =  f(list).toString()
		assert expected == got
		
		def list2 = [1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10]
		def expected2 = 'f(' + (list2.take(19) + ['...']).toString() + ')'
		def got2 =  f(list2).toString()
		assert expected2 == got2
	}
}
