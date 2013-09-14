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
package fpig.funlist.functions

import fpig.concepts.MonadDescription;
import fpig.concepts.MonadWithEmptyDescription;
import fpig.funlist.types.FunList;
import fpig.util.CallUtil;
import groovy.lang.Closure;

import fpig.funlist.functions.BaseFL as lists

/**
 * Describes FunList as Monad. This is the classic non-determinisic computation monad.
 * As type mapping this monad can be viewed as mapping from type A to FunList&lt;A&gt;.
 * <p> Wiki:
 * <br> <a href="http://code.google.com/p/fpiglet/wiki/MonadPolymorphism">http://code.google.com/p/fpiglet/wiki/MonadPolymorphism</a>
 * @author Robert Peszek
 */
class FunListMonad<A> extends MonadWithEmptyDescription<A, FunList<A>>{
   static FunListMonad instance = new FunListMonad() //@Singleton annotation does not want to work!
   
   Closure getPure(){
	  {A a -> lists.e(a) << lists.empty() }
   }
	
   Closure getBind(){
	   CallUtil.toFunction {Closure c, FunList<A> m-> 
		   def res = lists.concat << lists.map(c) << m
		   res
	   }
   }
   
   def getEmptyM() {
	   lists.empty()
   }
   
}
