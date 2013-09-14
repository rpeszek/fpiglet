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
import static fpig.string.asfunlist.functions.InAndOutOfFunLists_forTokenizedStrings.*
import static fpig.funlist.functions.BaseFL.head as f_head
import static fpig.funlist.functions.BaseFL.tail as f_tail
import static fpig.funlist.functions.BaseFL.empty as f_empty
import static fpig.funlist.functions.BaseFL.foldR as f_foldR

import fpig.funlist.types.FunList;

class InOutConversionTokenizedStringsTests extends GroovyTestCase{
	
	void testFunlistInForTokenizedString() {
		def list  = funlistIn(' ') << 'word1 word2 word3'
		assert 'word1' == f_head << list
		assert 'word2' == f_head << f_tail << list
		assert 'word3' == f_head << f_tail << f_tail << list
		assert  f_empty() == f_tail << f_tail << f_tail << list
	}

	void testFunlistOutForTokenizedString() {
		def list  = funlistIn(' ') << 'word1 word2 word3'
		String back = funlistOut(' ') << list
		assert back ==  'word1 word2 word3'
		assert f_empty() == funlistIn(' ') << ''
	}
	
	void testWithFunList() {
		assert 7 == withFunList(' ', f_foldR(f (MAX << {it.length()}), 0)) <<  '12 123 1234567'
	}

}
