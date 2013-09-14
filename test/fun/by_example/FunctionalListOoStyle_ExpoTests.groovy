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

import fpig.funlist.types.*
import static fpig.common.functions.FromOperators.*


/**
 * Hopefully nobody will ever use OO style coding with Fpiglet, but
 * if someone needs it here are some examples.
 * 
 * @author Robert Peszek
 */
class FunctionalListOoStyle_ExpoTests extends GroovyTestCase{

    void testOoStylePrepending() {
        FunList empty = FunList.EMPTYLIST
        def one_two_tree = empty.prepend(3).prepend(2).prepend(1)

        assert [1,2,3] == one_two_tree.fetch(10)
        assert [1,2,3] == one_two_tree.fetchAll()
    }

    void testOoStyleWithConvertedList() {
        FunList testList = FunList.fromOoList([1,5,6])
        assert [1,5,6] == testList.fetch(10)
        assert [1,5,6] == testList.fetchAll()
    }

    void testLargeList() {
        FunList largeList = FunList.fromOoList(1..10000)
        FunList squaresPlusOneDivisibleBy5 = FunList.fromOoList(1..10000).map{1 + it * it}.filter{it % 5==0}
        assert [5,10,50] == squaresPlusOneDivisibleBy5.fetch(3)
		
		//or:
		assert [5,10,50] == largeList.map{1 + it * it}.filter{it % 5==0}.take(3).fetchAll()
		assert [5,10,50] == largeList.map{1 + it * it}.filter{it % 5==0}.takeWhile(SMALLER(51)).fetchAll()
		
		//next 3
		assert [65,145,170] == largeList.map{1 + it * it}.filter{it % 5==0}.drop(3).take(3).fetchAll()
		assert [65,145,170] == largeList.map{1 + it * it}.filter{it % 5==0}.dropWhile(SMALLER(51)).take(3).fetchAll()
		
    }

    void testStream() {
        FunList naturalnumbers = FunList.funStreamFrom(1)
        FunList squaresPlusOneDivisibleBy5 = naturalnumbers.map {1 + it * it}.filter {it % 5==0}

        assert [5,10,50] == squaresPlusOneDivisibleBy5.fetch(3)
        assert [5,10,50] == squaresPlusOneDivisibleBy5.take(3).fetchAll()
        assert squaresPlusOneDivisibleBy5.fetch(30).size() == 30
    }
}
