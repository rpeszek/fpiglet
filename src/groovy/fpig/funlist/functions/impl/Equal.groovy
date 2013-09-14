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

/**
 * Internal Fpiglet use.
 * <p>
 *  See {@link fpig.funlist.functions.BaseFL} for information on eq functions
 * 
 * @author Robert Peszek
 */
class Equal {
	static Closure getEqCT() {
		return  {FunList l1, FunList l2->
			if(l1.empty)
			    l2.empty
		    else if(l2.empty){
				false
			}
		    else {
				def res = l1.head == l2.head
				if(!res) {
					false
				} else {
			    	Equal.eqCT.trampoline(l1.tail, l2.tail) 
				}
		    }
		}
	}

	static Closure eq = CallUtil.toFunction {FunList li1, FunList li2 -> Equal.eqCT.trampoline()(li1,li2)} 
	

}
