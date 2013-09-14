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


/**
 * Groovy Closures overload {@code <<} to mean both function composition and passing arguments.
 * This is very convenient, but it
 * creates ambiguity when the argument is a closure.
 * <p>
 * This class allows for use of convenient << syntax when passing closures as arguments.
 * Fpiglet uses it defining ClosureMonad
 * 
 * @see fpig.common.functions.ClosureMonad
 * 
 * @author Robert Peszek
 */
class ClosureHolder {
	
	ClosureHolder(Closure c){
		cVal = c
	}
	
    Closure cVal
}
