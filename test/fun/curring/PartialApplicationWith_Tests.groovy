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

import fpig.util.curring.*;

class PartialApplicationWith_Tests extends GroovyTestCase{
	Parameter_PartrialApplication wayOfFun = new Parameter_PartrialApplication()
	ExplicitCurring wayOfFunPrep = new ExplicitCurring()

	Closure f(Closure c) {
		def fc = wayOfFunPrep.toFunction(c)
		wayOfFun.toFunction(fc)
	}

	void test_Resolution() {
		def _ = new Parameter_()
		Closure c = f({a,b,c,d -> a + 2*b + 3*c + 4*d})
		Closure a_c = c(_, 0, _, 0)
		assert a_c(0,1) == 3
		assert a_c(1,0) == 1		
	}

	void test_ResolutionWithoutCurring() {
		def _ = new Parameter_()
		Closure c = wayOfFun.toFunction {a,b,c,d -> a + 2*b + 3*c + 4*d}
		Closure a_c = c(_, 0, _, 0)
		assert a_c(0,1) == 3
		assert a_c(1,0) == 1
	}
	
	void test_Resolution2() {
		def _ = new Parameter_()
		Closure c = f({a -> {b -> {c -> {d -> a + 2*b + 3*c + 4*d}}}})
		Closure a_c = c(_, 0, _, 0)
		assert a_c(0,1) == 3
		assert a_c(1,0) == 1
	}
}
