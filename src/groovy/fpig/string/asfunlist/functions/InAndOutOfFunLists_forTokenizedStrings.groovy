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
* Functions for moving in and out between Strings (viewed as List of tokenized Strings) and FunList (Lazy List).
 * <p>
 * Similar to {@link fpig.io.asfunlist.functions.InAndOutOfFunLists} only String based, not InputStream based.
 * 
* @author Robert Peszek
*/

class InAndOutOfFunLists_forTokenizedStrings {
	/**
	 * Creates List from given String based on specified token.
	 * @param String token
	 * @param String fromS
	 */
    static Closure funlistIn = CallUtil.toFunction {String token, String fromS->
		List tString = fromS.tokenize(token)
		tString = tString.reverse()
		tString.inject(LazyList.EMPTYLIST){acc, el->
			BaseFL.prepend(el, acc)
		}
    }

	/**
	 * Private
	 */
    static Closure funlistOutC = {String token, FunList flist ->
        StringBuffer result = new StringBuffer();
		FunList current = flist; 
        while(!current.empty) {
            //very ugly imperative code, sorry!
			result.append(current.head);
			if(!current.tail.empty) {
				result.append(token)
			}
            current = current.tail
        }
        result.toString()
    }
	
 	/**
	 * Creates String from given FunList based on specified token.
	 * @param String token
	 * @param FunList flist
	 */
   static Closure funlistOut = CallUtil.toFunction funlistOutC
	
   /**
    * Accepts token, closure and String. Executes closure in the context of converted FunList.
    * <p>
    * Example:
    * <pre>
    * import static fpig.funlist.functions.BaseFL..foldR
    *   
    * assert 7 == withFunList(' ', foldR(f (MAX << {it.length()}), 0)) <<  '12 123 1234567' 
    * </pre>
    * @param String token
    * @param Closure containing FunList manipulation
    * @param String input String
    */
   static Closure withFunList = CallUtil.toFunction {String token, Closure f, String s ->
		def output = f << InAndOutOfFunLists_forTokenizedStrings.funlistIn(token) << s
		if(output instanceof FunList)
		   InAndOutOfFunLists_forTokenizedStrings.funlistOut(token) << output
		else 
		   output
   }
}
