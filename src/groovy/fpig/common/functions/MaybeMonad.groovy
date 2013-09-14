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

import fpig.common.types.Maybe;
import fpig.concepts.MonadDescription;
import fpig.concepts.MonadWithEmptyDescription;
import fpig.util.CallUtil;
import groovy.lang.Closure;
import static fpig.common.functions.impl.FpigMaybeBase.*

import static fpig.funlist.functions.BaseFL.*

/**
 * Describes {@link fpig.common.types.Maybe} as Monad. 
 * As type mapping this monad description can be viewed as mapping from type A to Maybe&lt;A&gt;.
 * <p>
 * Example of simple monadic use of Maybe:
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
 *
 * def grandBoss = b(boss) << b(boss)
 * assert isSomething << grandBoss << just(jon) //(1)
 * </pre>
 * Line marked (1) can be replaced with
 * <pre>
 * MonadDescription m = MaybeMonad.instance
 * assert isSomething << grandBoss << m.pure(jon)
 * </pre>
 * <p> Wiki:
 * <br> <a href="http://code.google.com/p/fpiglet/wiki/Maybe">http://code.google.com/p/fpiglet/wiki/Maybe</a>
 * 
 * @see fpig.common.types.Maybe
 * @see fpig.concepts.MonadDescription
 * @see fpig.concepts.MonadWithEmptyDescription
 * @see fpig.monad.functions.BaseM
 * @author Robert Peszek
 *
 */
class MaybeMonad<A> extends MonadWithEmptyDescription<A, Maybe<A>>{
   static MaybeMonad instance = new MaybeMonad() //@Singleton annotation does not want to work!
   
   Closure getPure(){
	  {A a -> just(a) }
   }
	
   Closure getBind(){
	   CallUtil.toFunction {Closure c, Maybe<A> m-> 
		   if(isSomething(m)){
			   c(unjust(m))
		   } else {
		       nothing()
		   }
	   }
   }
   
   def getEmptyM() {
	   nothing()
   }
   
}
