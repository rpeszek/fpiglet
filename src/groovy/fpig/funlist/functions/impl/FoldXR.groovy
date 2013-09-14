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
 *  See {@link fpig.funlist.functions.BaseFL} for information on foldXR, reduceXR functions
 * 
 * @author Robert Peszek
 */
class FoldXR {

	/*
	* For infinite lists
	* The trick is that the accumulator needs to be a closure.
	*/
	static Closure getFoldXRC() {
		return  {Closure foldF, acc, FunList l->
			if(l.empty)
			   acc
			else {
			   foldF(l.head, {FoldXR.foldXRC(foldF, acc, l.tail)}) //Haskell knows that foldF does not depend on second parameter, Groovy does not
			}
			   
		}
	}
	/*
	 * Version of fold which for finite lists
	* foldXR :: (b -> (_->a) -> a) -> a -> [b] -> a
    */
	static Closure foldXR = CallUtil.toFunction foldXRC

	/*
	 * version that works on infinity
	 */
	static Closure reduceXR = CallUtil.toFunction {Closure foldF, FunList l->
		if(l.empty)
		    null //TODO change this to maybe
		else {
			def tail = l.tail
			if(tail.empty){
				l.head
			} else {
			    foldF(l.head, {FoldXR.reduceXR(foldF, l.tail)})
			}
		}
	}

}
