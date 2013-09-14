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

import fpig.funlist.functions.BaseFL
import fpig.funlist.functions.AsFunList;
import fpig.util.CallUtil

/**
 * Functional List library {@link fpig.funlist.functions.BaseFL} adopted to InputStreams viewed as lazily tokenized lists.
 * This class uses {@link fpig.io.asfunlist.functions.FunListToTokenizedInputStreamFunctor} to map over Fpilget functional list library to InputStreams.
 * <p>
 * Example:
 * <pre>
 * String s = "line 1\nline 2\nline 3"
 * def fio = TokenizedInputStreamAsFunList.withLines()
 * InputStream newIs = fio.reverse << new ByteArrayInputStream(s.getBytes())
 *
 * def myReader = new InputStreamReader(newIs)
 * assert 'line 3' == myReader.readLine()
 * assert 'line 2' == myReader.readLine()
 * assert 'line 1' == myReader.readLine()
 * </pre>
 * <p>
 * Please see {@link fpig.groovylist.asfunlist.functions.GroovyListAsFunList} for discussion of performance issues. Consider using 
 * {@link fpig.io.asfunlist.functions.FunListToTokenizedInputStreamFunctor} directly.
 * 
 * @author Robert Peszek
 */
class TokenizedInputStreamAsFunList {
	static AsFunList withToken(token){
		def fmap = FunListToTokenizedInputStreamFunctor.forToken(token).fmap		
		AsFunList.mappedWith fmap
	}
	
	static AsFunList withLines() {
		withToken('\n')
	}
	
	//TODO we may want to get better word tokenization
	static AsFunList withWords() {
		withToken(' ')
	}
		
}
