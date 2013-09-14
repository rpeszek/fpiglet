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
package fpig.common.functions

import fpig.common.types.Either;
import fpig.common.types.Maybe;
import fpig.concepts.MonadDescription;
import fpig.util.CallUtil;
import groovy.lang.Closure;
import static fpig.common.functions.impl.FpigEitherBase.*

import static fpig.funlist.functions.BaseFL.*

/**
 * Describes {@link Either} as Monad. 
 * As type mapping this monad description can be viewed as mapping from from R to Either&lt;_, R &gt;.
 * <p>
 * Example of simple monadic use of Either:
 * <pre>
 * Closure diet = { person ->
 *     if(person.weight>150)
 *        right([name:person.name, weight: person.weight-20])
 *     else
 *        left('eat more, you are too skinny')
 * }
 * 
 * def jon = [name: 'Jon', weight: 160]   
 * def res = b(diet) << b(diet) << right(jon) //(1)
 * assert 'eat more, you are too skinny' == unleft(res)
 * </pre>
 * 
 * Line marked (1) can be replaced with
 * <pre>
 * MonadDescription m = EitherMonad.instance
 * def res = b(diet) << b(diet) << m.pure(jon)
 * </pre>
 * or with
 * <pre>
 * MonadDescription m = EitherMonad.instance
 * def res = m.bind(diet) << m.bind(diet) << m.pure(jon)
 * </pre>
 * <p> Wiki:
 * <br> <a href="http://code.google.com/p/fpiglet/wiki/Either">http://code.google.com/p/fpiglet/wiki/Either</a>
 * @see fpig.concepts.MonadDescription
 * @see fpig.monad.functions.BaseM
 * @see fpig.common.types.Either
 * @author Robert Peszek
 *
 */
class EitherMonad<R> extends MonadDescription<R, Either<Object, R>>{
   static EitherMonad instance = new EitherMonad() //@Singleton annotation does not want to work!
   
   Closure getPure(){
	  {R a -> right(a) }
   }
	
   Closure getBind(){
	   CallUtil.toFunction {Closure c, Either m-> 
		   if(isRight(m)){
			   c(unright(m))
		   } else {
		       m
		   }
	   }
   }
   
}
