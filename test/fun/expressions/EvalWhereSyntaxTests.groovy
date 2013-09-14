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
package fun.expressions

import static fpig.common.functions.FpigBase.*
import static fpig.expressions.EvalWhereSyntax.*

class EvalWhereSyntaxTests extends GroovyTestCase{


    void testEvalWhereSyntax() {
								
        assert 5 ==  eval { sum.call(2,3) } << where {sum = {a, b-> a+b}} //that one uses propertyMissing
        assert 5 ==  eval { sum(2,3) } << where {sum = {a, b-> a+b}} //that one needs to use methodMissing as well
		assert 8 ==  eval { area(*rect) } << where { area = {a, b -> a *b }; rect = [2,4]}
		
		def expression = eval { addUp << split(5) } << where { addUp = {List a->  a.sum()}; split = f({n, a -> [a] * n})}
		assert expression instanceof Closure
		def testResult = expression(2)
		assert 10 == testResult
								
    }
	
	//clone of this test moved to Expo			
	void testEvalWhereSyntax2() {
		  def bmiCalculator = {massLb, heightIn -> (massLb / (heightIn * heightIn)) * 703}
				
		  def liggleGuyBmi = eval { bmi(littleguy) } <<
		       where { 
								  bmi = {person -> bmiCalculator(person.wLb, person.hIn)} 
										littleguy = [name:'littleguy', wLb: 150, hIn:5*12] 
			   }
		  assert liggleGuyBmi < 30
	}
				

}
