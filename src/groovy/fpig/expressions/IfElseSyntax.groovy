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
 * DSL syntax supporting more functional if-elseif-elseif-else chains implemented as curried function composition.
 * <p>
 * {@code _else_} is a required part of the chain.
 * <p>
 * Example:
 * <pre>
 * import static fpig.expressions.IfElseSyntax.*
 * 
 * def score = 75.2
 * def grade =   _if_{ score > 89 } >> _then_ {'A'} >>
 *           _elseif_{ score > 79 } >> _then_ {'B'} >>
 *           _elseif_{ score > 69 } >> _then_ {'C'} >>
 *             _else_{ 'F' }
 *                                       
 * assert grade == 'C'
 * </pre>
 * Using {@code _ifun_} makes it even more functional
 * <pre>
 * def grade  = _ifun_(LARGER(89)) >> _then_ {'A'}  >>
 *            _elseif_(LARGER(79)) >> _then_ {'B'}  >>
 *            _elseif_(LARGER(69)) >> _then_ {'C'}  >>
 *             _else_ { 'F' }
 *                                       
 * assert grade(75.2) == 'C'
 * </pre>
 * @author Robert Peszek
 */
class IfElseSyntax {

	static Closure _if_ = {Closure c ->
		def statement = new IfElseSyntax()
		statement.conditionSatisfied = c()
		statement
	}

	static Closure _ifunC = {Closure c, arg ->
		def statement = new IfElseSyntax()
		statement.conditionSatisfied = c(arg)
		statement.hasArg = true
		statement.arg = arg
		//return asF(statement)
		statement
	}

	static Closure _elseifC = {Closure c, IfElseSyntax statement ->
		if(!statement.conditionSatisfied) {
			statement.conditionSatisfied = statement.hasArg ? c(statement.arg) : c()
		}
		//return asF(statement)
		statement
	}

	static Closure _thenC = {Closure c, IfElseSyntax statement ->
		if(statement.conditionSatisfied && !statement.hasResult) {
			statement.result = statement.hasArg ? c(statement.arg) : c()
			statement.hasResult = true
		}
		//return asF(statement)
		statement
	}

	static Closure _elseC = {Closure c, IfElseSyntax statement ->
		if(statement.conditionSatisfied) {
			return statement.result
		} else {
			return statement.hasArg ? c(statement.arg) : c()
		}
	}

	static _ifun_ = CallUtil.toFunction(_ifunC)
	static _then_ = CallUtil.toFunction(_thenC)
	static _elseif_ = CallUtil.toFunction(_elseifC)
	static _else_ = CallUtil.toFunction(_elseC)

	//			def rightShift(next) {
	//				  if(next instanceof Closure) {
	//							   next(this)
	//						}
	//			}

	/**
	 * This allows for >> syntax to work
	 * @param c
	 * @return
	 */
	def rightShift(Closure c) {
		c(this)
	}

	//privates
	boolean conditionSatisfied = false
	boolean hasArg = false
	def arg
	def hasResult = false
	def result


}
