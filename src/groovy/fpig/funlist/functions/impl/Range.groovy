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
 * See {@link fpig.funlist.functions.BaseFL} for information on range function
 * 
 * @author Robert Peszek
 */
class Range {


	static Closure getRangeC() {
		return {n, m ->
			if(m < n)
				FunList.EMPTYLIST
			else
				FunList.EMPTYLIST.build(n, {Range.rangeC(++n, m) }) //Groovy call dispatch needs it with class name!
		}
	}
	
	static Closure range = CallUtil.toFunction Range.rangeC


}
