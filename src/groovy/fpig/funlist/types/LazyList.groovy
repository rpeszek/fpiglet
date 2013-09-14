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

package fpig.funlist.types

import fpig.groovylist.asfunlist.functions.InAndOutOfFunLists;
import fpig.funlist.functions.EmptyListException;
import fpig.funlist.mixins.*

/**
* Fpiglet internal class.
* <p>
* Functional Lazy List Implementation. 
*
* @author Robert Peszek
*/
@Mixin([OoListOperationsPart, OoListInAndOutOfListsPart])
class LazyList<T> extends FunList<T>{


    public boolean isEmpty() { this._empty }
    public T getHead() {
        _head
    }
    public LazyList<T> getTail() {
		if(!tailEvaluator)
		  throw new EmptyListException("Cannot tail on empty list")
        def res = tailEvaluator.call()
        res as LazyList //Groovy has problem with mixin,  cast errors without 'as' 
    }
	
    public <S> LazyList<S> build(S val, Closure tailEval) { new LazyList<S>(val, tailEval) }

    //privates
    private T _head 
    private boolean _empty = false
    private Closure tailEvaluator
    private LazyList(T head, Closure tailEvaluator) { this._head=head; this.tailEvaluator=tailEvaluator }
    private LazyList() { this._empty = true }

	//OO support
	public String toString() {
		toStringImpl(this as FunList)
	}
}
