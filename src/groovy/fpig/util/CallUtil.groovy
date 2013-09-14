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
package fpig.util

import fpig.util.curring.AutoCurring;
import fpig.util.curring.ExplicitCurring;
import fpig.util.curring.Parameter_;
import fpig.util.curring.Parameter_PartrialApplication;

/**
 * Provides curried function transformation and some convenience functions related to function composition and curring.
 * <p> Client programs may prefer to use shortcuts provided by {@link fpig.common.functions.FpigBase} instead.
 * <p> Wiki:
 * <br> <a href="http://code.google.com/p/fpiglet/wiki/CurriedFunctions">http://code.google.com/p/fpiglet/wiki/CurriedFunctions</a>
 * @author Robert Peszek
 */
class CallUtil {
	/**
	 * PRIVATE
	 */
	static def wayOfFun = new AutoCurring()
	/**
	 * PRIVATE
	 */
	static Parameter_PartrialApplication _syntax = new Parameter_PartrialApplication()

	/**
	 * @see fpig.common.functions.FpigBase#_
	 */
	static Parameter_ _ = new Parameter_()

	/**
	 * @see fpig.common.functions.FpigBase#f(Closure)
	 */
	static Closure toFunction(Closure c) {
		Closure cx = wayOfFun.toFunction c
		_syntax.toFunction(cx)
	}

	/**
	 * Does it look like this closure was converted to curried function already?
	 * This checks if closure has single parameter of Object[] type.
	 */
	static boolean isCurriedFunctionSuspect(Closure c){
		(c.maximumNumberOfParameters ==1 && c.parameterTypes[0] == Object[])		
	}
	/**
	 * Flipped version of haskell's $
	 * @see fpig.common.functions.FpigBase#apply
	 */
	static Closure functionApplicationFlipped = {arg, Closure c -> c(arg) }

	/**
	 * @see fpig.common.functions.FpigBase#flip
	 */
	static Closure flip = toFunction {Closure c-> { x,y -> c(y,x) }  }
	
	/**
	 * Convenience method accepting two closures f and f and returning
	 * {@code {x, y -> g(x, f(y))} }
	 */
    static Closure composeSecond = {Closure g, Closure f -> {x,y -> g(x, f(y))}}	
}
