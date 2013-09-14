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
package fpig.common.types

import groovy.transform.EqualsAndHashCode;
import groovy.transform.Immutable;

/**
 * Just is one of two flavors of Maybe.  Represents 'something'.
 * <p>
 * This class/object should not be used directly. 
 * Programs should interact with this type using functions defined in
 * {@link fpig.common.functions.FpigBase}. This class should not be used in declarations either. 
 * Instead, use Maybe if you want more typed code.
 * 
 * @see fpig.common.types.Maybe
 * @see fpig.common.types.Nothing
 * 
 * @author Robert Peszek
 */
//@Immutable  //for some reason this fails to compile with @Immutable
@EqualsAndHashCode(includeFields=true)
class Just<T> extends Maybe<T>{
  T getVal() {
	  this._val
  }
  //privates
   T _val 
   
  //OO convenience methods
  String toString() {
	  'Just(' + _val?.toString() + ')'
  }
}
