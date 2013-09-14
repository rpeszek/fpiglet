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

import fpig.io.asfunlist.impl.FunListInputStream;
import fpig.funlist.functions.BaseFL;
import fpig.funlist.types.FunList
import fpig.funlist.types.LazyList
import fpig.util.CallUtil
import groovy.lang.Closure;

/**
* Functions for moving in and out between I/O InputStream and {@code FunList} (Lazy List)
* <p>
* NOTE: This implementation is lazy, records are not read from InputStream unless resulting {@code FunList} is being accessed.
* <p>
* The FunList/LazyList obtained from transforming an InputStream is persisting data on first retrieval so calling list.tail several times will not cause several calls to tokenizer.
* This built-in memoization makes the conversion meaningful since list operations have no way of 'skipping' a list element.
* <p>
* The FunList is created by {@code funlistIn} function which tokenizes data from the input stream.
* The FunList is converted back to InputStream by {@code funlistOut} function which 'untokenizes' the list elements.
* <p> 
* This class comes with default implementation of {@code universalTokenizer} and {@code universalUntokenizer} based on {@code char} delimiter.
* However, {@code funlistIn},  {@code funlistOut}, {@code withFunList}, methods accept tokenizer and untokenizer Closures as argument
* so any custom way to split InputStream into {@code FunList} can be easily plugged in. 
* This logic does not even need to assume text/char streams. 
* <p> 
* TODO: Current version does not handle custom encoding 
*  
* @see fpig.funlist.types.FunList
* @author Robert Peszek
*/

class InAndOutOfFunLists {
    
	/**
	 * Default implementation of tokenizer (tokenizer closure is used to split InputStream into elements needed to define {@code FunList}).
	 * This implementation splits based on character delimiter.
	 * <p>
	 * The job of untokenizer is to return next element in the list when called. The required signature is:
	 * <pre>
	 *   InputStream -> T //T is record type stored in FunList
	 * </pre>
	 * So universalTokenizer needs to be partially applied with a token to become a real tokenizer.
	 * @param delimiter
	 * @param InputStream
	 */
	static Closure universalTokenizer = CallUtil.toFunction {delimiter, InputStream is ->
    	//UGLY imperative code alert
		def buffer = new StringBuffer();
		int read = is.read()
		while(read !=-1 && read as char!=delimiter as char){
			buffer.append(read as char)
			read = is.read()
		}
		if(read==-1){
			if(buffer.size() == 0)
				return null
		} 
		buffer.toString()
	}

	/**
	 * Default implementation of untokenizer.  
	 * <p>
	 * The required signature is:
	 * <pre>
	 *   T -> InputStrem //T is record type stored in FunList
	 * </pre>
	 * So universalUntokenizer needs to be partially applied with a token to become a real untokenizer.
	 * <p>
	 * When InputStream encapsulating a {@code FunList} is read,
	 * the input stream implementation ({@link fpig.io.asfunlist.impl.FunListInputStream}) will invoke untokenizer as it needs to.
	 * The job of untokenizer is to return InputStream from which the {@code FunListInputStream} can read.
	 * <p>
	 * TODO needs handling of encoding
	 * @param delimiter
	 * @param listElement
	 * 
	 */
	static Closure universalUntokenizer = CallUtil.toFunction {delimiter, s->
		//s is list element being untokenized
		s = s + delimiter
		//new ReaderInputStream(new StringReader(s))
		new ByteArrayInputStream(s.getBytes()) //TODO handle encoding!
	}

	/**
	 * private
	 */
    static Closure getFunlistInC() {
		return {Closure tokenizer, data, inputStream->
			if(data!=null){
				//persisting closure, this is designed not to call tokenizer more than once
				//this poor man's memoization is more than about performance, it removes side-effects
				Closure tailC = {
					def last = myHead 
					if(myHead == null){
					   myHead = tokenizer(inputStream); 
    				   myTail = InAndOutOfFunLists.funlistInC(tokenizer, myHead, inputStream)
					}
					myTail
				}
				tailC.delegate = [:]
				FunList.EMPTYLIST.build(data, tailC)
			} else {
			    //inputStream.close() //have client close it, that looks to me like a side-effect
			    FunList.EMPTYLIST
			}
	    }
    }
	
	/**
	 * Transforms InputStream into a functional list {@code FunList} using tokenizer closure.
	 * The role of tokenizer is to return next list element when called.  
	 * Tokenizer is not expected to store any state (it will be called once for each record in correct order).
	 * <p>
	 * tokenizer closure is expected to have this logical signature:
	 * <pre>
	 *   tokenizer:: InputStream -> T //T is record type stored in FunList
	 * </pre>
	 */
	static Closure funlistIn = CallUtil.toFunction {Closure tokenizer, inputStream->
		def first = tokenizer(inputStream)
		InAndOutOfFunLists.funlistInC(tokenizer, first, inputStream)
	}
	
	/**
	 * private
	 */
    static Closure funlistOutC = {Closure untokenizer, FunList flist ->
		new FunListInputStream(untokenizer, flist); 
    }
	/**
	 * Transforms {@code FunList} back into InputStream.
	 * <p>
	 * Accepts untokenizer closure allowing for custom untokenizing.
	 * This method returns {@link fpig.io.asfunlist.impl.FunListInputStream}.  
	 * {@code FunListInputStream} will invoke untokenizer as needed.
	 * <p>
	 * 	untokenizer closure is expected to have this logical signature:
	 * <pre>
	 *   untokenizer:: T -> InputStream //T is record type stored in FunList
	 * </pre>

	 */
    static Closure funlistOut = CallUtil.toFunction funlistOutC
	
	/**
	 * General version of withFunList accepting custom tokenizer and untokenizer.
	 * <p>
	 * parameters:
	 * <pre>
	 *   tokenizer - tokenizer closure (see funlistIn for more info)
	 *   untokenizer - untokenizer closure (see funlistOut for more info)
	 *   f - closure to be executed f is expected to accept FunList as parameter and return FunList (or other final result which will not be converted, such as integer length).
	 *   InputStream s - input stream to be converted to FunList.
	 * </pre>
	 */
	static Closure withFunList = CallUtil.toFunction {Closure tokenizer, Closure untokenizer, Closure f, InputStream s ->
		def output = f << InAndOutOfFunLists.funlistIn(tokenizer) << s
		if(output instanceof FunList)
		   InAndOutOfFunLists.funlistOut(untokenizer) << output
		else 
		   output
	}
	
	/**
	 * Specialized version of withFunList using default tokenizer and untokenizer (based on character token).
	 * <p>
	 * Example:
	 * <pre>
	 * String s = "line 1\nline 2\nline 3"
	 * InputStream input = new ByteArrayInputStream(s.getBytes())
	 *
	 * InputStream newIs = withFunListUsingToken('\n' as char, reverse) << input
	 *
	 * def myReader = new InputStreamReader(newIs)
	 * assert 'line 3' == myReader.readLine()
	 * assert 'line 2' == myReader.readLine()
	 * assert 'line 1' == myReader.readLine()
	 * </pre>
	 */
	static Closure withFunListUsingToken = CallUtil.toFunction {token, Closure f, InputStream s  -> 
		Closure tokenizer  = InAndOutOfFunLists.universalTokenizer(token)
		Closure untokenizer = InAndOutOfFunLists.universalUntokenizer(token)
		InAndOutOfFunLists.withFunList(tokenizer, untokenizer, f, s)
	}
}
