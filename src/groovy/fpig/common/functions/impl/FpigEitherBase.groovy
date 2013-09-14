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

import fpig.common.types.Either;
import fpig.common.types.Left;
import fpig.common.types.Right;
import fpig.util.CallUtil
import fpig.util.curring.Parameter_;
import groovy.lang.Closure;

/**
 * For internal Fpiglet use.
 * @author Robert Peszek
 */
class FpigEitherBase {
	

	
	
	/**
	 * For use by Fpiglet functions and FpigBase.
	 */
	static Closure right = {val -> new Right(_val:val)}
	
	/**
	 * For use by Fpiglet functions and FpigBase.
	 */
	static Closure left = {val -> new Left(_val:val)}
	
	/**
	 * For use by Fpiglet functions and FpigBase.
	 */
	static Closure isRight = {Either e-> 
		(e instanceof Right)
	}
	
	/**
	 * For use by Fpiglet functions and FpigBase.
	 */
	static Closure unright = {Either e->
		if(e instanceof Right){
			e.val
		} else {
		  throw new RuntimeException('Cannot unRight Left')
		}
	}
	
	/**
	 * For use by Fpiglet functions and FpigBase.
	 */
	static Closure unleft = {Either e->
		if(e instanceof Left){
			e.val
		} else {
		  throw new RuntimeException('Cannot unLeft Right')
		}
	}
}
