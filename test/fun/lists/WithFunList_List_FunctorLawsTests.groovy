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
package fun.lists

import static fpig.common.functions.FpigBase.*
import static fpig.groovylist.asfunlist.functions.InAndOutOfFunLists.*
import static fpig.funlist.functions.BaseFL.*
import fpig.groovylist.asfunlist.functions.FunListToListFunctor;
import fpig.funlist.types.FunList;
import fpig.funlist.types.LazyList;

class WithFunList_List_FunctorLawsTests extends GroovyTestCase{

	void testWithFunList_ID() {
		assert [1,5,7] == (withFunList (ID)) << [1,5,7]
	}
	
	void testWithFunList_ID_functorsyntax() {
		assert [1,5,7] == (FunListToListFunctor.statics.fmap (ID)) << [1,5,7]
	}

	
	void testWithFunList_composition() {
		assert [1,5,7] == (withFunList (reverse << reverse)) << [1,5,7]
		assert [1,5,7] == (withFunList (reverse)) << (withFunList (reverse)) << [1,5,7]
	}

	void testWithFunList_composition2() {
		assert [5,10,50] == withFunList (filter {it % 5==0} << map {1 + it * it})  << (1..7)
		assert [5,10,50] == withFunList (filter {it % 5==0}) << withFunList (map {1 + it * it} ) << (1..7)
		
	}
	
	void testWithFunList_composition_functor_syntax() {
		Closure fmap = FunListToListFunctor.statics.fmap
		
		assert [5,10,50] == fmap(filter {it % 5==0} << map {1 + it * it})  << (1..7)
		assert [5,10,50] == fmap (filter {it % 5==0}) << fmap (map {1 + it * it} ) << (1..7)
		
	}


	//take is sort a functor but not exactly (would not work with composition2 test above)
	void testWithFunListTake_SemiIDLaw() {
		def res = (withFunListTake (3, ID)) << [1,2,3]
		assert [1,2,3] == res
	}

	void testWithFunListTake_composition() {
		assert [1,5,7] == (withFunListTake (3, reverse << reverse)) << [1,5,7]
		assert [1,5,7] == (withFunListTake (3, reverse)) << (withFunListTake (3, reverse)) << [1,5,7]
	}
	

}
