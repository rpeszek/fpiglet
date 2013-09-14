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
package fpig.concepts

/**
 * This class is used to 'describe' a monad with extra information about what is 'empty'. 
 * <p> Wiki:
 * <br> <a href="http://code.google.com/p/fpiglet/wiki/MonadPolymorphism">http://code.google.com/p/fpiglet/wiki/MonadPolymorphism</a>
 * @see fpig.concepts.MonadDescription
 * @author Robert Peszek
 *
 * @param Generic Type A - type from
 * @param Generic Type M - type to  
 */
abstract class MonadWithEmptyDescription<A,M> extends MonadDescription<A,M> {
   
       
   /**
    * emptyM defines empty context.
    * <p>
    * In functional programming this is also called mzero.  
    * <p> Implementing class is expected to return monadic value representing empty context (typically a singleton).
    * <p>
    * Comparing to typical monad definition additive monads allow have mzero and mplus operations (they are also Monoids).
    * But mplus is often not needed and just having the concept of empty is useful for implementing various things, for example, 
    * {@code emptyM} is all that is needed to implement restrict/where conditions in monadic comprehension 
    * (which are equivalent to Haskell's guard function).
    */
   abstract def getEmptyM()
}

