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
package fun.lists_as;

import static org.junit.Assert.*;
import groovy.util.GroovyTestCase;
//import static fpig.common.functions.FpigBase.*
import static fpig.common.functions.FromOperators.*

abstract class Base_AsFunListFunctions extends GroovyTestCase{

	abstract def getDataIn()
	abstract void assertEmpty(data)
    abstract def getModule()
	abstract Closure getToList()	

	void testAllFunctions(){
		//assertEmpty( module.empty )
 		assert 'a' == module.head << dataIn
		assert ['b','c'] == toList<< module.tail << dataIn
		assert 3 ==  module.length << dataIn
		assert 'c' == module.foldL(MAX, 'a') << dataIn
		assert 'c' == module.foldLUntil({false}, MAX, 'a') << dataIn
		assert 'a' == module.foldLUntil({true}, MAX, 'a') << dataIn
		assert 'c' == module.reduceL(MAX) << dataIn
		assert 'c' == module.foldR(MAX, 'a') << dataIn
		assert 'c' == module.reduceR(MAX) << dataIn
		assert ['c'] == toList<< module.filter({it == 'c'}) << dataIn
		assert (module.isEmpty << module.filter({it == 'd'}) << dataIn)

		assert ['a', 'b'] == toList << module.take(2) << dataIn
		assert ['a', 'b'] == toList << module.takeWhile({it != 'c'}) << dataIn

		assert ['c'] == toList << module.drop(2) << dataIn
		assert ['c'] == toList << module.dropWhile({it != 'c'}) << dataIn

		assert ['c','b','a'] == toList << module.reverse << dataIn

	}
}
