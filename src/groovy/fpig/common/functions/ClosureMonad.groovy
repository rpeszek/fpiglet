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

import fpig.common.types.ClosureHolder;
import fpig.common.types.Either;
import fpig.common.types.Maybe;
import fpig.concepts.MonadDescription;
import fpig.util.CallUtil;
import groovy.lang.Closure;
import static fpig.common.functions.impl.FpigEitherBase.*

import static fpig.funlist.functions.BaseFL.*

/**
 * Describes Closure as Monad. 
 * As type mapping this monad description can be viewed as mapping from type T to {@code ClosureHolder} containing Closures returning values of type T.
 * <p>
 * ClosureHolder is used because standard syntax {@code <<} does not work well when trying to pass closure as parameter.
 * <p>
 * Example:
 * <pre>
 * def someVar = 50
 * def closure1 = fH({ someVar })
 * def closure2 = fH({ new Random().nextInt() })
 * ClosureHolder res = selectP{ [x, y] }.from{
 *    x << {closure1}
 *    y << {closure2}
 * }
 * assert res.cVal() instanceof List
 * assert res.cVal()[0] = 50
 * </pre>
 * Example 2:
 * <pre>
 * Closure poly = selectP { x3 + 3*x2 + x + 5 }.from {
 *    x3 << { POWER(_,3) }
 *    x2 << { POWER(_,2) }
 *    x  << { POWER(_,1) }
 * }
 * </pre>
 * defines polynomial function: {@code x^3 + 3x^2 + x + 5}.  
 * <br> Note: the above comprehension has done automatic boxing and un-boxing between
 * {@code Closure} and {@code ClosureHolder}!
 * <p>
 * NOTE: This monad is intended to be used with both Fpiglet curried functions and regular Groovy closures.
 * It assumes that closures in the format 
 * <pre>
 *  {Object[] args -> ...}
 * </pre>
 * are curried functions.
 * <p>
 * If you compare this to Haskell, this is a simple special case of MonadReader
 * 
 * @see fpig.concepts.MonadDescription
 * @see fpig.common.types.ClosureHolder
 * @author Robert Peszek
 *
 */
class ClosureMonad<T> extends MonadDescription<T, ClosureHolder>{
   static ClosureMonad instance = new ClosureMonad() //@Singleton annotation does not want to work!
   
   Closure getPure(){
	  {T a -> new ClosureHolder({ a }) }
   }
	
   Closure getBind(){
	   return CallUtil.toFunction {Closure c, ClosureHolder m-> 
		   if(m.cVal.maximumNumberOfParameters == 0){
			  new ClosureHolder( { ->
				def mRes = m.cVal()
			    ClosureHolder resH = c(mRes)
				return resH.cVal()
			 })
    	   } else {
		     def resC = {x ->
				def mRes = m.cVal(x)
			    ClosureHolder resH = c(mRes)
				return resH.cVal(x)
			 }
			 if(CallUtil.isCurriedFunctionSuspect(m.cVal)){
				 new ClosureHolder(CallUtil.toFunction(resC))
			 } else {
			 	 new ClosureHolder(resC)
			 }
		   }
	   }
   }
   
}
