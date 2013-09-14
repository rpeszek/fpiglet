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

import fpig.concepts.ApplicativeDescription;
import fpig.funlist.types.FunList;
import fpig.funlist.functions.BaseFL
import fpig.funlist.functions.Infinity
import fpig.util.CallUtil;
import groovy.lang.Closure;

import static fpig.funlist.functions.BaseFL.*

/**
 * Describes FunList as Applicative using 'zip' logic.
 * <p>
 * As type mapping this applicative description can be viewed as mapping from type A to type FunList&lt;A&gt;.
 *
 * <p> Wiki:
 * <br> <a href="http://code.google.com/p/fpiglet/wiki/ApplicativePolymorphism">http://code.google.com/p/fpiglet/wiki/ApplicativePolymorphism</a>
 * @author Robert Peszek
 */
class FunListZipApplicative<A> extends ApplicativeDescription<A, FunList<A>>{
   static FunListZipApplicative instance = new FunListZipApplicative() //@Singleton annotation does not want to work!
   
   Closure getPure(){
	  Infinity.repeat
   }

   Closure getFmap() {
	   BaseFL.map
   }	
   
   Closure getAp() {
	   CallUtil.toFunction {FunList ops, FunList args -> 
		   Closure zipC = CallUtil.toFunction {Closure c, x -> c(x)}  
		   def res= zipWith(zipC, ops, args)
		   res
	   }
   }
   
}
