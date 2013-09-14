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
 * This class is used to 'describe' Applicative Functors. 
 * <p>
 * To define/describe Applicative Functor in Fpiglet you need to extend this class.
 * <p> 
 * To be Applicative the 'thing' needs to be described as a Functor first (this class extends {@code FunctorDescription}).
 * Monads logically imply Applicative (and can be converted to Applicative using {@link MonadAsApplicative}). 
 * To be a true Applicative, the type mapping must obey applicative and functor laws (see wiki page for more info).
 * <p>
 * Generics are used for informational purposes only.
 * <p>
 * Applicative is a powerful from of functional polymorphism.  Fpiglet includes a library of functions which work
 * on any Applicative: {@link fpig.applicative.functions.BaseA}.  
 * Applicative also yields a coding style where operations on several functors are 'applied' to the functor values almost as if they
 * were regular objects.
 * <pre>
 * ApplicativeDescription a = FunListZipApplicative.instance
 * BaseA af = BaseA.getInstance(a)
 * def data1 = f ([10, 1, 2])
 * def data2 = f ([1,2,3,4])
 * def res = af.zipWith(PLUS, data1, data2)
 * assert [11, 3, 5] == funlistOut << res	
 * </pre>
 *
 * If you are comparing this to haskell {@code ap} is equivalent to {@code <*>} except for not being infix.
 * <p>
 * This documentation uses Logical signatures. 
 * <p> Wiki:
 * <br> <a href="http://code.google.com/p/fpiglet/wiki/LogicalSignatures">http://code.google.com/p/fpiglet/wiki/LogicalSignatures</a>
 * <br> <a href="http://code.google.com/p/fpiglet/wiki/ApplicativePolymorphism">http://code.google.com/p/fpiglet/wiki/ApplicativePolymorphism</a>
 * @see fpig.applicative.functions.BaseA
 * @see fpig.concepts.FunctorDescription
 * @see fpig.concepts.MonadDescription
 * @see fpig.concepts.MonadAsApplicative
 * @author Robert Peszek
 *
 * @param Generic Type T - type from
 * @param Generic Type A - type to (Applicative Functor Type) 
 */
abstract class ApplicativeDescription<T,A> extends FunctorDescription<T,A> {
   
      
 /**
  * Returns closure which places element in applicative context.
  * <p> 
  * Logical signature
  * <pre>
  *    pure:: a -> f a //where f is applicative functor
  * </pre>
  * or to use generic info:
  * <pre>
  *  pure::  T -> A
  * </pre>
  */
   abstract Closure getPure()
   
   
 /**
  * Returns Applicative transformation.
  * <p>
  * Applicative transformation maps applicative functions to functions working on applicatives.
  * For example if applicative is {@link fpig.funlist.types.FunList} defined with zip applicative logic, ap allows to 
  * take a list of operations such as PLUS(3) or TIMES(4) and apply them to lists of elements. 
  * <p>
  * Logical signature.
  * <pre>
  *    ap :: f (a -> b) -> f a -> f b
  * </pre>
  *    
  * or to use generic info:
  * <pre>
  *    ap :: (T -> A) -> A -> A
  * </pre>
  * <p> Wiki:
  * <br> <a href="http://code.google.com/p/fpiglet/wiki/ApplicativePolymorphism">http://code.google.com/p/fpiglet/wiki/ApplicativePolymorphism</a>
  */
   abstract Closure getAp()
}

