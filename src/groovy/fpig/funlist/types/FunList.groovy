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
import fpig.funlist.functions.Infinity;

/**
* Should be viewed as internal class used by list functions only or for OO type of coding.
* Functional programs should not interact with this class directly (other than using it to declare functional list type).
* Rather, functional library {@link fpig.funlist.functions.BaseFL} or applicative or monadic libraries should be used.
* <p>
* FunList is an abstract base class allowing different implementations of functional lists.
* <p>
* Use of Generic typing is not perfect (informational only), but it is hard to do a decent job with Java generics.
* <p>
* Note: there is only one EMPTY list.
* <p>
* NOTE about implementation: public keywords added so the syntax  {@code FunList build(S val, Closure tailEval)} does not show as errors.  It is unclear
* why this is needed (Groovy 2.0 on Eclipse).
*
* @author Robert Peszek
*/
abstract class FunList<T> {
    private static final FunList _EMPTYLIST = new LazyList() //TODO where should that be defined?
    public static FunList getEMPTYLIST(Map props = null) {
        _EMPTYLIST
    }

    public abstract boolean isEmpty()
    public abstract T getHead()
    public abstract FunList<T> getTail()
	
	/**
	 * Generics info provided to convey purpose only.
	 * Closure tailEval needs to return T and &lt;S super T&gt;
	 * 
	 * In SCALA you would write [S &lt;: T].
	 */
    public abstract <S> FunList<S> build(S val, Closure tailEval)

	//convenience OO functions
    public static FunList<T> fromOoList(List<T> fromL, Map props = null) {
        InAndOutOfFunLists.funlistIn(fromL, props)
    }
    public static FunList<T> funStreamFrom(n) {
        Infinity.streamFrom(n)
    }
}
