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
import static fpig.string.asfunlist.functions.StringAsFunList.*
import fpig.string.asfunlist.functions.InAndOutOfFunLists_forStrings as inout
import fpig.funlist.functions.BaseFL as fl

/**
 * Tests all the goodies converted over from functional lists to Strings
 * 
 * @author Robert Peszek
 */
class StringsAsFunListsTests extends GroovyTestCase{

	void testReverse() {		
		assert 'cba' == reverse << 'abc'
	}

	void testFoldL() {
 		assert ('c' as char) == foldL(MAX, 0) << 'abc'
		  
	}

	void testReduceL() {
 		assert ('c' as char) == reduceL(MAX) << 'abc'
	}

	void testBasics() {
		assert ('a' as char) == head << 'abc'
		assert ('b' as char) == head << tail << 'abc'
		assert ('c' as char) == head << tail << tail << 'abc'
		assert '' << inout.funlistOutC << fl.empty() 
		assert '' == tail << tail << tail << 'abc' 
	}

}
