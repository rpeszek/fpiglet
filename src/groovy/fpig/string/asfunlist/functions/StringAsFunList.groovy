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
import groovy.lang.Closure;

/**
 * Functional List library {@link fpig.funlist.functions.BaseFL} adopted to Strings viewed as lists of chars.
 * This class uses {@link fpig.string.asfunlist.functions.FunListToStringFunctor} to map over Fpilget functional list library to Strings.
 * <p>
 * Example:
 * <pre>
 * import static fpig.string.asfunlist.functions.StringAsFunList.*
 * 
 * assert ('c' as char) == reduceL(MAX) << 'abc'
 * </pre>
 * <p>
 * Please see {@link fpig.groovylist.asfunlist.functions.GroovyListAsFunList} for discussion of performance issues. Consider using 
 * {@link fpig.string.asfunlist.functions.FunListToStringFunctor} directly.
 * 
 * @author Robert Peszek
 */
class StringAsFunList {
	static AsFunList delegate = AsFunList.mappedWith(FunListToStringFunctor.statics.fmap)

	static Closure getIsEmpty() {
		delegate.isEmpty
	}

	static Closure getHead() {
		delegate.head
	}
	
	static Closure getTail() {
		delegate.tail
	}

	static Closure getLength() {
		delegate.length
	}

	static Closure getFoldL() {
		delegate.foldL
	}
	
	static Closure getFoldLUntil() {
		delegate.foldLUntil
	}

	static Closure getReduceL(){
		delegate.reduceL
	}

	static Closure getFoldR() {
		delegate.foldR
	}
	
	static Closure getReduceR() {
		 delegate.reduceR
	}
	
	static Closure getFilter(){
		delegate.filter
	}

	static Closure getMap(){
		delegate.map
	}
	
	static Closure getTake() {
		delegate.take
	}

	static Closure getTakeWhile() {
		delegate.takeWhile
	}

	static Closure getDrop() {
		delegate.drop
	}
	
	static Closure getDropWhile() {
		delegate.dropWhile
	}
	
	static Closure getReverse() {
		delegate.reverse
	}
	
}
