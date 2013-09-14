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
package fun.common

import static fpig.common.functions.FpigBase.*
import fpig.common.functions.MaybeMonad
import fpig.common.functions.EitherMonad
import fpig.common.*
import fpig.concepts.MonadDescription;
import fpig.monad.functions.BaseM;
import static fpig.common.functions.FromOperators.*

class BasicFunctionalTypesTests extends GroovyTestCase{

    
   void testMaybe() {
	   def just4 = just(4)
	   assert isSomething(just4)
	   assert !isSomething(nothing())
	   assert 4 == unjust(just4)

	   assert just('test') == just('test')
	   assert just('test') != just('test1')
	   assert nothing() == nothing()

	   MonadDescription m = MaybeMonad.instance
	   Closure c = { x ->
			 if(x<10)
				just(x)
			 else
				nothing()
	   }
	   
	   def res = b(c) << m.pure(4)
	   assert isSomething(res)
	   assert 4 == unjust(res)
	   
	   def res2 = b(c) << m.pure(40)
	   assert !isSomething(res2)
	   
	   BaseM mf = BaseM.getInstance(m)
	   assert just(3) == mf.join << just << just(3)
	   
	   assert just(6) == mf.ap(just(TIMES(2))) << just(3)
	   
   }
   
   void testEither() {
	   def e1 = right(5)
	   assert isRight(e1)
	   def e2 = left('error message')
	   assert !isRight(e2)
	   assert 5 == unright(e1)
	   assert 'error message' == unleft(e2)

	   assert right('hello') == right('hello')
	   assert left('hello') == left('hello')
	   assert right('hello') != right('hello1')
	   assert left('hello') != left('hello1')
	   
	   
	   MonadDescription m = EitherMonad.instance
	   Closure c = { x ->
			if(x<10)
			   right(2 * x)
			else
			   left('wrong value')
	   }
	   
	   def res = b(c) << b(c) << m.pure(4)
	   assert isRight(res)
	   assert 16 == unright(res)
	   
	   def res2 = b(c) << b(c) << m.pure(7)
	   assert !isRight(res2)
	   assert 'wrong value' == unleft(res2)
	   
   }

   void testToString() {
	   assert just(5).toString() == 'Just(5)'
	   assert nothing().toString() == 'Nothing'
	   assert right(5).toString() == 'Right(5)'
	   assert left(5).toString() == 'Left(5)'

	   assert just(null).toString() == 'Just(null)'
	   assert right(null).toString() == 'Right(null)'
	   assert left(null).toString() == 'Left(null)'

   }
}
