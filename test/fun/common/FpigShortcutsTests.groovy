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
package fun.common

import static fpig.common.functions.FpigBase.*


class FpigShortcutsTests extends GroovyTestCase{
	//curried functions and their use is tested in CurriedFunctionsTests.groovy

	void testFunctionApplication() {
		def thrice = { a -> a * 3 }
		assert apply(2)(thrice) == 6

		def listOfClosures = [
			{it + 1},
			{it*it},
			{it}
		]
		//shows how lists of closures can be mapped using an argument, sorry for using Groovy lists!
		assert [3, 4, 2]== listOfClosures.collect (apply(2))

		def listOfMulticlosures = [
			f{a,b -> a+b},
			f{a,b-> a*b},
			f{a, b -> a},
			f{a, b-> b}
		]
		def appliedList = listOfMulticlosures.collect(apply(2))
		assert [2+3, 2*3, 2, 3]== appliedList.collect(apply(3))

	}

	void testFlips() {
		def c = {a, b -> a}
		assert c(2,5) == 2
		assert flip(c)(2,5) == 5

		def c2 = {x, y, z -> x}
		assert c2(2,5,7) == 2

		//flip(c2)(2,5,7) will not work

		assert flip(f(c2))(2,5)(7) == 5 //TODO that seems to be wrong, need to think about curring more
		// assert flip(f(c2))(2,5,7) == 5 //currently this will not work
		assert f(flip(f(c2)))(2,5,7) == 5
	}
}
