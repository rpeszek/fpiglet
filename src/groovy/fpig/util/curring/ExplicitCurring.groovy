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

import groovy.lang.Closure;

/**
 * Provides curried function implementation using explicit curring described below.
 * <p>
 * It is deprecated only because Fpiglet needs to settle on one approach. 
 * This approach is fully functional otherwise.
 * <p>
 * Calling {@code explicitCurring.toCurriedClosure(c)} returns a closure in <i>curried form</i>. (Which means, a closure with one parameter returning a closure with one parameter, etc..)
 * <p>
 * The  {@code callExplicitlyCurriedFunction} logic is simpler than auto-curring equivalent. The closures are transformed in a similar way 
 * as in {@link AutoCurring} class, with one difference that the closure is first transformed using {@code explicitCurring.toCurriedClosure(c)}.
 * This simplifies the implementation logic of how the curried functions are invoked because this logic needs to know only how to call closures with one parameter and
 * how to recurse on the results if returned result is a closure.  However, this version maybe somewhat less performant.
 * <p>
 * Otherwise this class shares the same limitation as {@link AutoCurring}.  Please review {@link AutoCurring} for details.
 * 
 * <p> Wiki:
 * <br> <a href="http://code.google.com/p/fpiglet/wiki/CurriedFunctions">http://code.google.com/p/fpiglet/wiki/CurriedFunctions</a>
 * @see fpig.util.curring.AutoCurring
 * @author Robert Peszek
 */
@Deprecated
class ExplicitCurring {
	/**
	 * This function treats all closures with 0 or 1 argument as curried.
	 * <p>
	 * For closures with more parameters this method will replace the closure with a new closure of just one parameter:
	 * this is logically equivalent to 
	 * <pre>
	 * 	switch(numOfClosureArgs ){
	 *	  case 2: return {a0 -> {a1 -> c(a0, a1) }}
	 *	  case 3: return {a0 -> {a1 -> {a2 -> c(a0, a1, a2)}}}
	 *	  case 4: return {a0 -> {a1 -> {a2 -> {a3 -> c(a0, a1, a2, a3)}}}}
	 *    ...
	 *   }
	 *   </pre>
	 * only it works with arbitrary amount of parameters.
	 *   
	 *  However,
	 *  toCurriedClosure( { a -> { c, d -> a+ b + c} ) will return the same closure (it is client responsibility to do nested closure conversion:
	 *  { a -> f { c, d -> a+ b + c} } when using this method.
	 * 
	 * @param c
	 * @return
	 */
	static Closure toCurriedClosure (Closure c) {
		int numOfClosureArgs = c.parameterTypes.size()
		if(numOfClosureArgs > 1) {
			return {a -> toCurriedClosure(c.curry(a))}
		} else {
			return c
		}
	}


	def callExplicitlyCurriedFunction(Closure c, Object[] args) {
		args = args ?: []

		int numOfClosureArgs = c.parameterTypes.size()
		if(numOfClosureArgs > 1){
			throw new CurriedFunctionCallException('Not a curried closure, too many args: ' + numOfClosureArgs)
		}
		if(args.size() > 1) {
			def arg = args.head()
			def remainingArgs = args.tail()
			def res = c(arg)
			if(res instanceof Closure) {
				if(res.maximumNumberOfParameters ==1 && res.parameterTypes[0] == Object[]){
					return callExplicitlyCurriedFunction(res, remainingArgs)
				} else {
				    return callExplicitlyCurriedFunction(toFunction(res), remainingArgs)
				}
			} else {
				throw new CurriedFunctionCallException("Closure argument missmatch, expected that after executing closure with partial set of ${numOfClosureArgs} parameters another closure is returned, but ${res} was returned");
			}
		} else if( args.size() == 1){
			def res = c(args[0])
			if(res instanceof Closure) {
				if(res.maximumNumberOfParameters ==1 && res.parameterTypes[0] == Object[]){
					return res //assuming already curried
				}
				return toFunction(res)
			} else {
				return res
			}
		} else {
			return c()
		}
	}

	/**
	 * This returns a wrapped version of curried closure. This version will allow both types of calling strategies:
	 * c(1,2,3,4) and c(1)(2)(3)(4)
	 * @param c
	 * @return
	 */
	Closure toFunction(Closure c) {
		Closure fc = toCurriedClosure(c)
		return {Object[] args ->
			callExplicitlyCurriedFunction(fc, args)
		}
	}
}
