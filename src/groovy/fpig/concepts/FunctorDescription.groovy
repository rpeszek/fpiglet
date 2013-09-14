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
 * This class is used to 'describe' Functors. 
 * <p>
 * To define/describe a Functor in Fpiglet you need to extend this class.
 * <p>
 * Functors are very important (101) concept in functional programming and in Fpiglet. Please review documentation on Fpiglet wiki
 * if you are new to the concept.  To be a true functor, type mapping must obey functor laws (see wiki page for more info).
 * In the nutshell functors are for mapping over types in a way that does not break function composition.
 * <p>
 * Generics are used for informational purposes only and this info is lost in fmap definition.
 * <p>
 * Fpiglet defines several Functors listed in see also.
 * <p> Wiki:
 * <br> <a href="http://code.google.com/p/fpiglet/wiki/FunctorPolymorphism">http://code.google.com/p/fpiglet/wiki/FunctorPolymorphism</a>
 * <br> <a href="http://code.google.com/p/fpiglet/wiki/LogicalSignatures">http://code.google.com/p/fpiglet/wiki/LogicalSignatures</a>
 * 
 * @see fpig.groovylist.asfunlist.functions.FunListToListFunctor
 * @see fpig.string.asfunlist.functions.FunListToStringFunctor
 * @see fpig.string.asfunlist.functions.FunListToTokenizedStringFunctor
 * @see fpig.io.asfunlist.functions.FunListToTokenizedInputStreamFunctor
 * @author Robert Peszek
 *
 * @param Generic Type A - mapped from
 * @param Generic Type B - mapped to
 */
abstract class FunctorDescription<A,B> extends TypeMapping<A,B> {
   
      
   /**
    * The essence of being a functor.
    * <p>
    * Logical signature:
    * <pre>
    *   fmap : (a->b) -> F a -> F b  
    * </pre>  
    * 
    * Classinc example, we could define fmap for mapping between any type T and Groovy List<T> like so:
    * <pre>
    *  fmap = {Closure c, List<T> list -> list.collect(c) }
    * </pre>   
    * Since these are curried functions, generics information does not make much sense!
   */
   abstract Closure getFmap()
}

