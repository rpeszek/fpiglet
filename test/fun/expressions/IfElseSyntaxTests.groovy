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
import static fpig.expressions.IfElseSyntax.*
import fpig.expressions.IfElseSyntax;


class IfElseSyntaxTests extends GroovyTestCase{

				
 void testIfSyntax() {				
	    def score = 75.2
		def grade =   _else_ { 'F' }  << 	
		   _then_ {'C'} << _elseif_ { score > 69 } <<					
		   _then_ {'B'} << _elseif_ { score > 79 } <<
		   _then_ { 'A' }   <<  _if_{ score > 89 }
		
		assert grade == 'C'
  }

  //duplicate of Expo test
  void testIfForwardSyntax() {
		def score = 75.2
		def grade =    _if_{ score > 89 } >> _then_ { 'A' } >>
		           _elseif_{ score > 79 } >> _then_ {'B'} >>
				   _elseif_ { score > 69 } >> _then_ {'C'} >>
											 _else_ { 'F' }
							
		assert grade == 'C'
  }

				
    void testIfunSyntax() {
			//shows lazy if statement chain, all evaluation is deferred to the time number is passed to grade function
			def grade  =   _else_ { 'F' }  <<
						_then_ {'C'} << _elseif_ { it > 69 } <<
						_then_ {'B'} << _elseif_ { it > 79 } <<
						_then_ { 'A' } << _ifun_{ it > 89 }
			
			assert grade instanceof Closure
			assert grade(75.2) == 'C'
			assert grade(85.2) == 'B'
			assert grade(90) == 'A'
	}
				
	void testIfunForwardSyntax() {
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

				
	void testIfGoryDetails() {
			def x = 5
			def step1 = _if_{x < 0 }
			assert step1 instanceof IfElseSyntax
			def step2 =  _then_ { -1 } << step1
			assert step2 instanceof IfElseSyntax
			def step3 = _else_ { 1 } << step2
			assert step3 == 1
			
			assert 1 ==   _else_ { 1 }  << _then_ { -1 }   <<  _if_{ x < 0 }				
	}
			
	void testIfunGoryDetails() {
		def step1 = _ifun_{ it < 0 }
		assert step1 instanceof Closure
		def step2 =  _then_ { -1 } << step1
		assert step2 instanceof Closure
		def step3 = _else_ { 1 } << step2
		assert step3 instanceof Closure
		assert step3(5) == 1
		assert step3(-5) == -1
		
		assert 1 ==   (_else_ { 1 }  << _then_ { -1 }   <<  _ifun_{ it < 0 }) (5)
		assert -1 ==   (_else_ { 1 }  << _then_ { -1 }   <<  _ifun_{ it < 0 }) (-5)				
    }

			
	void testIfunAsTrasformation() {
				
			assert 105 ==   (_else_ { it + 100 }  << _then_ { it }   <<  _ifun_{ it < 0 }) (5)
			assert -5 ==   (_else_ { it + 100 }  << _then_ {it }   <<  _ifun_{ it < 0 }) (-5)
			
			assert 105 ==   ( _ifun_{ it < 0 } >> _then_ { it } >> _else_ { it + 100 }) (5)
			assert -5 ==   ( _ifun_{ it < 0 } >> _then_ { it } >> _else_ { it + 100 }) (-5)

	}

}
