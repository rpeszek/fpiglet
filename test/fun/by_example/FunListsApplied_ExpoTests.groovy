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
import static fpig.common.functions.Projections.*
import static fpig.string.asfunlist.functions.StringAsFunList.*
import static fpig.string.asfunlist.functions.InAndOutOfFunLists_forStrings.*
import static fpig.funlist.functions.BaseFL.filter as FL_filter
import static fpig.funlist.functions.BaseFL.length as FL_length
import static fpig.funlist.functions.BaseFL.map as FL_map
import static fpig.funlist.functions.BaseFL.foldL as FL_foldL
import static fpig.expressions.IfElseSyntax.*
import fpig.string.asfunlist.functions.FunListToStringFunctor;


/**
 * Shows examples of functional syntax converted to Groovy/Java Lists.
 *
 * Functor working under the hood is not shown.
 * 
 * @author Robert Peszek
 */
class FunListsApplied_ExpoTests extends GroovyTestCase{

   void testFunListsAsStrings() {
	   assert 4 == FL_length << FL_filter{it == 'e' as char} << funlistIn << "how many e's in this sentence?"
	   assert 4 == length << filter{it == 'e' as char} << "how many e's in this sentence?"
   }
   
   void testFunListsAsStrings2() {
	   def f_lower_case =  map {char c -> _if_{c in 'A'..'Z'} >> _then_ {c + 32 as char} >> _else_{c}} 
	   assert 'abc' == f_lower_case << 'AbC'
   }

   void testFunListsAsStrings3() {
	   def f_lower_case =  funlistOut << FL_map {char c -> _if_{c in 'A'..'Z'} >> _then_ {c + 32 as char} >> _else_{c}} << funlistIn
	   assert 'abc' == f_lower_case << 'AbC'
   }

   void testFunListsAsStrings4() {
	   def f_lower_case =  withFunList(FL_map {char c -> _if_{c in 'A'..'Z'} >> _then_ {c + 32 as char} >> _else_{c}})
	   assert 'abc' == f_lower_case << 'AbC'
   }

   void testFoldL() {
	   def fmap = FunListToStringFunctor.statics.fmap
	   assert ('c' as char) == fmap(  FL_foldL(MAX, 0) ) << 'abc'
		
  }

}
