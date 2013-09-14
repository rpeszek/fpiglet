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
 *  See {@link fpig.funlist.functions.BaseFL} for information on foldL, reduceL functions
 * 
 * @author Robert Peszek
 */
class FoldL {
	/*
	 * foldLuntil :: (a->boolean) -> (a -> b -> a) -> a -> [b] -> a
	 * @return
	 */
	static Closure getFoldLUntilCT() {
		return  {Closure predicateF, Closure foldF, acc, FunList l->
			if(l.empty)
				acc
		    else if(predicateF(acc)){
				acc
			}
			else
				FoldL.foldLUntilCT.trampoline(predicateF, foldF, foldF(acc, l.head), l.tail) //Groovy call dispatch needs it with class name!
		}
	}


	//needs re-wrapping because of trampoline usage and how callUtil works
	static Closure foldL = CallUtil.toFunction {Closure foldF, acc, FunList l-> 
		Closure predicate = {false}
		FoldL.foldLUntilCT.trampoline()(predicate, foldF, acc, l)
	} 
	
	static Closure foldLUntil = CallUtil.toFunction {Closure predicateF, Closure foldF, acc, FunList l->
		FoldL.foldLUntilCT.trampoline()(predicateF, foldF, acc, l)
	}
	
	static Closure reduceL = CallUtil.toFunction {Closure foldF, FunList l-> 
		 if(l.empty)
		    null //TODO change this to maybe
	     else 
		    FoldL.foldL(foldF, l.head, l.tail) 			                                   
	}

}
