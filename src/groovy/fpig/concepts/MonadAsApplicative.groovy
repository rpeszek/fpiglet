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

import fpig.monad.functions.BaseM;
import fpig.util.CallUtil;

/**
 * Bridges Monad to Applicative and to Functor (allows us to think that if X is a Monad, then X is also Applicative and is also Functor)
 * <p>
 * In math Monads are defined as Functors with extra pure and join transformation.  
 * For computing purposes it is best to define bind operation and implement join as a function. That is why Fpiglet decided
 * not to inherit Monad from Applicative.
 *
 * <p> Wiki:
 * <br> <a href="http://code.google.com/p/fpiglet/wiki/FunctorPolymorphism">http://code.google.com/p/fpiglet/wiki/FunctorPolymorphism</a>
 * <br> <a href="http://code.google.com/p/fpiglet/wiki/ApplicativePolymorphism">http://code.google.com/p/fpiglet/wiki/ApplicativePolymorphism</a>
 * <br> <a href="http://code.google.com/p/fpiglet/wiki/MonadPolymorphism">http://code.google.com/p/fpiglet/wiki/MonadPolymorphism</a>
 * 
 * @see fpig.concepts.FunctorDescription
 * @see fpig.concepts.ApplicativeDescription
 * @see fpig.concepts.MonadDescription
 * @author Robert Peszek
 * @param Generic Type F
 * @param Generic Type T
 */
class MonadAsApplicative<A,M> extends ApplicativeDescription<A,M> {
  MonadDescription monad
  
  MonadAsApplicative(MonadDescription<A,M> monad){
	  this.monad = monad
  }
      

  /**
   * Implements functor fmap for a monad.
   */
  Closure getFmap() {
	  CallUtil.toFunction {Closure c, M m ->
		  monad.bind({A a -> 
			  monad.pure(c(a)) 
			  }, 
		  m) 
	  }
  }

  /**
   * Implements applicative pure for a monad.
   */
  Closure getPure() {
	  monad.pure
  }  
  
  /**
   * Implements applicative ap for a monad.
   */
  Closure getAp() {
	  return CallUtil.toFunction {M mc,  M ma -> 
		 monad.bind ({Closure c ->
			 def mb = monad.bind({A x -> 
				 monad.pure(c(x))
			 }, ma)
			 mb 
		 }, mc) 
	  }
  }
}

