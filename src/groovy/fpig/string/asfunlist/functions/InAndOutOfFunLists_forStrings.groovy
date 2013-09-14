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

package fpig.string.asfunlist.functions

import fpig.funlist.functions.BaseFL;
import fpig.funlist.types.FunList
import fpig.funlist.types.LazyList
import fpig.util.CallUtil

/**
* Functions for moving in and out between Strings and FunList (Lazy List). Strings are viewed as lists of chars.
*
* @author Robert Peszek
*/

class InAndOutOfFunLists_forStrings {
	
	/**
	 * private
	 */
    static Closure getFunlistInC(){
		{String fromS, int i->
			if(fromS.length() > i) {
				FunList.EMPTYLIST.build(fromS[i] as char, {InAndOutOfFunLists_forStrings.funlistInC(fromS, i+1)})
			} else {
			    FunList.EMPTYLIST
			}
        }
    }
	
	/**
	 * Transforms a String into a FunList containing String characters.
	 * @param String fromS
	 */
	static Closure funlistIn = {String fromS -> InAndOutOfFunLists_forStrings.funlistInC(fromS, 0)}

	/**
	 * private
	 * @param FunList flist
	 */
    static Closure funlistOutC = {FunList flist ->
        StringBuffer result = new StringBuffer();
		FunList current = flist; 
        while(!current.empty) {
            //very ugly imperative code, sorry!
			result.append(current.head);
            current = current.tail
        }
        result.toString()
    }
	
	/**
	 * Transforms FunList back into a String.
	 */
    static Closure funlistOut = CallUtil.toFunction funlistOutC
	
	/**
	 * Accepts String, but executes closure logic in the context of FunList.
     * Example code:
     * <pre>
     * import static fpig.funlist.functions.Base.foldL
     * 
     * assert ('c' as char) == withFunlist( foldL(MAX, 0) ) << 'abc'
     * </pre>
     * @param Closure f
     * @param string 
	 */
	static Closure withFunList = CallUtil.toFunction {Closure f, String s ->
		def output = f << InAndOutOfFunLists_forStrings.funlistIn << s
		if(output instanceof FunList)
		   InAndOutOfFunLists_forStrings.funlistOut output
		else 
		   output
	}
}
