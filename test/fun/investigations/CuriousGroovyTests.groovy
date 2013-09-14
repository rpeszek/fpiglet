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
package fun.investigations


/**
 * Just my exploring Groovy, not much worth looking at here.
 * @author peszek
 *
 */
class CuriousGroovyTests extends GroovyTestCase{

	void testLeftShiftWithClosure() {
		//<< works better than >>
		def thrice = { a -> a * 3 }
		def plusOne = { a -> a + 1 }
		def expr = thrice >> plusOne //function composition
		// equivalent: times6 = { a -> thrice(twice(a)) }
		assert expr(2) == 7 //that works
		assert 6 == thrice << 2 //passes argument to closure

		//does not work
		//assert 2 >> thrice == 6

		assert 6 == ({a -> 2} >> thrice) () //that works
	}


	//				void testTemp() {
	//					  def arr = []
	//					  [1,2].collect {it} >> arr
	//							assert arr == [1, 2]
	//				}

	void testClosureInformation() {
		Closure c0 = {Object[] args -> ''}
		def parmTypes0 = c0.parameterTypes
		assert parmTypes0.size() == 1
		assert parmTypes0[0] == Object[]
		assert c0.maximumNumberOfParameters == 1

		def c1 = {a -> ''}
		def parmTypes1 = c1.parameterTypes
		assert parmTypes1.size() == 1
		assert parmTypes1[0] == Object
		assert c1.maximumNumberOfParameters == 1


		def c2 = {a, String s = '' -> ''}
		def parmTypes2 = c2.parameterTypes
		assert parmTypes2.size() == 2
		assert parmTypes2[0] == Object
		assert parmTypes2[1] == String
		assert c2.maximumNumberOfParameters == 2

	}
	
	void testCurringWithArrayArg() {
		Closure sumAll = {Integer[] args -> args.inject(0) {acc, arg -> acc + arg}}
		
		assert 4 == sumAll(1,1,1,1)
		def curriedVersion=  sumAll.curry(1).curry(1).curry(1).curry(1) 
		assert curriedVersion instanceof Closure
		assert curriedVersion() == 4
	}
	
	void testCurringWithFixArgs() {
		Closure sumAll = {a,b,c,d -> a+b+c+d}
		
		assert 4 == sumAll(1,1,1,1)
		def curriedVersion=  sumAll.curry(1).curry(1).curry(1).curry(1)
		assert curriedVersion instanceof Closure
		assert curriedVersion() == 4
		assert sumAll.curry(1).curry(1).curry(1).curry(1)() == 4
	}
	
	void testTrampolining() {
		def filterHelper
		filterHelper = {Closure predicate,  List l, acc ->
					if(!l)
						acc
					else if (predicate(l.head())){
						acc =  acc + [l.head()]
						filterHelper.trampoline(predicate, l.tail(), acc)
					}else
						filterHelper.trampoline(predicate, l.tail(), acc)
				}.trampoline()
		 
		def filter = {Closure predicate,  List l->
		   filterHelper(predicate,l,[])
		}
				
		def even = { it % 2==0}
		
		assert (1..500).collect {it*2} == filter(even, 1..1000)
	}

}
