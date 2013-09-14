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
package fpig.io.asfunlist.functions

import fpig.concepts.FunctorDescription;
import fpig.funlist.types.FunList;

/**
 * Provides functorial mapping from functional list library to InputStream.
 * This allows to treat InputStream as a functional {@code FunList}.  InputStream is tokenized using explicit token or line breaks.
 * <p>
 * As a type mapping this Functor can be viewed as mapping between FunList&lt;String&gt; type and InputStream type.
 * <p>
 * More elaborate tokenization can be defined by using {@link fpig.io.asfunlist.functions.InAndOutOfFunLists} functions directly without using this functor.
 * <p>
 * Example code:
 * <pre>
 * String s = "line 1\nline 2\nline 3"
 * InputStream input = new ByteArrayInputStream(s.getBytes())
 *
 * def functor = FunListToTokenizedInputStreamFunctor.forLineTokenization()
 * InputStream newIs = functor.fmap(reverse) << input
 *
 * def myReader = new InputStreamReader(newIs)
 * assert 'line 3' == myReader.readLine()
 * assert 'line 2' == myReader.readLine()
 * assert 'line 1' == myReader.readLine()
 * </pre>
 * 
 * <p> Wiki:
 * <br> <a href="http://code.google.com/p/fpiglet/wiki/FunctorPolymorphism">http://code.google.com/p/fpiglet/wiki/FunctorPolymorphism</a>
 * @see fpig.concepts.FunctorDescription
 * @author Robert Peszek
 *
 */
class FunListToTokenizedInputStreamFunctor extends FunctorDescription<FunList<String>, InputStream>{
	
	static FunctorDescription forToken(token) {
		return new FunListToTokenizedInputStreamFunctor(token: token)
	}
	static FunctorDescription forLineTokenization() {
		return forToken('\n')
	}

    String token
	 
	Closure getFmap() {
		
	    fpig.io.asfunlist.functions.InAndOutOfFunLists.withFunListUsingToken(token)
	}
	
}
