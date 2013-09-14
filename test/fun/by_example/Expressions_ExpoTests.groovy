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
import static fpig.expressions.IfElseSyntax.*
import static fpig.common.functions.FromOperators.LARGER
import static fpig.expressions.EvalWhereSyntax.*

/**
 * Shows some of the syntax DSL 
 * - Demonstrates more functional version of if-else statement
 * - Demonstrates scope localization with eval << where
 * (internally accomplished using curried function composition with small - minimum - amount of metaprogramming).
 * 
 * @author Robert Peszek
 */
class Expressions_ExpoTests extends GroovyTestCase{

    void testAndShowIfElseSyntax(){
		def score = 81
		def grade =    _if_{ score > 89 } >> _then_ { 'A' } >>
				   _elseif_{ score > 79 } >> _then_ {'B'} >>
				   _elseif_ { score > 69 } >> _then_ {'C'} >>
											 _else_ { 'F' }
							
		assert grade == 'B'
	}

	void testAndShowIfElseSyntaxEvenBetter(){
		//shows lazy if statement chain, all evaluation is deferred to the time number is passed to grade function
		def grade  = _ifun_{ it > 89 } >> _then_ {'A'}  >>
				   _elseif_{ it > 79 } >> _then_ {'B'}  >>
				   _elseif_{ it > 69 } >> _then_ {'C'}  >>
										 _else_ { 'F' }
		
		assert grade instanceof Closure
		assert grade(75.2) == 'C'
		assert grade(85.2) == 'B'
		assert grade(90) == 'A'
    }
	
	void testAndShowIfElseSyntaxEvenEvenBetter(){
		//larger is a function defined in fpig.common.functions.FromOperators
		//make _it_ your very distant cousin!
		
		def grade  = _ifun_(LARGER(89)) >> _then_ {'A'}  >>
		_elseif_(LARGER(79)) >> _then_ {'B'}  >>
		_elseif_(LARGER(69)) >> _then_ {'C'}  >>
							  _else_ { 'F' }

		assert grade instanceof Closure
		assert grade(75.2) == 'C'
		assert grade(85.2) == 'B'
		assert grade(90) == 'A'
	}

	void testAndShowEvalWhereSyntax() {
		def bmiCalculator = {massLb, heightIn -> (massLb / (heightIn * heightIn)) * 703}
		
		//note that bmi and littleguy are defined locally to eval!
	    def liggleGuyBmi = eval { bmi(littleguy) } <<
		   where {
			  bmi = {person -> bmiCalculator(person.wLb, person.hIn)}
			  littleguy = [name:'littleguy', wLb: 150, hIn:5*12]
		   }
		   
	    assert liggleGuyBmi < 30

	}
}
