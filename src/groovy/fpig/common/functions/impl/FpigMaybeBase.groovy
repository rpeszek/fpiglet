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
package fpig.common.functions.impl

import fpig.common.types.Just;
import fpig.common.types.Maybe;
import fpig.common.types.Nothing;
import fpig.util.CallUtil
import fpig.util.curring.Parameter_;
import groovy.lang.Closure;

/**
 * For internal Fpiglet use.
 * @author Robert Peszek
 */
class FpigMaybeBase {
	


	/**
	 * For use by Fpiglet functions and FpigBase.
	 */
	static Closure just = {val -> new Just(_val:val)}	
	
	/**
	 * For use by Fpiglet functions and FpigBase.
	 */
	static Closure nothing = {Object[] args -> Nothing.instance}
	
	/**
	 * For use by Fpiglet functions and FpigBase.
	 */
	static Closure unjust = {Maybe m->
		if(m instanceof Just){
			m.val
		} else {
		    throw new RuntimeException('Cannot unjust Nothing')
		}
	}
	
	/**
	 * For use by Fpiglet functions and FpigBase.
	 */
	static Closure isSomething = {Maybe m-> 
		(m instanceof Just) 
	}
	
}
