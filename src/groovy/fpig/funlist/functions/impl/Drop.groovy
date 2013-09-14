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
import groovy.lang.Closure;

/**
 * Internal Fpiglet use.
 * <p>
 *  See {@link fpig.funlist.functions.BaseFL} for information on drop/dropWhile functions
 * 
 * @author Robert Peszek
 */
class Drop {

	static Closure getDropWhileCT() {
		return {Closure predicate,  FunList l ->
			if(l.empty)
				l
			else if (predicate(l.head))
				Drop.dropWhileCT.trampoline(predicate, l.getTail()) 
			else
				l 
		}
	}
	
	//needs re-wrapping in a closure because of trampoline use and of how callUtility works
	static Closure dropWhile = CallUtil.toFunction {Closure predicate,  FunList l -> Drop.dropWhileCT.trampoline()(predicate, l)}

	static Closure getDropCT() {
		return {n,  FunList l ->
			if(l.empty)
				l
			else if(n==0) 
				l
			else
				Drop.dropCT.trampoline(n-1, l.getTail())
		}
	}
	
	static Closure drop = CallUtil.toFunction {n,  FunList l -> Drop.dropCT.trampoline()(n, l)}


}
