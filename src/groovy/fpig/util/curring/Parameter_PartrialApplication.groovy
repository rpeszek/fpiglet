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

import fpig.funlist.types.FunList;

/**
 * Provides resolution for calls like c(_, _, 3).
 * <p>
 * Used as part of CallUtil Curried Function logic (internally by Fpiglet) but
 * can be used directly by calling 
 * <pre>
 * Closure c = Parameter_PartrialApplication.toFunction {your closure definition}
 * </pre>
 * to enable partial application using _ syntax (Defined as {@code static FpigBase._} or  {@code CallUtil._}).
 * <p>
 * The approach taken here is not using {@code Closure.ncurry()} calls, which would have to be chained
 * a lot to accomplish the goal. Instead, a map is used to remember position of parameters and a simple
 * wrapping closure takes care of re-mapping the arguments.
 * <p> Wiki:
 * <br> <a href="http://code.google.com/p/fpiglet/wiki/CurriedFunctions">http://code.google.com/p/fpiglet/wiki/CurriedFunctions</a>
 * @author peszek
 *
 */
class Parameter_PartrialApplication {

	def callResolving_(Closure c, Object[] args) {
		args = args ?: []
		if(args.find {it instanceof Parameter_}){
			def argsMap = toArgumentMap(args)
			return {Object[] a ->
				def newargs = replace(argsMap, a)
				callResolving_(c, newargs)
			}
		} else {
			return c(*args)
		}
	}

	Map<Integer, Object> toArgumentMap(Object[] args) {
		Map<Integer, Object> res = [:]
		args.eachWithIndex { arg, i ->
			res[i] = arg
		}
		res
	}

	Object[] replace(Map<Integer, Object> argMap, newArgs){
		//terrible imperative code alert!
		int newArgsSize = newArgs.length
		int newArgsIndx = -1
		def res = (0..(argMap.size()-1)).collect { i ->
			if(argMap[i] instanceof Parameter_ && newArgsIndx  < newArgsSize - 1){
				newArgsIndx = newArgsIndx + 1
				newArgs[newArgsIndx]
			} else {
				argMap[i]
			}
		}
		res
	}

	Closure toFunction(Closure c) {
		return {Object[] args ->
			callResolving_(c, args)
		}
	}

}
