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
package fpig.expressions

import fpig.util.CallUtil;

/**
 * Scope localization DSL syntax.
 * <p>
 * Demonstrates power of function composition to achieve scope localization.
 * This allows for syntax like this:
 * <pre>
 * import static fpig.expressions.EvalWhereSyntax.*
 * 
 * eval {expression of x, y, z} << where {x = .. ; y = .., z = ...}
 * assert 5 ==  eval { sum.call(2,3) } << where {sum = {a, b-> a+b}}
 * </pre>
 * Please see Fpiglet test cases for some examples.
 * <p> Wiki:
 * <br> <a href="http://code.google.com/p/fpiglet/wiki/Expressions">http://code.google.com/p/fpiglet/wiki/Expressions</a>
 * @author Robert Peszek
 */
class EvalWhereSyntax {
	static Closure where = { Closure closure ->
		EvalWhereSyntax le  = new EvalWhereSyntax()
		closure.resolveStrategy = Closure.DELEGATE_FIRST
		closure.delegate = le
		closure()
		le
	}

	static Closure evalC = {Closure c, EvalWhereSyntax s ->
		c.resolveStrategy = Closure.DELEGATE_FIRST
		c.delegate = s
		c()
	}
	static Closure eval = CallUtil.toFunction(evalC)

	//privates
	def storage = [:]
	def propertyMissing(String name, value) { storage[name] = value }
	def propertyMissing(String name) { storage[name] }
	def methodMissing(String name, args) { //invoked where where defines closures
		def value = storage[name]
		if(value instanceof Closure) {
			value.call(*args)
		} else {
			throw new MissingMethodException(name, delegate, args)
		}
	}

}
