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
 * See {@link fpig.funlist.functions.BaseFL} for information on take/takeWhile functions
 * 
 * @author Robert Peszek
 */
class Take {

	/*
	 * No need for trampoline, lazy recursion
	 */
	static Closure getTakeWhileC() {
		return {Closure predicate,  FunList l ->
			if(l.empty)
				l
			else if (predicate(l.head))
				l.build(l.head, { Take.takeWhileC(predicate, l.getTail()) }) //Groovy call dispatch needs it with class name!
		    else 
			    FunList.EMPTYLIST
		}
	}
	
	static Closure takeWhile = CallUtil.toFunction Take.takeWhileC

	/*
	 * No need for trampoline, lazy recursion
	 */
	static Closure getTakeC() {
		return {n,  FunList l ->
			if(l.empty)
				l
			else if(n==0) 
				FunList.EMPTYLIST
			else
				l.build(l.head, { Take.takeC(n-1, l.getTail()) }) //Groovy call dispatch needs it with class name!
		}
	}
	
	static Closure take = CallUtil.toFunction Take.takeC


}
