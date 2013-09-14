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
package fpig.funlist.functions

import fpig.util.CallUtil
import fpig.funlist.types.FunList
import fpig.funlist.types.LazyList;
import fpig.funlist.functions.impl.*

/**
 * List functions for generating infinite functional lists.
 * <p> Wiki:
 * <br> <a href="http://code.google.com/p/fpiglet/wiki/ListFunctions">http://code.google.com/p/fpiglet/wiki/ListFunctions</a>
 * <br> <a href="http://code.google.com/p/fpiglet/wiki/LogicalSignatures">http://code.google.com/p/fpiglet/wiki/LogicalSignatures</a>
 * @author Robert Peszek
 */
class Infinity {
	
	/**
	 * Returns sequence starting at specified number.
	 * <p>
	 * Example
	 * <pre>
	 *   assert [5,6,7] << funlistOutTake(3) << streamFrom(5)
	 * </pre>
	 * @param Number n
	 * @return FunList
	 */
	static Closure getStreamFrom() {
		 {n -> FunList.EMPTYLIST.build(n, { Infinity.streamFrom(n+1) }) }
    }
	
	/**
	 * Same as streamFrom(1). Returns sequence of natural numbers starting with 1.
	 */
	static Closure naturalNumbers = { -> Infinity.streamFrom(1)}
	
	/**
	 * Repeats the same object forever.
	 * @param object to be repeated
	 * @return FunList
	 */
	static Closure getRepeat() {
		{a -> FunList.EMPTYLIST.build(a, { Infinity.repeat(a) }) }
    }
	
	/**
	 * private
	 */
	static Closure getIterateC() {
		{Closure f, a -> FunList.EMPTYLIST.build(a, { Infinity.iterate(f, f(a)) }) }
	} 
	
	/**
	 * Takes a closure {@code fn} and initial value {@code a} and applies this closure over and over:
	 * <pre>
	 *   a, fn(a), fn(fn(a)), fn(fn(fn(a))), ...
	 * </pre>
	 * Stack overflow safe.
	 * @param Generic Type A
	 * @return FunList of A
	 */
	static Closure iterate = CallUtil.toFunction Infinity.iterateC
}
