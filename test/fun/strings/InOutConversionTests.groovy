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
import static fpig.string.asfunlist.functions.InAndOutOfFunLists_forStrings.*
import static fpig.funlist.functions.BaseFL.head as f_head
import static fpig.funlist.functions.BaseFL.tail as f_tail
import static fpig.funlist.functions.BaseFL.empty as f_empty
import fpig.funlist.types.FunList;

class InOutConversionTests extends GroovyTestCase{

	void testFunlistInForCharStrings() {		
		def list  = funlistIn << 'abc'
		
		assert 'a' as char == f_head << list
		assert 'b' as char == f_head << f_tail << list
		assert 'c' as char == f_head << f_tail << f_tail << list
		assert f_empty()   == f_tail << f_tail << f_tail << list

	}

	void testFunlistOutCharStrings() {		
		def list  = funlistIn << 'abc'
		String back = funlistOut << list
		assert back == 'abc'
	}
	

}
