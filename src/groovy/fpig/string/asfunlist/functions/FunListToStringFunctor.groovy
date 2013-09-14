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

import fpig.concepts.FunctorDescription;
import fpig.funlist.types.FunList;

/**
 * Provides functorial mapping from functional list library to String viewed as List of chars.
 * This allows to treat String as a functional {@code FunList}. 
 * <p>
 * As a type mapping this Functor can be viewed as mapping between FunList&lt;Character&gt; type and String type.
 * <p>
 * Example code:
 * <pre>
 * import static fpig.funlist.functions.BaseFL.foldL
 * def fmap = FunListToStringFunctor.statics.fmap
 * 
 * assert ('c' as char) == fmap( foldL(MAX, 0) ) << 'abc'
 * </pre>
 * 
 * @author Robert Peszek
 *
 */
class FunListToStringFunctor extends FunctorDescription<FunList<Character>, String>{
	
	static FunctorDescription getStatics() {
		return _statics
	}
 
 
	Closure getFmap() {
	   fpig.string.asfunlist.functions.InAndOutOfFunLists_forStrings.withFunList
	}
	
	//private
	static FunctorDescription _statics= new FunListToStringFunctor();
}
