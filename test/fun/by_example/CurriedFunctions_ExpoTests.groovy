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


/**
 * fun.curring.BaseCurriedFunctionTests contains tests worth looking at.
 * In particular testWhatCouldHappenOnDevelopementError is worth looking at.
 * 
 * @author Robert Peszek
 */
class CurriedFunctions_ExpoTests extends GroovyTestCase{

	void testAndShowCurriedFunctions() {
		Closure c1 = {a, b, c, d-> "${a},${b},${c},${d}"}
		Closure c2 = {a -> {b -> {c -> {d -> "${a},${b},${c},${d}"}}}}
		Closure c3 = {a, b -> {c, d -> "${a},${b},${c},${d}"}}
		
		//showcase what Groovy does
		shouldFail(MissingMethodException){ c1(1) }
		assert c1(1,2,3,4) == "1,2,3,4"
		
		shouldFail(MissingMethodException){ c2(1,2,3,4) }
		assert c2(1) instanceof Closure
		assert c2(1)(2)(3)(4) == "1,2,3,4"
		
		shouldFail(MissingMethodException){ c3(1,2,3,4) }
		shouldFail(MissingMethodException){ c3(1) }
		assert c3(1,2) instanceof Closure
		
		//showcase Groovy curring ugliness
		assert c1.curry(1).curry(2).curry(3).curry(4) instanceof Closure
		assert c1.curry(1).curry(2).curry(3).curry(4).call() == "1,2,3,4"
		
		//what can groovy do to simulate c2(1,2,3,4)? Nothing
		
		
		//test what Fpiglet does
		showcaseFpigletCurriedFunctions c1
		showcaseFpigletCurriedFunctions c2
		showcaseFpigletCurriedFunctions c3
	}
	
	void showcaseFpigletCurriedFunctions(Closure c){
		Closure fc = f c //same as CallUtil.toFunction(c)
		
		assert fc(1)(2,3,4) == "1,2,3,4"
		assert fc(1)(2,3)(4) == "1,2,3,4"
		assert fc(1,2)(3,4) == "1,2,3,4"
		assert fc(1,2)(3)(4) == "1,2,3,4"
		assert fc(1,2,3)(4) == "1,2,3,4"
		assert fc(1,2,3,4) == "1,2,3,4"
		assert fc(1)(2)(3,4) == "1,2,3,4"
		assert fc(1)(2)(3) instanceof Closure
		assert fc(1)(2)(3)(4) == "1,2,3,4"
	}

					
	void testParameter_Syntax() {
		Closure c = f({a,b,c,d -> a + 2*b + 3*c + 4*d})
		Closure a_c = c(_, 0, _, 0)
		assert a_c(0,1) == 3
		assert a_c(1,0) == 1
	}

	void testParameter_Syntax2() {
		Closure c = f({a -> {b -> {c -> {d -> a + 2*b + 3*c + 4*d}}}})
		Closure a_c = c(_, 0, _, 0)
		assert a_c(0,1) == 3
		assert a_c(1,0) == 1
	}

	void testParameter_Syntax3() {
		def square = POWER(_,2)
		assert square(4) == 16
		
		def squarePlus1 = PLUS(1) << POWER(_,2) //1+n^2
		assert squarePlus1(2) == 5
	}


}
