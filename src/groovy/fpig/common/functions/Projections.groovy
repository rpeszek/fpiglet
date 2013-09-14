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
package fpig.common.functions

import fpig.util.CallUtil;


/**
 * Defines a list of convenient curried functions returning subset of arguments.
 *
 * @author Robert Peszek
 */
class Projections {
	
	/**
	 * Function FIRST has 2 arguments and returns the first one.
	 * @param a
	 * @param b 
	 * @return a
	 */
	static Closure FIRST = CallUtil.toFunction {a, b -> a}
	
	/**
	 * Function SECOND has 2 arguments and returns the second one.
	 * @param a
	 * @param b
	 * @return b
	 */
	static Closure SECOND = CallUtil.toFunction {a, b -> b}
}
