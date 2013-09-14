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

import fpig.concepts.FunctorDescription;
import fpig.funlist.types.FunList;

/**
 * Functor mapping between functional {@code FunList} and OO {@code List}.  This Functor allows Fpiglet to map over functional list
 * library to Groovy Lists.
 * <p>
 * As a type mapping this Functor can be viewed as mapping between FunList&lt;T&gt; type and List&lt;T&gt; type.
 * <p> Wiki:
 * <br> <a href="http://code.google.com/p/fpiglet/wiki/FunctorPolymorphism">http://code.google.com/p/fpiglet/wiki/FunctorPolymorphism</a>
 *   
 * @see fpig.concepts.FunctorDescription
 * @author peszek
 * @param Generic Type T
 */
class FunListToListFunctor<T> extends FunctorDescription<FunList<T>, List<T>>{
	
	static FunctorDescription getStatics() {
		return _statics
	}
 
 
	Closure getFmap() {
	   fpig.groovylist.asfunlist.functions.InAndOutOfFunLists.withFunList
	}
	
	//private
	static FunctorDescription _statics= new FunListToListFunctor();
}
