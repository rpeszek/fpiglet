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
 * Functional Alternative to nulls.  Similar to SCALA Option or Haskell Maybe.
 * <p>
 * Other than for variable declaration, programs should not interact with this type. Instead, use functions defined in
 * {@link fpig.common.functions.FpigBase}
 * <p>
 * Example:
 * <pre>
 * def bigBoss = [name:'Big Cheese']
 * def mike = [name:'Mike', boss: bigBoss]
 * def jon = [name:'Jon', boss: mike]
 * 
 * Closure boss = { person->
 *   if(person.boss){
 *     just(person.boss)
 *   } else {
 *     nothing()
 *   }
 * }
 *	   
 * assert isSomething << boss << jon
 * assert !(isSomething << boss << bigBoss)
 * assert unjust << boss << jon == mike
 * </pre>
 * <p> Wiki:
 * <br> <a href="http://code.google.com/p/fpiglet/wiki/Maybe">http://code.google.com/p/fpiglet/wiki/Maybe</a>
 * 
 * @see fpig.common.types.Just
 * @see fpig.common.types.Nothing
 * @see fpig.common.functions.FpigBase
 * @see fpig.common.functions.MaybeMonad
 * @author Robert Peszek
 */
abstract class Maybe<T> {

}
