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
package fun.groovylists

import static fpig.common.functions.FpigBase.*
import static fpig.common.functions.FromOperators.*
import static fpig.groovylist.asfunlist.functions.GroovyListAsFunList.*

/**
 * Tests all the goodies converted over from functional lists to groovy lists 
 * all thanks to the curried function called withFunList
 * 
 * @author Robert Peszek
 */
class ListAsFunListsTests extends GroovyTestCase{


	void testLargeList() {
		//tests numbers in the form n^2 + 1 which can be devided by 5
		def res =  filter {it % 5==0} << map {1 + it * it}  << (1..10000)
		assert res instanceof List
		assert [5,10,50] == res.take(3)
		//or
		assert [5,10,50] ==  take(3) << filter {it % 5==0} << map {1 + it * it} << (1..10000)
	}

	void testReverse() {		
		assert [3,5,2] == reverse << [2,5,3]
	}

	void testFoldL() {
 		assert 10 == foldL(PLUS, 0) << [2,5,3]
		 
		Closure sumAll = foldL(PLUS, 0)
		assert sumAll([2,5,3]) == 10
 
	}

	void testReduceL() {
		Closure sumAll = reduceL PLUS		
		assert sumAll([2,5,3]) == 10
		
		assert 10 == reduceL(PLUS) << [2,5,3]
	}


}
