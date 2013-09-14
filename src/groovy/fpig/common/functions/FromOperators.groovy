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

import static fpig.util.CallUtil.toFunction as f;

/**
 * Defines curried functions based on common operators.
 * <p>
 * For example:
 * <pre>
 *   PLUS //is defined as f {a,b -> a+b}
 * </pre>
 * @author Robert Peszek
 */
class FromOperators {
	
	//math ops
	static Closure PLUS = f {a,b -> a+b}
	static Closure MINUS = f {a,b -> a-b}
	static Closure TIMES = f {a,b -> a*b}
	static Closure POWER = f {a,b -> a**b}
	static Closure DIVIDED = f {a,b -> a/b}
	static Closure MODULO = f {a,b -> a%b}
	static Closure MAX = f {a,b-> (a>b)? a: b}
	static Closure MIN = f {a,b-> (a<b)? a: b}
	static Closure LARGER = f {a,b-> (a<b)}
	static Closure SMALLER = f {a,b-> (a>b)}
	static Closure EQUALS = f {a,b-> (a==b)}
	static Closure LEFT_SHIFT = f {a,b -> a << b}
	static Closure COMPOSE = LEFT_SHIFT
	
	
}
