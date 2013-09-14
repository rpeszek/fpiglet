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
import static fpig.groovylist.asfunlist.functions.InAndOutOfFunLists.*
import static fpig.funlist.functions.BaseFL.*
import static fpig.funlist.functions.Infinity.*
import static fpig.common.functions.FromOperators.*
import static fpig.common.functions.Projections.*
import test.utils.GruesomePlus;
import fpig.applicative.functions.BaseA;
import fpig.common.functions.MaybeMonad;
import fpig.common.types.Maybe;
import fpig.concepts.ApplicativeDescription;
import fpig.concepts.FunctorDescription;
import fpig.concepts.MonadAsApplicative;
import fpig.concepts.MonadDescription;
import fpig.funlist.functions.FunListMonad;
import fpig.funlist.functions.FunListZipApplicative;
import fpig.funlist.functions.Infinity;
import fpig.funlist.types.FunList;
import fpig.funlist.types.LazyList;
import fpig.monad.functions.BaseM;
import groovy.lang.Closure;
import groovy.xml.streamingmarkupsupport.BaseMarkupBuilder;
import gruesome.Gruesome;

/**
 * 
 * TODO use greusome/QuickCheck for these tests
 * @author Robert Peszek
 *
 */
class Functor_Applicative_Monad_Laws_ExpoTests extends GroovyTestCase{
				
	/**
	 * Tests functor laws:
	 * <pre>
	 * {@code
	 * fmap(ID) '==' ID
	 * fmap(f << g) '==' fmap(f) << fmap(g)
	 * }
	 * </pre>
	 * 
	 */
	void testFunctorLaws_usingMaybe() {
		MonadDescription maybeM = MaybeMonad.instance
		FunctorDescription mf = new MonadAsApplicative(maybeM)
		
		//law 1: fmap(ID) == ID
		def mapped_ID = mf.fmap(ID)
		assert just(5) == mapped_ID(just(5))
		
		//law 2: fmap(f << g) == fmap(f) << fmap(g)
		def fc = PLUS(3)
		def gc = TIMES(2)
		def composed = fc << gc
		assert composed(5) ==  3 + (2*5)
		def res2a =  mf.fmap(composed)
		def res2b =  mf.fmap(fc) << mf.fmap(gc)
		assert res2a(just(5)) == res2b(just(5))
 		assert just(3 + (2*5)) == res2a(just(5))		
	}
 
	void testFunctorLaws_usingMaybe_gruesome() {
		MonadDescription maybeM = MaybeMonad.instance
		FunctorDescription mf = new MonadAsApplicative(maybeM)
		Closure genMaybes = GruesomePlus.genMaybes(Gruesome.genInt)
		
		//law 1: fmap(ID) == ID
		def firstLaw = {Maybe x -> x == mf.fmap(ID)(x)}
		GruesomePlus.assertForAll (firstLaw, [genMaybes])
		
		//law 2: fmap(f << g) == fmap(f) << fmap(g)
		def fc = PLUS(3)
		def gc = TIMES(2)
		def secondLaw = {Maybe x->  mf.fmap(fc << gc)(x) == mf.fmap(fc) << mf.fmap(gc) << x}
		GruesomePlus.assertForAll (secondLaw, [genMaybes])
		
		//this test tries to randomize both data and functions to be tested
		Closure genTest = {[fc:GruesomePlus.genPolynomial(GruesomePlus.genInt), gc:GruesomePlus.genPolynomial(GruesomePlus.genInt), x:genMaybes()]}
		def secondLaw_ = {Map t ->  mf.fmap(t.fc << t.gc)(t.x) == mf.fmap(t.fc) << mf.fmap(t.gc) << t.x}
		GruesomePlus.assertForAll (secondLaw_, [genTest])
	}

	/**
	 * Tests applicative laws: 
	 * <pre>
	 * {@code 
	 * ap(pure(f), ax) '==' fmap(f, ax)
	 * ap(pure(ID), ax) '==' ax
	 * ap(pure(f), pure(x) '==' pure(f x)
	 * ap(af, pure(x)) '==' ap(pure(FpigBase.apply(x)), af)
	 * }
	 * </pre>
	 * where f is regular function, x is regular value, ax is applicative (converted) value, and af is applicative (converted) function.
	 * So if applicative is FunList (FunListZipApplicative) then fa would be list of functions, and ax would be list of values.
	 */
	void testAndShowApplicativeLaws_usingFunListZip() {
		ApplicativeDescription fa = FunListZipApplicative.instance
		Closure fc = PLUS(5);		
		FunList ax = f( [1,2,3])
		def x = 3
		
		//BaseA a = BaseA.getInstance(fa)
		
		//Law 1: ap(pure(f), ax) == fmap(f, ax) where f:a->b, ax is applicative value
		def res1a = fa.ap(fa.pure(fc), ax)
		def res1b = fa.fmap(fc, ax)
		
		assert funlistOut << res1a == funlistOut << res1b
		assert funlistOut << res1a == [6,7,8]
		
		//Law 2: ap(pure(ID), ax) == ax
		def res2a = fa.ap(fa.pure(ID), ax)	
		def res2b = ax
		
		assert funlistOut << res2a == funlistOut << res2b	
		assert funlistOut << res2a == [1,2,3]
		
		//Law 3: ap(pure(f), pure(x) == pure(f x)
		def res3a = fa.ap(fa.pure(fc), fa.pure(x))
		def res3b = fa.pure(fc(x))
		
		//note these are both infinite sequences
		assert funlistOutTake(3) << res3a == funlistOutTake(3) << res3b
		assert funlistOutTake(3) << res3a == [8,8,8]
		
		//Law 4: ap(af, pure(x)) == ap(pure(FpigBase.apply(x)), af)  //where af is applicative of a function
		FunList af = f( [fc, fc, fc])
		def res4a = fa.ap(af, fa.pure(x))
		def res4b = fa.ap(fa.pure(apply(x)), af)
		
		assert funlistOut << res4a == funlistOut << res4b
		assert funlistOut << res4a == [8,8,8]	
	}
	
	
	/**
	 * Tests monad laws: 
	 * <pre>
	 * bind(f, pure(x)) '==' f x //(or b(f) << pure(x) '==' f(x)) (Left identity)
	 * bind(pure, mx) '==' mx  //(or b(pure) << mx '==' mx) (Right identity)
	 * bind(f, bind(g, mx)) '==' bind({x -> (bind(f, g(x))}, mx) //or b(f) << b(g) << mx '=='  b({x -> (b(f) << g) (x)}) (mx)  (Associativity) 
	 * </pre>
	 * where f is regular function, x is regular value, ax is applicative (converted) value, and af is applicative (converted) function.
	 * So if applicative is FunList (FunListZipApplicative) then fa would be list of functions, and ax would be list of values.
	 */
	void testAndShowMonadLaws_usingFunListMonad(){
		MonadDescription m = FunListMonad.instance
		BaseM fm = BaseM.getInstance(m)
		def fc = {a -> f([-a,a])}
		def gc = {a -> f([a, a + 10])}
		def x = 3
		def mx = f([1,2,3])
		
		//Left identity Law:	 bind(f, pure(x)) == f x //(or b(f) << pure(x) == f(x))
		def res1a = m.bind(fc, m.pure(x))
		def res1b = fc(x)
		assert funlistOut << res1a == funlistOut << res1b
		assert funlistOut << res1a == [-x,x]
		
		def res1a_ = b(fc) << m.pure(x)
		assert funlistOut << res1a_ == funlistOut << res1b
		
		
		//Right identity Law:	 bind(pure, mx) == mx  //(or b(pure) << mx == mx)
		def res2a = m.bind(m.pure, mx)
		def res2b = mx
		assert funlistOut << res2a == funlistOut << res2b
		assert funlistOut << res2a == [1,2,3]
		
		def res2a_ = b(m.pure) << mx
		assert funlistOut << res2a_ == funlistOut << res2b
		
		//Associativity Law: bind(f, bind(g, mx)) == bind({x -> (bind(f, g(x))}, mx)	
		//or b(f) << b(g) << mx ==  b({x -> (b(f) << g) (x)}) (mx) 
		def res3a = m.bind(fc, m.bind(gc, mx))
		def res3b = m.bind({_x-> m.bind(fc, gc(_x))}, mx)
         
		assert funlistOut << res3a == funlistOut << res3b
		assert funlistOut << res3a == [-1,1,-11,11,-2,2,-12,12,-3,3,-13,13]

		def res3a_ = b(fc) << b(gc) << mx
		def res3b_ = b({_x -> (b(fc) << gc) (_x)}) (mx)
		 
		assert funlistOut << res3a_ == funlistOut << res3b_
		assert funlistOut << res3a_ == [-1,1,-11,11,-2,2,-12,12,-3,3,-13,13]

	}
}
