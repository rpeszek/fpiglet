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
 * Functional Alternative to Exceptions, similar to Haskell Either class.
 * <p>
 * Other than for variable declaration, programs should not interact with this type. Instead, programs should use functions defined in
 * {@link fpig.common.functions.FpigBase}.
 * <p>
 * Example:
 * <pre>
 * Closure diet = { person ->
 *     if(person.weight>150)
 *        right([name:person.name, weight: person.weight-20])
 *     else
 *        left('eat more, you are too skinny')
 * }
 *
 * def bob = [name: 'Bob', weight: 200]
 *	   
 * def bobAfterDiet = diet(bob)
 * assert isRight(bobAfterDiet)
 * assert unright(bobAfterDiet).weight == 180
 * </pre>
 * <p> Wiki:
 * <br> <a href="http://code.google.com/p/fpiglet/wiki/Either">http://code.google.com/p/fpiglet/wiki/Either</a>
 * 
 * @see fpig.common.types.Left
 * @see fpig.common.types.Right
 * @see fpig.common.functions.FpigBase
 * @see fpig.common.functions.EitherMonad
 * @author Robert Peszek
 */
abstract class Either<L, R> {

}
