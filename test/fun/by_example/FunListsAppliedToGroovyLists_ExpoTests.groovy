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
import static fpig.common.functions.Projections.*
import fpig.groovylist.asfunlist.functions.FunListToListFunctor;
import static fpig.groovylist.asfunlist.functions.GroovyListAsFunList.*
import static fpig.groovylist.asfunlist.functions.InAndOutOfFunLists.*
import static fpig.funlist.functions.BaseFL.map as FunList_map
import static fpig.funlist.functions.BaseFL.filter as FunList_filter


/**
 * Shows examples of functional syntax converted to Groovy/Java Lists.
 *
 * Functor working under the hood is not shown.
 * 
 * @author Robert Peszek
 */
class FunListsAppliedToGroovyLists_ExpoTests extends GroovyTestCase{

//todo think of better tests
	void testAndShowFunListFunctionsConvertedToGroovy() {
		
		//functionaly implemented reverse applied to groovy list		
		assert (10..1) == reverse << (1..10)
		
		//functionally implemented folds
		Closure smallest = foldL(MIN, 20)
		assert smallest([7,2,5,3]) == 2
		
		//functionally implemented reduceL
		Closure smallest2 = reduceL MIN
		assert smallest2([54,4,34]) == 4
		
		Closure last = reduceL SECOND
		assert last([54,4,34]) == 34
		
		//functionally implemented foldR
		Closure sum = reduceR PLUS
		assert sum([4,4,3]) == 11
		
	}
	
	void testAndShowMoreComplexUsage() {
		//composition of functionally implemented map and filter (Functor law, really)
		//tests numbers in the form n^2 + 1 which can be devided by 5
		def res =  filter {it % 5==0} << map {1 + it * it}  << (1..10000)
		assert res instanceof List
		assert [5,10,50] == res.take(3)
		
		//or in one swoop using functional take:
		assert [5,10,50] == take(3) << filter {it % 5==0} << map {1 + it * it}  << (1..10000)
		assert [5,10,50] == takeWhile(SMALLER(51)) << filter {it % 5==0} << map {1 + it * it}  << (1..10000)

		//and next 3 numbers are:
		assert [65,145,170] == take(3) << drop(3) << filter {it % 5==0} << map {1 + it * it}  << (1..10000)
		assert [65,145,170] == take(3) << dropWhile(SMALLER(51)) << filter {it % 5==0} << map {1 + it * it}  << (1..10000)

		
		//more explicit version of the code above 
		//please examine static imports for definition of FunList_filter and FunList_map
		assert [5,10,50] == withFunList (FunList_filter {it % 5==0}) << withFunList (FunList_map {1 + it * it} ) << (1..7)

		//this test shows that the conversion is Functor (second law)
		//also note that this version is faster (fewer back and forths)
		assert [5,10,50] == withFunList (FunList_filter {it % 5==0} << FunList_map {1 + it * it})  << (1..7)
		
		//even more explicit version of the code above
		Closure fmap = FunListToListFunctor.statics.fmap
		
		assert [5,10,50] == fmap(FunList_filter {it % 5==0} << FunList_map {1 + it * it})  << (1..7)
		assert [5,10,50] == fmap(FunList_filter {it % 5==0}) << fmap (FunList_map {1 + it * it} ) << (1..7)

	}

	void testAndShowFolding() {
		def mySum = reduceL(PLUS)
		assert 10 == mySum ([2,2,5,1])
		assert [1,2,-1,-2,5]== mySum ([[1,2], [-1,-2],[5]])

		def mySum2 = reduceR(PLUS)
		assert 10 == mySum2 ([2,2,5,1])
		assert [1,2,-1,-2,5]== mySum2 ([[1,2], [-1,-2],[5]])
		
		//try to add numbers until, but stop at 10
		def sumIfSmallerThan10L =  {a, b -> 
			(a+b > 10)? a: (a + b)
		}
		def addUntil10_Left =  reduceL(sumIfSmallerThan10L) 

		def sumIfSmallerThan10R =  flip(sumIfSmallerThan10L) //this reverses a and b parameters
		def addUntil10_Right = reduceR(sumIfSmallerThan10R) 
		
		//this demonstrates that folding from right and left can yield different results
		assert 7 == addUntil10_Left << [5,2,6,4]
		assert 10 == addUntil10_Right << [5,2,6,4]
		
		//other fun experiments
		def addTo = foldL(PLUS)
		assert 20 == addTo(10) << [2,2,5,1] 	
		
		def addTo2 = foldR(PLUS)
		assert 20 == addTo2(10) << [2,2,5,1]

		def lastElement = reduceL(SECOND)
		assert 1 == lastElement ([2,2,5,1])		
	}

}
