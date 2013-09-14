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

package fpig.funlist.functions

import fpig.funlist.functions.BaseFL
import fpig.util.CallUtil

/**
 * Used internally by Fpiglet.
 * <p>
 * Simplifies how AsFunList classes are coded. 
 * For example, {@link fpig.string.asfunlist.functionsTokenizedStringAsFunList} uses this class to provide functional list library to tokenized strings.
 *
 * @author Robert Peszek
 */
class AsFunList {
	static  AsFunList mappedWith(Closure _fmap) {
		return new AsFunList(fmap: _fmap)
	} 
	
	Closure fmap
	
	Closure getIsEmpty() {
		fmap(BaseFL.isEmpty)
	}

	Closure getHead() {
		fmap(BaseFL.head)
	}

	Closure getTail() {
		fmap(BaseFL.tail)
	}
	
	Closure getLength() {
		fmap(BaseFL.length)
	}

	Closure getFoldL() {
		CallUtil.toFunction {Closure foldF, acc,  l-> fmap(BaseFL.foldL(foldF, acc)) << l }
	}
	
	Closure getFoldLUntil() {
		CallUtil.toFunction {Closure predicateF, Closure foldF, acc,  l-> fmap(BaseFL.foldLUntil(predicateF, foldF, acc)) << l }
	}

	Closure getReduceL(){
		CallUtil.toFunction {Closure foldF,  l-> fmap(BaseFL.reduceL(foldF)) << l	}	
	}

	Closure getFoldR() {
		CallUtil.toFunction {Closure foldF, acc,  l-> fmap(BaseFL.foldR(foldF, acc)) << l }
	}
	
	Closure getReduceR() {
		 CallUtil.toFunction {Closure foldF,  l-> fmap(BaseFL.reduceR(foldF)) << l}
	}
	
	Closure getFilter(){ 
		CallUtil.toFunction {Closure predicate,  l -> fmap(BaseFL.filter(predicate)) << l}
	}

	Closure getMap(){
		CallUtil.toFunction {Closure expr,  l -> fmap(BaseFL.map(expr)) << l}
	}
	
	Closure getTake() {
		 CallUtil.toFunction {n, l -> fmap(BaseFL.take(n)) << l}
	}

	Closure getTakeWhile() {
		 CallUtil.toFunction {predicate, l -> fmap(BaseFL.takeWhile(predicate)) << l}
	}

	Closure getDrop() {
		 CallUtil.toFunction {n, l -> fmap(BaseFL.drop(n)) << l}
	}
	
	Closure getDropWhile() {
		CallUtil.toFunction {predicate,  l -> fmap(BaseFL.dropWhile(predicate)) << l}
	}
	
	Closure getReverse() {
		 fmap(BaseFL.reverse)
	}

}
