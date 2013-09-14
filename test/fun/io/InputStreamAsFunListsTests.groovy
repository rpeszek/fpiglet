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
package fun.io

import static fpig.common.functions.FpigBase.*
import static fpig.common.functions.FromOperators.*
import static fpig.funlist.functions.BaseFL.*
import static fpig.io.asfunlist.functions.InAndOutOfFunLists.*
import fpig.io.asfunlist.functions.FunListToTokenizedInputStreamFunctor;
import fpig.io.asfunlist.functions.InAndOutOfFunLists;
import fpig.io.asfunlist.functions.TokenizedInputStreamAsFunList;
import fpig.io.asfunlist.impl.FunListInputStream;
import fpig.funlist.types.FunList;

/**
 * Tests all the goodies converted over from functional lists to Strings
 * 
 * @author Robert Peszek
 */
class InputStreamAsFunListsTests extends GroovyTestCase{

	void testFunListInputStream() {	
		def myList = e('hello') << e('there') << e('here') << e('it') << e('is') <<  empty()
        def myInputStream = new FunListInputStream(universalUntokenizer('\n'), myList) 
        def myReader = new InputStreamReader(myInputStream)

        assert 'hello' == myReader.readLine()
        assert 'there' == myReader.readLine()
        assert 'here' == myReader.readLine()
        assert 'it' == myReader.readLine()
        assert 'is' == myReader.readLine()
        assert null == myReader.readLine()
    }	 

	
	void testUniversalTokenizer() {
		String s =
"""line 1
line 2
line 3"""
		InputStream input = new ByteArrayInputStream(s.getBytes())

		assert InAndOutOfFunLists.universalTokenizer('\n', input) == 'line 1'
		assert InAndOutOfFunLists.universalTokenizer('\n', input) == 'line 2'
		assert InAndOutOfFunLists.universalTokenizer('\n', input) == 'line 3'
		
	}

	
	void testSideffects() {
		String s = "line 1\nline 2\nline 3"

		InputStream input = new ByteArrayInputStream(s.getBytes())
		
		FunList list = funlistIn(universalTokenizer('\n')) << input
		
 		assert 'line 1' == head << list
		assert 'line 2' == head << tail << list
		assert 'line 2' == head << tail << list
		assert 'line 2' == head << tail << list
		assert 'line 3' == head << tail << tail << list
		assert 'line 3' == head << tail << tail << list
		assert 'line 3' == head << tail << tail << list
		assert 'line 3' == head << tail << tail << list
	}

	void testInAndOutForStreams() {
		String s = "line 1\nline 2\nline 3"

		InputStream input = new ByteArrayInputStream(s.getBytes())
		
		FunList list = funlistIn(universalTokenizer('\n')) << input
// //NOTE this has sideeffects! so this will not work!
//		assert list.head == 'line 1'
//		assert list.tail.head == 'line 2'
//		assert list.tail.tail.head == 'line 3'
		
		FunList reversedList = reverse << list
		assert reversedList.head == 'line 3'
		
		InputStream newIs = funlistOut(universalUntokenizer('\n')) << reversedList
		
		def myReader = new InputStreamReader(newIs)		
		assert 'line 3' == myReader.readLine()
		assert 'line 2' == myReader.readLine()
		assert 'line 1' == myReader.readLine()		
	}
	
	void testWithFunlistForStreams() {
		String s = "line 1\nline 2\nline 3"
		InputStream input = new ByteArrayInputStream(s.getBytes())
		
		InputStream newIs = withFunListUsingToken('\n' as char, reverse) << input
		
		def myReader = new InputStreamReader(newIs)
		assert 'line 3' == myReader.readLine()
		assert 'line 2' == myReader.readLine()
		assert 'line 1' == myReader.readLine()
	}

	void testFunctorialMapping() {
		String s = "line 1\nline 2\nline 3"

		InputStream input = new ByteArrayInputStream(s.getBytes())
		
		def functor = FunListToTokenizedInputStreamFunctor.forLineTokenization()
		InputStream newIs = functor.fmap(reverse) << input
		
		def myReader = new InputStreamReader(newIs)
		assert 'line 3' == myReader.readLine()
		assert 'line 2' == myReader.readLine()
		assert 'line 1' == myReader.readLine()
	}

	void testShortcuts() {
		def io = TokenizedInputStreamAsFunList.withLines()
		
		File f = new File('./resources/test/test.txt')
		assert f.exists()
		f.withInputStream { is->
			def isNew = io.filter{it.length()>10}  << is
			def txt = isNew.getText() //== 'long long line 2\n' + 'long long line 4' 
			//TODO figure out best OS agnostic test 
			assert txt ==~ /long long line 2\s*long long line 4\s*/
		}		
	}
}
