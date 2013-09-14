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
package fpig.applicative.functions

import fpig.concepts.ApplicativeDescription;
import fpig.funlist.functions.BaseFL;
import fpig.funlist.functions.Infinity;
import fpig.funlist.types.FunList;
import fpig.util.CallUtil;
import static fpig.expressions.IfElseSyntax.*
import static fpig.groovylist.asfunlist.functions.InAndOutOfFunLists.*
import static fpig.funlist.functions.BaseFL.*

/**
 * Defines base (standard) applicative functions.
 * <p>
 * This documentation uses Logical signatures. 
 * <p> Wiki:
 * <br> <a href="http://code.google.com/p/fpiglet/wiki/LogicalSignatures">http://code.google.com/p/fpiglet/wiki/LogicalSignatures</a>
 * @see fpig.concepts.ApplicativeDescription
 * @author Robert Peszek
 * @param Generic Type A - type to be converted
 * @param Generic Type F - applicative functor type
 */
class BaseA<A,F>  {
  /**
   * Returns instance of BaseA library for specified instance of ApplicativeDescription.
   */
   static <A,M> BaseA<A,M> getInstance(ApplicativeDescription<A, F> functor){
	   new BaseA(apfunctor: functor)	   
   }
   
   /**
    * PRIVATE   
    */
   ApplicativeDescription<A, F> apfunctor
  
   
   /**
   * Convenience 3 arg version of app.  Applies applicative functor transformed operation on 2 applicative functor transformed arguments.
   * <p>
   * 
   * logical signature:
   * <pre>
   * {@code
   *    f (a->b->r) -> f a-> f b -> f r
   * }
   * </pre>
   * If you are comparing this to Haskell, the equivalent of {@code ap2(f, x, y)} would be
   * <pre>
   * {@code
   *   f <*> x <*> y
   * }
   * </pre>
   */
  Closure getAp2() {
	  return CallUtil.toFunction { f_op, f2, f3 ->
		  def r_op = apfunctor.ap(f_op,f2)
		  apfunctor.ap(r_op,f3)
	  }
  }
  
  /**
   * Applicative logic across list of applicative values.
   * Accepts both FunList and Groovy List of arguments.
   * This is equivalent to (and is implemented using) a fold.
   * <p>
   * 
   * Example
   * <pre>
   *   MonadDescription m = MaybeMonad.instance
   *   ApplicativeDescription am = new MonadAsApplicative(m)
   *   BaseA baseA = BaseA.getInstance(am)
   *   baseA.apN(just({a,b,c-> a+b+c), [just(1), just(2), just(3)]) //returns just(6)
   * </pre>
   * Logical signature:
   * <pre>
   * {@code 
   *   f(a1->...->aN->r) -> [f a1, ... f aN] -> f r 
   * }
   * </pre>
   * If you are comparing this to Haskell, the equivalent of {@code apN(f, [x, y, z])} would be
   * <pre>
   * {@code
   *   f <*> x <*> y <*> z
   * }
   * </pre>
   */
  Closure getApN() {
	  return CallUtil.toFunction { f_op, argList ->
		  FunList argFList = 
		  _if_{argList instanceof List} >> _then_ { (funlistIn << argList) } >>
		  _elseif_ {argList instanceof FunList} >> _then_ { argList } >>
		  _else_ { throw new RuntimeException('getApN: invalid argument list' + argList) }
		  
		  def res = foldL (apfunctor.ap, f_op, argFList)
	  }
  }
  
  /**
   * Similar to ap2 only with 'regular' function as first argument.
   * So, for FunList with ZipApplicative this will end up being
   * equivalent to a standard functional `zipWith` accepting list of 2 arguments.
   * <p>
   * Example:
   * <pre>
   * ApplicativeDescription fa = FunListZipApplicative.instance
   * BaseA a = BaseA.getInstance(fa)
   * a.zipWith(PLUS, f([10, 1, 2]), f([1,2,3,4]))  //returns f([11,3,5]), PLUS is a predefined closure in Fpiglet
   * </pre>
   * logical signature:
   * <pre>
   * {@code 
   *   (a1->a2->r) -> f a1-> f a2 -> f r (where f is applicative functor)
   * }
   * </pre>
   * If you are comparing this to Haskell, the equivalent of {@code zipWith(f, x, y)} would be
   * <pre>
   * {@code
   *   f <$> x <*> y 
   * }
   * </pre>
   */
  Closure getZipWith() {
	  CallUtil.toFunction { op, f2, f3 ->
//		  def f_op = apfunctor.pure(op)
//		  return ap2(f_op, f2, f3)
		  return apfunctor.ap(apfunctor.fmap(op, f2), f3)
	  }
  }
  /**
   * Similar to apN only with a 'regular' function as first argument.
   * So for FunList with ZipApplicative this will end up being
   * equivalent to a typical list 'zip' function accepting list of N arguments.
   * <p>
   * Example:
   * <pre>
   * zipNWith( {a,b,c-> a+b+c}, [list1, list2, list3])
   * </pre>
   * 
   * Logical signature:
   * <pre>
   * (a1->...->aN->r) -> [f a1, ... f aN] -> f r  (where f is applicative functor, [] can be FunList or Groovy List)
   * </pre>
   * If you are comparing this to Haskell, the equivalent of {@code zipWith(f, x, y)} would be
   * <pre>
   * {@code
   *   f <$> x1 <*> ... <*> xN
   * }
   * </pre>
   */
  Closure getZipNWith() {
	  CallUtil.toFunction { op, f_argList -> 
		  def f_op = apfunctor.pure(op)
		  return apN(f_op, f_argList)
	  }
  }
}

