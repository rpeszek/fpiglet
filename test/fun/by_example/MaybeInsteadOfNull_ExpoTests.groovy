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
package fun.by_example

import static fpig.common.functions.FpigBase.*
import static fpig.common.functions.FromOperators.*
import fpig.applicative.functions.BaseA;
import fpig.common.functions.MaybeMonad
import fpig.common.functions.EitherMonad
import fpig.common.*
import fpig.concepts.ApplicativeDescription;
import fpig.concepts.MonadAsApplicative;
import fpig.concepts.MonadDescription;
import fpig.monad.functions.BaseM;

class MaybeInsteadOfNull_ExpoTests extends GroovyTestCase{

  
   void testAndShow_MaybeType() {
	   //Functional alternative to handling nulls.
       //Just represents non-null value
	   //Nothing is equivalent to null
	   
	   //consider imaginary company with this corporate ladder,
	   //(not very functional code, it uses plain Groovy):
	   def bigBoss = [name:'Big Cheese']
	   def mike = [name:'Mike', boss: bigBoss]
	   def jon = [name:'Jon', boss: mike]
	   def ann = [name:'Ann', boss: mike]
	   
	   Closure boss = { person->
		   if(person.boss){
			   just(person.boss)
		   } else {
		       nothing()
		   }
	   }
	   
	   assert isSomething << boss << jon
	   assert !(isSomething << boss << bigBoss)
	   
	   //since maybe is a monad, we can chain boss method
	   def grandBoss = b(boss) << b(boss)
	   assert isSomething << grandBoss << just(jon)
	   assert bigBoss == unjust	<< grandBoss << just(ann)
	   //alternative assert using unpure
	   unpure(grandBoss << just(ann)) {
		   assert it == bigBoss
	   }

	   assert !(isSomething << grandBoss << just << mike)  
	   assert !(isSomething << grandBoss << just << bigBoss)	   
   } 
   
   void testAndShow_MaybeAsApplicative() {
	   BaseM m = BaseM.getInstance(MaybeMonad.instance)
	   def add3 = f {a, b, c-> a+b+c}
	   
	   //applicative methods are available as monadic functions
	   //applicative allows to apply functions, or operations to transformed values, so we can add or multiply just values using just operations!
	   def res = m.apN(just(add3)) << [just(1), just(2), just(3)]
	   assert 6 == unjust(res)
	   
	   def res2 = m.ap2(just(TIMES), just(3), just(3))
	   assert 9 == unjust(res2)
	   
	   //this can be also done using applicative functions
	   ApplicativeDescription applicative = new MonadAsApplicative(MaybeMonad.instance)
	   BaseA a = BaseA.getInstance(applicative)
	   
	   def _res = a.apN(just(add3)) << [just(1), just(2), just(3)]
	   assert 6 == unjust(_res)
	   
	   def _res2 = a.ap2(just(TIMES), just(3), just(3))
	   assert 9 == unjust(_res2)
	   
	   //there is a generalized version of zipWith that works for any applicative and allows to use regular functions with 
	   //Maybe arguments:
	   def __res = a.zipNWith(add3) << [just(1), just(2), just(3)]
	   assert 6 == unjust(__res)
	   
	   def __res2 = a.zipWith(TIMES, just(3), just(3))
	   assert 9 == unjust(__res2)

	   	   
   } 
}
