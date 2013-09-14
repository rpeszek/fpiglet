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

import fpig.funlist.functions.BaseFL
import fpig.funlist.functions.AsFunList;
import fpig.util.CallUtil

/**
 * Functional List library {@link fpig.funlist.functions.BaseFL} adopted to Strings viewed as lists of tokenized Strings.
 * Similar to {@link fpig.io.asfunlist.functions.TokenizedInputStreamAsFunList} only String based, not InputStream based.
 * <p>
 * Example:
 * <pre>
 * 
 * def sF = TokenizedStringAsFunList.withToken(' ') 	
 * assert 'word3 work2 word1' == sF.reverse << 'word1 work2 word3'
 * </pre>
 * Please see {@link fpig.groovylist.asfunlist.functions.GroovyListAsFunList} for discussion of performance issues. Consider using 
 * {@link fpig.string.asfunlist.functions.FunListToStringFunctor} directly.
 * 
 * @author Robert Peszek
 */
class TokenizedStringAsFunList {
	static AsFunList withToken(token){
		def fmap = FunListToTokenizedStringFunctor.forToken(token).fmap		
		AsFunList.mappedWith fmap
	}
		
}
