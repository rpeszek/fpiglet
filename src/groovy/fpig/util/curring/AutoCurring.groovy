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
package fpig.util.curring

/**
 * Provides curried function implementation using auto-curring described below.
 * <p>
 * Calling {@code autoCurring.toFunction(c)} with a closure c will return an equivalent of {@code &callWithAutoCurrying.curry(c)}
 * or, to be more explicit,
 * <pre>  
 * {Object[] args -> callAutoCurriedFunction(c, args)}
 * </pre>
 * where {@code callAutoCurriedFunction} is recursive and matches arguments on the closure and 
 * either partially applies them if closure has more arguments than 
 * the arguments passed or invokes it with  {@code maximumNumberOfParameters} accepted by the closure and 
 * applies the rest of the arguments recursively on the resulting closures.
 * <p>
 * This implementation assumes that any closure defined as 
 * <pre>  
 * {Object{] args ->..} 
 * </pre>
 * has been already converted to curried function.
 * So it is the client responsibility to convert such closures explicitly if needed.
 * <p>
 * There is a little glitch:  if a wrong number of args is passed, the closure can partially execute and then fail!
 * See {@code fun.curring.BaseCurriedFunctionsTests.testWhatCouldHappenOnDevelopementError} test case in Fpiglet source for an example.
 * <p> Wiki:
 * <br> <a href="http://code.google.com/p/fpiglet/wiki/CurriedFunctions">http://code.google.com/p/fpiglet/wiki/CurriedFunctions</a>
 * @author Robert Peszek
 */
class AutoCurring {

	def callAutoCurriedFunction(Closure c, Object[] args) {
		args = args ?: []

		int numOfClosureArgs = c.maximumNumberOfParameters
		if(args.size() < numOfClosureArgs) {
			Closure res = args.inject(c) {acc, arg -> acc.curry(arg)}
			if(res.maximumNumberOfParameters ==1 && res.parameterTypes[0] == Object[])
				return res
			return toFunction(res)
		} else if(args.size() > numOfClosureArgs) {
			def newArgs = args.take(numOfClosureArgs)
			def remainingArgs = args.drop(numOfClosureArgs)
			def res = c(*newArgs) //note c.call(args) will not work!
			if(res instanceof Closure) {
				return callAutoCurriedFunction(res, remainingArgs)
			} else {
				throw new CurriedFunctionCallException("Closure argument missmatch, expected that after executing closure with partial set of ${numOfClosureArgs} parameters another closure is returned, but ${res} was returned");
			}
		} else {
			def res = c(*args) //note c.call(args) will not work!
			if(res instanceof Closure) {
				if(res.maximumNumberOfParameters ==1 && res.parameterTypes[0] == Object[]){
					return res //assuming already curried
				}
				return toFunction(res)  //TODO would be good to verify that it is not a function yet
			} else {
				return res
			}
		}
	}

	/**
	 * 
	 * 
	 * Equivalent of &callWithAutoCurrying.curry(c)
	 * @param c
	 * @return
	 */
	Closure toFunction(Closure c) {
		return {Object[] args ->
			callAutoCurriedFunction(c, args)
		}
	}

}
