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
import static fpig.funlist.functions.BaseFL.*
import static fpig.funlist.functions.Infinity.*
import fpig.funlist.functions.Infinity;
import fpig.funlist.types.FunList;
import fpig.funlist.types.LazyList;
import groovy.lang.Closure;

class FpigletPlaysWithInfinity_ExpoTests extends GroovyTestCase{
	FunList getNaturalnumbers() {
		naturalNumbers()
	}			

	/**
	 * This test shows off infinite list (stream) capability
	 */
    void testAndShowApplyingMapAndFileterToInfiniteList() {
		
        //define manipulation an composition of map and filter 
		//outputs numbers if format 1+n^2 which are divisible by 5
        def manipulation = funlistOutTake(3) << filter {it % 5==0} << map {1 + it * it}
        assert manipulation instanceof Closure
        assert [5,10,50] == manipulation << funlistIn << (1..100)
		assert [5,10,50] == manipulation << naturalnumbers

        def fmanipulation = filter {it % 5==0} << map {1 + it * it}
		//if you time it the next line is going to take few milliseconds 
        def foutput = fmanipulation (naturalnumbers)
		
		//this is going to take a bit longer
        assert [5,10,50] == funlistOutTake(3)(foutput)
    }

	
	void testAndShowRightFoldingInfinity() {		
		//first example shows how we can find a given object in a list using folding.
		//this is almost equivalent of List.contains
		def findMyB_ = f {x, b, Closure acc -> (b==x) ? b: acc()}
				
		assert 100 == foldXR(findMyB_(100), 0) << funlistIn << [110,100,20,50,500,300]
		assert 100 == reduceXR(findMyB_(100)) << funlistIn << [110,100,20,50,500,300]
		
		//... and we can find our number in infinite list by right-folding it!
		//NOTE depends on configured stack size I am keeping this number low (50) to avoid stack overflows
		assert 50 == foldXR(findMyB_(50), 0) << naturalnumbers
		assert 50 == reduceXR(findMyB_(50)) << naturalnumbers
		
		//let us implement more generic find using fold!
		def foldFinder = f {Closure predicate, b, Closure acc -> predicate(b) ? b: acc()}

		//lets find first number divisible by 7
		assert 14 == reduceXR(foldFinder {it % 7 == 0}) << funlistIn << [1,5,14,10,28]
		
		//what is first number > 50 divisible by 7?  Right-folding infinity will tell us:
		assert 56 == reduceXR(foldFinder {it % 7 == 0 && it>50}) << naturalnumbers
		
		//but here is a simpler way to do that without folding, this way is also StackOverflow safe:
        def divisibleBy7AndLargerThan50 = filter {it % 7 == 0} << filter(LARGER(50)) << naturalnumbers
		def result = funlistOutTake(1) << divisibleBy7AndLargerThan50
		assert [56] == result
	}	
	
	void testLeftFoldWithStoppingRule() {
	    def firstSumOfSquaresLargerThan101 = foldLUntil(LARGER(101), PLUS, 0) << map(POWER(_,2)) << naturalnumbers
	    assert 140 == firstSumOfSquaresLargerThan101
	}
	
	void testAndShowStreams() {
		assert [1,1,1] == funlistOut << take(3) << repeat(1)
		
		assert [1,1,1] == funlistOut << take(3) << map{1} << naturalNumbers()
		
		assert [1,2,4] == funlistOut << take(3) << iterate(TIMES(2), 1)
		
		assert [1, 1,2, 1,2,3, 1,2,3,4] ==  funlistOutTake(10) << concat << map(range(1)) << naturalNumbers()
	}
}
