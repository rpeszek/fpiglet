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
 * This class is used to 'describe' Monads. 
 * <p>
 * To define/describe a Monad in Fpiglet you need to extend this class.
 * <p>
 * <p> 
 * Generics are used for informational purposes only and this info is lost in pure and bind methods.
 * <p>
 * Monads is a stronger concept than Applicative or Functor ({@link fpig.concepts.MonadAsApplicative})
 * <p>
 * If you are comparing this to Haskell, Fpiglet uses the term 'pure' instead of 'return' and 
 * {@code bind} is a flipped version of Haskell's {@code >>=}.
 *
 * <p> Wiki:
 * <br> <a href="http://code.google.com/p/fpiglet/wiki/MonadPolymorphism">http://code.google.com/p/fpiglet/wiki/MonadPolymorphism</a>
 * @author Robert Peszek
 *
 * @param Generic Type A - type from
 * @param Generic Type M - type to  
 */
abstract class MonadDescription<A,M> extends TypeMapping<A,M> {
   
      

 /**
  * {@code pure} puts element {@code a} in a monadic context {@code m a}.
  * 
  * <p>
  * Logical signature
  * <pre>
  *    a -> m a
  * </pre>
  * or to use generic info:
  * <pre>
  *   A -> M
  * </pre>
  */
   abstract Closure getPure()
   
   
 /**
  * The transformation which defines the Monad.
  * <p>
  * Logical signature
  * <pre>
  *    (a -> m b) -> m a -> m b
  * </pre>   
  * or to use generic info:
  * <pre>
  *    (A -> M) -> M -> M
  * </pre>   
  * Notice this is flipped verson of Haskell >>= function.   
  */
   abstract Closure getBind()
   
}

