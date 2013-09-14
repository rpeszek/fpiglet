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

package fpig.groovylist.asfunlist.functions

import fpig.funlist.functions.AsFunList;
import fpig.funlist.functions.BaseFL
import fpig.util.CallUtil
import groovy.lang.Closure;

/**
 * Functional List library {@link fpig.funlist.functions.BaseFL} adopted to Groovy/Java Lists.
 * This class uses {@link fpig.groovylist.asfunlist.functions.FunListToListFunctor} to map over Fpilget functional list library to Groovy List.
 * <p>
 * Example:
 * <pre>
 *  import static fpig.groovylist.asfunlist.functions.GroovyListAsFunList.*
 * 
 *  assert [5,10,50] == take(3) << filter{it % 5==0} << map {1 + it * it} << (1..10000)
 * </pre>
 * Note that this class goes a bit against the idea of decoupling OO code from functional code (Groovy lists are OO)
 * <br>
 * (see <a href="http://code.google.com/p/fpiglet/wiki/FunPiggyStyle">http://code.google.com/p/fpiglet/wiki/FunPiggyStyle</a>).
 * <p>
 * Also note, that the above code is not very performant. It is equivalent to this:
 * <pre>
 *  def fmap = FunListToListFunctor.statics.fmap
 * 
 *  assert [5,10,50] == fmap(take(3)) << fmap(filter{it % 5==0}) << fmap(map {1 + it * it}) << (1..10000)
 * </pre>
 * which will run faster if we write it like this:
 * <pre>
 *  assert [5,10,50] == fmap( take(3) << filter{it % 5==0} << map {1 + it * it} ) << (1..10000)
 * </pre>
 * or
 * <pre>
 *  import static fpig.groovylist.asfunlist.functions.InAndOutOfFunLists.withFunList
 *  assert [5,10,50] == withFunList( take(3) << filter{it % 5==0} << map {1 + it * it} ) << (1..10000)
 * </pre>
 * However, functions provided in this class are still very convenient in cases where the code is not a long chain of composed functions or the list is not very big.
 * 
 * @author Robert Peszek
 */
class GroovyListAsFunList {
  // NOTE: needs methods which work well will static imports, messing with metaclass would be simpler but it would
  // not accomplish the same thing.

	static AsFunList delegate = fpig.funlist.functions.AsFunList.mappedWith(FunListToListFunctor.statics.fmap)

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
