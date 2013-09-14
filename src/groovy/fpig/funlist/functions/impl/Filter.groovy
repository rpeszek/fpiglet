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
 * See {@link fpig.funlist.functions.BaseFL} for information on filer function
 * 
 * @author Robert Peszek
 */
class Filter {

	//trampoline makes the code hard to read, remove it and the code will work only for short lists where predicate fails sequentially in a row.
	static Closure getFilterCT() {
		return {Closure predicate,  FunList l ->
			if(l.empty)
				l
			else if (predicate(l.head))
				l.build(l.head, { Filter.filterCT.trampoline()(predicate, l.getTail()) }) //Groovy call dispatch needs it with class name!
				//needs to trampoline() to resolve actual tail since it uses trampoline when predicate fails
			else
				Filter.filterCT.trampoline(predicate, l.getTail()) //Groovy call dispatch needs it with class name!
				//needs to use trampoline in case list where initial list where predicate fails is long
		}
	}
	
	//needs re-wrapping in a closure because of trampoline use and of how callUtility works
	static Closure filter = CallUtil.toFunction {Closure predicate,  FunList l -> Filter.filterCT.trampoline()(predicate, l)}


}
