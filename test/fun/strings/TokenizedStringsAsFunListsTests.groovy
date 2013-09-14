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
package fun.strings

import static fpig.common.functions.FpigBase.*
import static fpig.common.functions.FromOperators.*
import static fpig.common.functions.Projections.*
import fpig.string.asfunlist.functions.StringAsFunList
import fpig.string.asfunlist.functions.TokenizedStringAsFunList;
import static fpig.util.CallUtil.*

/**
 * Tests all the goodies converted over from functional lists to Strings
 * 
 * @author Robert Peszek
 */
class TokenizedStringsAsFunListsTests extends GroovyTestCase{
	
	void testReverse() {
		def sF	= TokenizedStringAsFunList.withToken(' ') 	
		assert 'word3 work2 word1' == sF.reverse << 'word1 work2 word3'
	}

	void testFoldL() {
		def sF	= TokenizedStringAsFunList.withToken(' ') 	
 		assert 7 == sF.foldL({a, b-> MAX(a, b.length())}, 0) <<  '12 123 1234567'
	
		assert 7 == sF.foldR({b, a-> MAX(b.length(), a)}, 0) <<  '12 123 1234567'

		assert 7 == sF.foldL(composeSecond(MAX, {it.length()}), 0) <<  '12 123 1234567'
		
		assert 7 == sF.foldR(f (MAX << {it.length()}), 0) <<  '12 123 1234567' //this is much neater
		assert 7 == sF.foldR(f (MAX << {it.length()}), 0) <<  '12 123 1234567' //this is much neater
		
		assert 7 == sF.foldL(flip (f (MAX << {it.length()})), 0) <<  '12 123 1234567'
		
		//count words
		assert 3 == sF.foldL(flip (f (PLUS << FIRST(1))), 0) <<  '12 123 1234567'
	}
}
