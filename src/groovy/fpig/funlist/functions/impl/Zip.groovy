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
 * See {@link fpig.funlist.functions.BaseFL} for information on zipWith function
 * 
 * @author Robert Peszek
 */
class Zip {
	/*
	 * zipWith :: (a -> b -> c) -> [a] -> [b] -> [c]
	 */
	static Closure getZipWithC() {
		return  {Closure c, listA, listB ->
			if(listA.empty)
			    FunList.EMPTYLIST
		    else if(listB.empty)
			    FunList.EMPTYLIST
		    else {
			    listA.build(c(listA.head, listB.head), { Zip.zipWithC(c, listA.tail, listB.tail) })
		    }
		}
	}
	//needs re-wrapping because of trampoline usage and how callUtil works
	static Closure zipWith = CallUtil.toFunction zipWithC
	

}
