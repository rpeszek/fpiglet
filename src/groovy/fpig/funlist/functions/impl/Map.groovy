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
package fpig.funlist.functions.impl

import fpig.util.CallUtil
import fpig.funlist.types.FunList
import fpig.funlist.functions.impl.*

/**
 * Internal Fpiglet use.
 * <p>
 *  See {@link fpig.funlist.functions.BaseFL} for information on map function
 * 
 * @author Robert Peszek
 */
class Map {
	static Closure getMapC() {
		return {Closure expr, FunList l ->
			l.empty ? l: l.build(expr(l.head), { Map.mapC(expr, l.getTail())})
		}
	}
	static Closure map = CallUtil.toFunction mapC


}
