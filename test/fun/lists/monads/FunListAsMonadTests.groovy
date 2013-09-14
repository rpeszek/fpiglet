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
package fun.lists.monads

import static fpig.common.functions.FpigBase.*
import static fpig.groovylist.asfunlist.functions.InAndOutOfFunLists.*
import static fpig.funlist.functions.BaseFL.*
import static fpig.funlist.functions.FunListMonad.*
import static fpig.common.functions.FromOperators.*
import static fpig.common.functions.Projections.*
import fpig.concepts.ApplicativeDescription;
import fpig.concepts.FunctorDescription;
import fpig.concepts.MonadAsApplicative;
import fpig.funlist.functions.FunListMonad;
import fpig.funlist.functions.Infinity;
import fpig.funlist.types.FunList;
import fpig.funlist.types.LazyList;
import fpig.monad.functions.BaseM;
import groovy.lang.Closure;

class FunListAsMonadTests extends GroovyTestCase{

    void testSanity() {
		def range = funlistIn << (-1..1)
		def plusOrMinusOne = {int x -> 
			map(PLUS(x)) << range 
		}
		def p = FunListMonad.instance.pure
		
		def res = b(plusOrMinusOne) << p(0)
		
		assert -1..1 == funlistOut << res
		
		[-2,-1,0,-1,0,1,0,1,2] == b(plusOrMinusOne) << b(plusOrMinusOne) << p(0)
		
		assert 2 == length << filter(EQUALS(1)) << b(plusOrMinusOne) << b(plusOrMinusOne) << p(0)
	}
	
	void testFunctorConversion() {
		FunctorDescription functor = new MonadAsApplicative(FunListMonad.instance)
		
		def list = funlistIn << [1,3,7,8]
		def mymap = map(POWER(_,2)) 
		def myfmap = functor.fmap(POWER(_,2))
		
		//TODO need quckcheck here really need it!
		def res1 = funlistOut << mymap << list 
		def x = myfmap (list)
		def res2 = funlistOut << myfmap << list
		assert res1 == res2
	}

	void testApplicativeConversion() {
		ApplicativeDescription apfunctor = new MonadAsApplicative(FunListMonad.instance)
				
		def list0 = apfunctor.pure(5)
		assert [5] == funlistOut << list0
				 
		def list = funlistIn << [1,3]
		def ops = funlistIn << [PLUS(1),TIMES(2)]
		
		def resList = apfunctor.ap (ops, list)
		def groovyL =  funlistOut << resList
		assert groovyL == [2,4,2,6]

	}	

	//kind-a gory, better tests in by_example folder
	//verified against haskell foldM
	void testFoldLM() {
         BaseM m = BaseM.getInstance(FunListMonad.instance)		
		Closure fC = f {a, b -> 
			(e(b) << e(-b) << empty())
		}
		def res = m.foldLM(fC, 0) << funlistIn << [1,1,1]
		assert [1,-1,1,-1,1,-1,1,-1] == funlistOut <<res
		
		Closure f2C = f {a, b ->
			(e(a-b) << e(b-a) << empty())
		}
		def res2 = m.foldLM(f2C, 0) << funlistIn << [1,2,3]
		assert [-6,6,0,0,-4,4,-2,2] == funlistOut << res2
		
	}

	void testJoin() {
        BaseM m = BaseM.getInstance(FunListMonad.instance)		
		def list = funlistIn << [1,3,7,8]
		def list2 = funlistIn << [4,5,3]
		def megaList = funlistIn << [list, list2]
		
        def joinList = m.join (megaList)
		def groovyL =  funlistOut << joinList
		assert groovyL == [1,3,7,8, 4,5,3]
	}

		//TODO
	void testAp() {		
		BaseM m = BaseM.getInstance(FunListMonad.instance)
		def list = funlistIn << [1,3]
		def ops = funlistIn << [PLUS(1),TIMES(2)]
		
		def resList = m.ap (ops, list)
		def groovyL =  funlistOut << resList
		assert groovyL == [2,4,2,6]
	}

	void testMfilter() {
		BaseM m = BaseM.getInstance(FunListMonad.instance)
		def list = funlistIn <<[1,3,4,5]
		def x= funlistOut << list
		FunList filtered = m.mfilter(SMALLER(4)) << list
		assert [1,3] == funlistOut << filtered
		
        FunList filtered2 = mfilter(SMALLER(4)) << list
		assert [1,3] == funlistOut << filtered2
	}
 
}
