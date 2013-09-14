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
package fpig.monad.functions

import fpig.applicative.functions.BaseA;
import fpig.concepts.ApplicativeDescription;
import fpig.concepts.MonadAsApplicative;
import fpig.concepts.MonadDescription;
import fpig.concepts.MonadWithEmptyDescription;
import fpig.funlist.functions.BaseFL;
import fpig.funlist.types.FunList;
import fpig.util.CallUtil;
import fpig.common.functions.FpigBase
import static fpig.expressions.IfElseSyntax.*
import static fpig.groovylist.asfunlist.functions.InAndOutOfFunLists.*
import static fpig.funlist.functions.BaseFL.*

/**
 * Defines base (standard) monadic functions
 * <p> Wiki:
 * <br> <a href="http://code.google.com/p/fpiglet/wiki/MonadFunctions">http://code.google.com/p/fpiglet/wiki/MonadFunctions</a>
 * @author Robert Peszek
 * @param Generic Type A - type mapped from
 * @param Generic Type M - type mapped to
 */
class BaseM<A,M>  {
  /**
    * Returns instance of BaseM library for specified instance of MonadDescription.
    */
   static <A,M> BaseM<A,M> getInstance(MonadDescription<A, M> monad){
	   ApplicativeDescription appF = new MonadAsApplicative(monad)
	   new BaseM(monad: monad, appFunctor: appF)	   
   }
   
   /**
	* PRIVATE
	*/
   MonadDescription<A, M> monad
   /**
	* PRIVATE
	*/
   ApplicativeDescription appFunctor
   
  /**
   * Equivalent of Functor {@code fmap}, implemented for monads.
   * <p>
   * Logical signature
   * <pre>
   * (a->b) -> m a-> m b
   * </pre>
   * @see fpig.concepts.MonadAsApplicative#getFmap()
   * @see fpig.concepts.FunctorDescription#getFmap()
   */
  Closure getLift() {
	  //c :: (a->b) -> m a -> m b} 
	  appFunctor.fmap
  }
  
  /**
   * Monadic left fold.
   * <p>
   * Logical signature
   * <pre>
   * (a->b-> m a) -> a -> [b] -> m a
   * </pre>
   * Example: find all outcomes after {@code n} tosses of a coin where heads is +1 (you win a dollar) and tails is -1 (you pay a dollar):
   * <pre>
   * Closure toss = { x-> f([x+1, x-1])}
   * BaseM m = BaseM.getInstance(FunListMonad.instance)
   * def foldNTosses = f {n -> m.foldLM (apply, 0) << take(n) << repeat(toss)}
   * </pre>
   */
  Closure getFoldLM() {
	  Closure foldL = BaseFL.foldL
	  CallUtil.toFunction { Closure foldF, acc, l-> 
		  Closure foldFM = {ma, b -> 
			  monad.bind(foldF(CallUtil._,b), ma) 
		  }
		  foldL(foldFM, monad.pure(acc), l)
	  }
  }
  
  /**
   * Monadic fold with stopping rule.
   * <p>
   * Logical signature
   * <pre>
   * (m a->boolean) -> (a->b-> m a) -> a -> [b] -> m a
   * </pre>
   * This allows to write code like this:
   * <pre>
   * def knightPath = ...
   * def initialPath = ...
   * def destination = ...
   * Closure pathLeadsTo = ...
   * BaseM m = BaseM.getInstance(FunListMonad.instance)
   * def stoppingRule = {pathList -> !(isEmpty << filter(pathLeadsTo(destination)) << pathList) }
   * def shortestPaths =  filter(pathLeadsTo(destination)) << m.foldLMUntil(stoppingRule, apply, initialPath) << repeat(knightPath)
   * </pre>
   */
  Closure getFoldLMUntil() {
	  Closure foldLUntil = BaseFL.foldLUntil
	  CallUtil.toFunction { Closure precicateF, Closure foldF, acc, l->
		  Closure foldFM = {ma, b ->
			  monad.bind(foldF(CallUtil._,b), ma)
		  }
		  foldLUntil(precicateF, foldFM, monad.pure(acc), l)
	  }
  }
  
  /**
   * Returns a monad filtered using predicate closure.
   * <p>
   * This function replays monadic values if predicate is satisfied otherwise returns emptyM monad.
   * Works with {@code MonadWithEmptyDescription} monads only.
   * <p>
   * <b> Not to confuse with Haskel filterM </b>. This is not a list function.
   * If you are comparing this to Haskell the equivalent would be:
   * <pre>
   *   (\predicate, mx -> mx >>= (\x -> guard(predicate(x)) >> return x))
   * </pre>
   * <br>
   * Logical Signature:
   * <pre>
   *   (a -> boolean) -> ma -> ma
   * </pre>
   * @see fpig.concepts.MonadWithEmptyDescription
   * @return
   */
  Closure getMfilter() {
	  if(!monad instanceof MonadWithEmptyDescription){
		  throw new RuntimeException("Invalid use of mfilter, monad is not MonadWithEmptyDescription")
	  } 
	  return CallUtil.toFunction {Closure predicate,  M mx->
		 monad.bind ({x ->
		   if(predicate(x))
		      monad.pure(x)
		   else
		      monad.emptyM 
		 }, mx)
	  }
  }
  
  /**
   * Flattens the monad.
   * <p>
   * Often an equivalent way to define a monad is to define it as functor with pure and join functions.
   * In fact, monads in math are defined that way.
   * <p>
   * Logical signature
   * <pre>
   * m (m a) -> m a
   * </pre>
   * Example:
   * <pre>
   * assert just(3) == join << just << just(3)
   * </pre>
   * @return
   */
  Closure getJoin() {	  
	  return monad.bind(FpigBase.ID)
  }
  
  /**
   * Applicative function. Implements applicative for monads.
   * <p>
   * Logical signature 
   * <pre>
   * m(a->b) -> m a-> m b
   * </pre>
   * Example
   * <pre>
   * assert just(6) == ap(just(TIMES(2))) << just(3)
   * </pre>
   */
  Closure getAp() {	  
	  appFunctor.ap
  }
  
  /**
   * Convenience 2 arg version of app.
   * Equivalent to similar function fund in {@link fpig.applicative.functions.BaseA}.
   * <p>
   * logical signature:
   * <pre>
   * m(a->b->c) -> m a-> m b-> m c
   * </pre>
   * @see fpig.applicative.functions.BaseA#getAp2()
   * @return
   */
  Closure getAp2() {
	  BaseA app = BaseA.getInstance(appFunctor)
      app.ap2
  }
  
  /**
   * Convenience N-arg version of app.
   * Equivalent to similar function fund in {@link fpig.applicative.functions.BaseA}.
   * @see fpig.applicative.functions.BaseA#getApN()
   */
  Closure getApN() {
	  BaseA app = BaseA.getInstance(appFunctor)
      app.apN
  }
  
  /**
   * Both meanings of unpure are correct: it 'reverses' the {@code pure} function (generalizing functions like unright or unjust) and is intended to invoke side-effects.
   * <p>
   * This function will simply replay passed monadic value as its result, but will also invoke passed closure.
   * Because it replays the monadic value it can be easily inserted into monadic composition.
   * <p>
   * FpigBase also defines flipped version named {@code asUnpure}.
   * FpigBase version of unpure and asUnpure are polymorphic (work across monads).
   * <p>
   * Example:
   * <pre>
   * Closure diet = { person ->
   *           if(person.weight>150)
   *              right([name:person.name, weight: person.weight-20])
   *           else
   *              left('eat more, you are too skinny')
   *       }
   *
   * def jon = [name: 'Jon', weight: 160]
   * def jonAfter1 = diet(jon)
   *
   * unpure(jonAfter1) { //alternative to unright
   *   assert it.weight == 140
   * }
   *
   * unpure(_, {println 'should not print that'}) << b(diet) << unpure(_,{println "Jon is now ${it.weight} lb"}) << b(diet) << unpure(_,{println "jon is now ${it.weight} lb"}) << right(jon)
   * 
   * asUnpure {println 'should not print that'} << b(diet) << asUnpure {println "Jon is now ${it.weight} lb"} << b(diet) << asUnpure {println "jon is now ${it.weight} lb"} << right(jon)
   * </pre>
   * logical signature:
   * <pre>
   * m a -> (a->_) -> m a
   * </pre>
   * @see fpig.common.functions.FpigBase
   */
  Closure getUnpure() {
	  CallUtil.toFunction { M mx, Closure sideEffectC->
		 monad.bind ({x ->
		   sideEffectC(x)
		   monad.pure(x)
		 }, mx) 
	  }
  }
  
  /**
   * Same as monad bind method.  Allows for more polymorphic use (BaseM can replace MonadDescription).
   * 
   * @see fpig.concepts.MonadDescription
   */
  Closure getBind() {
	  monad.bind
  }
  
 /**
   * Same as monad pure method.  Allows for more polymorphic use (BaseM can replace MonadDescription).
   * @see fpig.concepts.MonadDescription
   */
  Closure getPure() {
	  monad.pure
  }
}

