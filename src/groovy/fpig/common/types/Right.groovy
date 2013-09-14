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

/**
 * Right is one of the flavors of Either.
 * Right typically encapsulates a successful result of some operation.
 * <p>
 * This class/object should not be used directly. 
 * Programs should interact with this type using functions defined in
 * {@link fpig.common.functions.FpigBase}.  This class should not be used in declarations either. 
 * Instead, use Either if you want more typed code.
 * 
 * @see fpig.common.types.Either
 * @see fpig.common.types.Left
 * @author Robert Peszek
 */
@EqualsAndHashCode(includeFields=true)
class Right<L, R> extends Either<L, R> {
	R getVal() {
		this._val
	}
	//privates
	R _val
	
	String toString() {
		'Right(' + _val?.toString() + ')'
	}
}
