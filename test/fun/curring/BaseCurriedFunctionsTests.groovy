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
package fun.curring


import org.codehaus.groovy.runtime.CurriedClosure;

abstract class BaseCurriedFunctionsTests extends GroovyTestCase{
	abstract Closure f(Closure c)

	void assertFcCalls(Closure fc){
		assert fc(1)(2,3,4) == 10
		assert fc(1)(2,3)(4) == 10
		assert fc(1,2)(3,4) == 10
		assert fc(1,2)(3)(4) == 10
		assert fc(1,2,3)(4) == 10
 		assert fc(1,2,3,4) == 10
		assert fc(1)(2)(3,4) == 10
		assert fc(1)(2)(3) instanceof Closure
		assert fc(1)(2)(3)(4) == 10
	}
	
	void testPartialApplication() {
		Closure c = {a, b, c, d-> a+b+c+d}
		Closure fc = f c //same as CallUtil.toFunction(c)

		shouldFail(MissingMethodException){ c(1) }
		assert c(1,2,3,4) == 10

		assertFcCalls(fc)
	}


	void testCallingCurriedFunction() {
		Closure c = {a -> {b -> {c -> {d -> a + b + c + d}}}} //equivalent of {a, b, c, d-> a+b+c+d}
		Closure fc = f c //same as CallUtil.toFunction(c)


		assertFcCalls(fc)
	}

	void testCallingCurriedFunction2() {
		Closure c = {a -> {b, c, d -> a + b + c + d}} //equivalent of {a, b, c, d-> a+b+c+d}
		Closure fc = f c //same as CallUtil.toFunction(c)

		assert c(1)(2,3,4) == 10
		
		assertFcCalls(fc)
	}

	void testCallingCurriedFunction3() {
		Closure c = {a ,b -> {c, d -> a + b + c + d}} //equivalent of {a, b, c, d-> a+b+c+d}
		Closure fc = f c //same as CallUtil.toFunction(c)

		assert c(1,2)(3,4) == 10
		
		assertFcCalls(fc)
	}

	void testCallingCurriedFunction4() {
		Closure c = {a -> {b, c, d -> a + b + c + d}} //equivalent of {a, b, c, d-> a+b+c+d}
		Closure fc = f c //same as CallUtil.toFunction(c)

		assert c(1)(2,3,4) == 10
		
		assertFcCalls(fc)
	}

	void testWhatCouldHappenOnDevelopementError() {
		int sideEffect = 0
		Closure c = {a -> sideEffect = 1; return {b -> a + b}} //equivalent of {a, b-> a+b}
		Closure fc = f c //same as CallUtil.toFunction(c)

		shouldFail(RuntimeException){
			fc(1,2,3) //executing fc will assume that this Closure must have 3 arguments or is curried equivalent of having 3 arguments!
			//so the closure is partially executed before fpiglet can figure out that the arguments do not match!
		}
		assert sideEffect == 1
	}

	//current bahavior, but may change!
	void testOptionalParameters() {
		Closure c = {a , b=0-> {c, d=0 -> a + b + c + d}} //equivalent of {a, b, c, d-> a+b+c+d}
		Closure fc = f c //same as CallUtil.toFunction(c)

		assert fc(1) instanceof Closure
		assert fc(1,2) instanceof Closure
		assert fc(1,2,3) instanceof Closure
		assert fc(1,2,3,4) == 10
		assert fc(1)(2) instanceof Closure
		assert fc(1)(2)(3) instanceof Closure
		assert fc(1)(2)(3)(4) == 10
	}

	void testArbitraryParms() {
		Closure fc1 = f { Object[] args -> args.length}
		Closure fc2 = f {a , Object[] args -> [a, args.length]}

		assert fc1(1) == 1
		shouldFail(RuntimeException){ fc1(1,2)  }
		assert fc2(1) instanceof Closure
		assert fc2(1,2) == [1, 1]
		shouldFail(RuntimeException){
			fc2(1,2,3)
		}

	}

	void testToFunctionIndempotence() {
		Closure c = {a, b, c, d-> a+b+c+d}
		Closure fc = f c
		Closure ffc = f fc
		assert ffc(1) instanceof Closure
		assert ffc(1)(2) instanceof Closure
		assert ffc(1)(2)(3) instanceof Closure
		assert ffc(1)(2)(3)(4) == 10
	}

	void testToFunctionIndempotence2() {
		Closure c = {a -> {b -> {c -> {d -> a + b + c + d}}}} //equivalent of {a, b, c, d-> a+b+c+d}

		Closure fc = f c
		Closure ffc = f fc

		assert ffc(1) instanceof Closure
		assert ffc(1, 2) instanceof Closure
		assert ffc(1, 2, 3) instanceof Closure
		assert ffc(1,2,3,4) == 10
		assert ffc(1)(2)(3)(4) == 10
	}
	
	/*

g = (\x y-> x+ 2*y)
f = (\x -> 1)

h = g.f

main = print (h 5 2)

	 */
	void testCurriedComposition() {
		def myPlus = f {a,b -> a+b}
		def stringLength = f {s -> s.length() }
		
		def comp = myPlus << stringLength
		assert comp('abc')(2) == 5
		assert f (comp)('abc', 2) == 5 
	}


}
