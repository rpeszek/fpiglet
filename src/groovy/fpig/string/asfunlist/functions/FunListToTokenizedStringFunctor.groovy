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

import fpig.concepts.FunctorDescription;
import fpig.funlist.types.FunList;

/**
 * Provides functorial mapping of functional list library to String viewed as List of tokenized Strings.
 * This allows to treat String as a functional {@code FunList}.  
 * <p>
 * As a type mapping this Functor can be viewed as mapping between FunList&lt;String&gt; type and String type.
 * <p>
 * Similar to {@link fpig.io.asfunlist.functions.FunListToTokenizedInputStreamFunctor} only String based, not InputStream based.
 * 
 * @author Robert Peszek
 *
 */
class FunListToTokenizedStringFunctor extends FunctorDescription<FunList<String>, String>{
	
	static FunctorDescription forToken(token) {
		return new FunListToTokenizedStringFunctor(token: token)
	}
 
    String token
	 
	Closure getFmap() {
	    fpig.string.asfunlist.functions.InAndOutOfFunLists_forTokenizedStrings.withFunList(token)
	}
	
}
