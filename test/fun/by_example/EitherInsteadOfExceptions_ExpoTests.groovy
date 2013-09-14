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

class EitherInsteadOfExceptions_ExpoTests extends GroovyTestCase{

  
   void testAndShow_EitherType() {
	   //left indicates error condition, right contains successful result
	   //Eigher is functional alternative to OO Exceptions.
	   //this example uses string error message, but any object (including Java Exception) can be used
	   //for values returned as left
	   
	   Closure diet = { person ->
		   if(person.weight>150)
			  right([name:person.name, weight: person.weight-20])
		   else
			  left('eat more, you are too skinny')
	   }

	   def bob = [name: 'Bob', weight: 200]
	   def jon = [name: 'Jon', weight: 160]
	   
       def bobAfter1 = diet(bob)
	   assert isRight(bobAfter1)
	   assert unright(bobAfter1).weight == 180

	   def jonAfter1 = diet(jon)
	   assert isRight(jonAfter1)
	   def newJon = unright(jonAfter1)
	   assert newJon.weight == 140

	   //alternative assert using unpure
	   unpure(jonAfter1) {
		   assert it.weight == 140
	   }
	   
	   assert !(isRight << diet << newJon)
	   

	   //Either is a monad so usafe operations can be chained
	   MonadDescription m = EitherMonad.instance	   
	   def res = b(diet) << b(diet) << m.pure(jon) 
	   //or instead of using m.pure:
	   def res2 = b(diet) << b(diet) << right(jon)
	   
	   //unpure will not execute on exception
	   unpure(res2) {
		   throw new RuntimeException('should not execute')
	   }
	   
	   unpure(_, {println 'should not print that'}) << b(diet) << unpure(_,{println "(1) jon is now ${it.weight} lb"}) << b(diet) << unpure(_,{println "(1) jon is now ${it.weight} lb"}) << right(jon)
	   asUnpure{println 'should not print that'} << b(diet) << asUnpure {println "(2) jon is now ${it.weight} lb"} << b(diet) << asUnpure{println "(2) jon is now ${it.weight} lb"} << right(jon)
	   

	   assert !isRight(res)
	   assert 'eat more, you are too skinny' == unleft(res)	   
	   assert !isRight(res2)
	   assert 'eat more, you are too skinny' == unleft(res2)	   
   } 

   void testAndShow_EitherAsApplicative() {
	   BaseM m = BaseM.getInstance(EitherMonad.instance)
	   def add3 = f {a, b, c-> a+b+c}
	   
	   //applicative methods are available as monadic functions
	   //applicative allows to apply functions, or operations to transformed values, so we can add or multiply right values using right operations!
	   def res = m.apN(right(add3)) << [right(1), right(2), right(3)]
	   assert 6 == unright(res)
	   
	   def res2 = m.ap2(right(TIMES), right(3), right(3))
	   assert 9 == unright(res2)
	   
	   //this can be also done using applicative functions
	   ApplicativeDescription applicative = new MonadAsApplicative(EitherMonad.instance)
	   BaseA a = BaseA.getInstance(applicative)
	   
	   def _res = a.apN(right(add3)) << [right(1), right(2), right(3)]
	   assert 6 == unright(_res)
	   
	   def _res2 = a.ap2(right(TIMES), right(3), right(3))
	   assert 9 == unright(_res2)
	   
	   //there is a generalized version of zipWith that works for any applicative and allows to use regular functions with
	   //Either arguments:
	   def __res = a.zipNWith(add3) << [right(1), right(2), right(3)]
	   assert 6 == unright(_res)
	   
	   def __res2 = a.zipWith(TIMES, right(3), right(3))
	   assert 9 == unright(_res2)

   }
 
}
