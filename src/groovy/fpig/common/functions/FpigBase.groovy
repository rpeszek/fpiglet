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
package fpig.common.functions

import fpig.common.functions.impl.FpigEitherBase;
import fpig.common.functions.impl.FpigMaybeBase;
import fpig.common.functions.impl.FpigMonads;
import fpig.common.types.ClosureHolder;
import fpig.concepts.MonadDescription;
import fpig.expressions.MComprehensionSelectSyntax;
import fpig.expressions.MComprehensionSyntax;
import fpig.groovylist.asfunlist.functions.InAndOutOfFunLists;
import fpig.funlist.types.FunList;
import fpig.util.CallUtil
import fpig.util.curring.Parameter_;
import groovy.lang.Closure;

/**
 * Collection of 'base' functions and methods.
 * <p>
 * FpigBase defines {@code f} transform for converting Groovy concepts to functional concepts (currently works with Lists and Closures).
 * <p>
 * FpigBase defines a set of 'polymorphic' monadic functions, which work across several functional types. The following FpigBase
 * functions work across all monads defined or configured with Fpiglet:
 * <pre>
 *  b 
 *  bind
 *  mfilter 
 *  unpure 
 *  asUnpure
 * </pre>
 * The following methods work across all monads:
 * <pre>
 * comprehend
 * select
 * selectP
 * </pre> 
 * FpigBase defines functions for interacting with {@link fpig.common.types.Maybe}
 * <pre>
 *  Maybe maybe4 = just(4) //wraps 4 in Maybe context
 *  Maybe maybeNothing = nothing() //returns Nothing version of Maybe
 *  unjust(maybe4) //unwraps 4
 *  isSomething(maybe4) //checks if Maybe is Just or Nothing
 *  unpure(maybe4) {
 *    assert it==4
 *  }  //note unpure serves as generalized version of unjust
 * </pre> 
 * FpigBase defines functions for interacting with {@link fpig.common.types.Either}
 * <pre>
 * Either success = right(5) //wraps 5 in Either context
 * Either error = left('this is wrong')
 * assert isRight(success)
 * assert !isRight(error)
 * assert 5 = unright(success)
 * unpure(success){
 *   assert it == 5
 * } //note unpure serves as generalized version of unright
 * </pre> 
 * 
 * Other conveniences include '_' definition, {@code fH ClosureHolder} transformation, as well as flip and apply functions.
 *   
 * @see fpig.common.types.Either
 * @see fpig.common.types.Maybe  
 * @see fpig.concepts.MonadDescription
 * @author Robert Peszek
 */
class FpigBase {
	
	/**
	 * Converts Closure to Curried Function.
	 * <p>
	 * One of very few methods in Fpiglet. It is a method to take advantage of method overloading.
	 *  
     * <p> Wiki:
	 * <br> <a href="http://code.google.com/p/fpiglet/">http://code.google.com/p/fpiglet/</a>
	 * <br> <a href="http://code.google.com/p/fpiglet/wiki/CurriedFunctions">http://code.google.com/p/fpiglet/wiki/CurriedFunctions</a>
	 * @see fpig.util.CallUtil
	 * @param c - Groovy closure
	 * @return - curried function version of the closure.
	 */
	static Closure f(Closure c) {
		CallUtil.toFunction(c)
	}
	
	/**
	 * Converts Groovy List ot functional FunList
	 * <p>
	 * One of very few methods in Fpiglet. It is a method to take advantage of method overloading.
	 * 
	 * @see fpig.funlist.types.FunList 
	 * @param l - List
	 * @return FunList
	 */
	static FunList f(List l) {
		(InAndOutOfFunLists.funlistIn << l)
	}

	/**
	 * Returns Closure Holder (convenience holder, used for passing closures as arguments with {@code <<} syntax).
	 * Closure Holders are also used
	 * to define {@link fpig.common.functions.ClosureMonad}.
	 */
	static ClosureHolder fH(Closure c){
		return new ClosureHolder(c)
	}
	
	/**
	 * Placeholder parameter used in defining partially applied curried functions.
	 * <p>
	 * Useful if program needs to partially apply parameter which is not first on the list.
	 * <p>
	 * Example:
	 * <pre>
	 *  POWER(_,2) //squares numbers
	 * </pre>
	 */
	static def _ = CallUtil._

	/**
	 * Flips first 2 function arguments.
	 * <p>
	 * Example 
	 * <pre>
	 * def c = {a, b -> a}
	 * assert c(2,5) == 2 
	 * assert flip(c)(2,5) == 5 
     * </pre>
     * Logical signature:
     * <pre>
     *   (a->b) -> b->a
     * </pre>
	 * @see fpig.util.CallUtil
	 */
	static flip = CallUtil.flip

	/**
	 * Reversed function application.
	 * <p>
	 * What is 5, is 5 a function?  Well if {@code f(5)} produces a value then who is to say that f is a function and 5 is a value?
	 * Maybe it is the other way around?  {@code apply} make this happen:
	 * <pre>
	 *   apply(5,f) //is the same as f(5)
	 *   def function5 = apply(5)
	 *   function5(f) //is the same as f(5)
	 * </pre>
	 * 
	 * @see fpig.util.CallUtil
	 */
	static Closure apply = f(CallUtil.functionApplicationFlipped)

	/**
	 * Identity Closure.
	 * <pre>
	 *  ID(x) //same as x
	 * </pre>
	 */
	static Closure ID = Closure.IDENTITY

	/**
	 * Polymorphic version of monadic bind.
	 * <p>	
	 * If you define your own monad, you need to configure Fpiglet for it using {@link fpig.common.functions.impl.FpigMonads#configureMonad(Class, fpig.concepts.MonadDescription)}.
	 * <p>
	 * Logical signature same as monadic bind.
	 * @see fpig.concepts.MonadDescription
	 * @see fpig.common.functions.impl.FpigMonads
	 */
	static Closure bind = FpigMonads.bind

	
	/**
	 * Same as bind.
	 * @see #bind
	 */
	static Closure b = FpigMonads.bind

	
	/**
	 * Polymorphic version of unpure monadic method.
	 * <p>	
	 * If you define your own monad, you need to configure Fpiglet for it using {@link fpig.common.functions.impl.FpigMonads#configureMonad(Class, fpig.concepts.MonadDescription)}.
	 * <br>
	 * See {@link fpig.monad.functions.BaseM#getUnpure()} for detailed description.
	 * 
	 * @see fpig.monad.functions.BaseM#getUnpure()
	 * @see fpig.common.functions.impl.FpigMonads
	 */
	static Closure unpure = FpigMonads.unpure

	/**
	 * Flipped version of unpure. Useful when injecting into monadic composition.
	 * <p>	
	 * If you define your own monad, you need to configure Fpiglet for it using {@link fpig.common.functions.impl.FpigMonads#configureMonad(Class, fpig.concepts.MonadDescription)}.
	 *
	 * @see fpig.monad.functions.BaseM#getUnpure()
	 */
	static Closure asUnpure = FpigMonads.asUnpure
	
	/**
	 * Polymorphic mfilter working across all monads (as long as monad is MonadWithEmptyDescription).
	 * @see fpig.monad.functions.BaseM#getMfilter()
	 */
	static Closure mfilter = FpigMonads.mfilter

	/**
	 * Shortcut to monadic comprehension 'comprehend' syntax.
	 *
     * <p> Wiki:
	 * <br> <a href="http://code.google.com/p/fpiglet/wiki/MonadicComprehensions">http://code.google.com/p/fpiglet/wiki/MonadicComprehensions</a>
	 */
	static def comprehend(Closure c) {
		 MComprehensionSyntax.comprehend(c)
	}
	
	/**
	 * Shortcut to monadic comprehension 'comprehend' syntax for unregistered monad.
	 * 
     * <p> Wiki:
	 * <br> <a href="http://code.google.com/p/fpiglet/wiki/MonadicComprehensions">http://code.google.com/p/fpiglet/wiki/MonadicComprehensions</a>
	 */
	static def comprehend(MonadDescription m, Closure c) {
		 MComprehensionSyntax.comprehend(m, c)
	}

	/**
	 * Shortcut to monadic comprehension 'select' syntax.
	 * 
     * <p> Wiki:
	 * <br> <a href="http://code.google.com/p/fpiglet/wiki/MonadicComprehensions">http://code.google.com/p/fpiglet/wiki/MonadicComprehensions</a>
	 */
	static def select(Closure c) {
		 MComprehensionSelectSyntax.select(c)
	}


	/**
	 * Shortcut to monadic comprehension 'selectP' (Pure) syntax.
	 *
     * <p> Wiki:
	 * <br> <a href="http://code.google.com/p/fpiglet/wiki/MonadicComprehensions">http://code.google.com/p/fpiglet/wiki/MonadicComprehensions</a>
	 */
	static def selectP(Closure c) {
		 MComprehensionSelectSyntax.selectP(c)
	}

	/**
	 * Wraps value in Maybe (Just) context.
	 * <p>
	 * Logical signature
	 * <pre>
	 * just:: A -> Maybe&lt;A&gt;
	 * </pre>
	 * {@code T t; just(t)} returns {@code Just<T>}
	 * 
	 * @see fpig.common.types.Maybe
	 * @see fpig.common.functions.MaybeMonad
	 */
	static Closure just = FpigMaybeBase.just	
	
	/**
	 * Returns Nothing version of Maybe.
	 * <p>
	 * Logical signature
	 * <pre>
	 * unjust:: _ -> Maybe
	 * </pre>
	 * Accepts any number of arguments.
	 * @see fpig.common.types.Maybe
	 * @see fpig.common.functions.MaybeMonad
	 */
	static Closure nothing = FpigMaybeBase.nothing
	
	/**
	 * Unwraps content of Maybe
	 * <p>
	 * Logical signature
	 * <pre>
	 * unjust:: Maybe&lt;A&gt; -> A
	 * </pre>
	 * will throw RuntimeException if called on Nothing.
	 * @see fpig.common.types.Maybe
	 * @see fpig.common.functions.MaybeMonad
	 */
	static Closure unjust = FpigMaybeBase.unjust
	
	/**
	 * Returns true if Maybe is Just and false if it is Nothing.
	 * <p>
	 * Logical signature
	 * <pre>
	 * Maybe&lt;A&gt; -> boolean
	 * </pre>
	 * @see fpig.common.types.Maybe
	 * @see fpig.common.functions.MaybeMonad
	 */
	static Closure isSomething = FpigMaybeBase.isSomething
	
	
	/**
	 * Wraps value in Either context. Typically used to wrap successful result value.
	 * <p>
	 * Logical signature
	 * <pre>
	 *  A -> Either&lt;_, A&gt;
	 * </pre>
	 * @see fpig.common.types.Either
	 * @see fpig.common.functions.EitherMonad
	 */
	static Closure right = FpigEitherBase.right
	
	/**
	 * Returns Left Either value. Typically used to return error information.
	 * <p>
	 * Logical signature
	 * <pre>
	 *  E -> Either&lt;E, _&gt;
	 * </pre>
	 * @see fpig.common.types.Either
	 * @see fpig.common.functions.EitherMonad
	 */
	static Closure left = FpigEitherBase.left
	
	/**
	 * If Either is Right returns true, if it is Left returns false. Can be used to check if Either value contains successful result.
	 * <p>
	 * Logical signature:
	 * <pre>
	 *   Either&lt;E,A&gt; -&gt; boolean
	 * </pre>
	 * @see fpig.common.types.Either
	 * @see fpig.common.functions.EitherMonad
	 */
	static Closure isRight = FpigEitherBase.isRight
	
	/**
	 * Unwraps Right value from Either.
	 * <p>
	 * Will throw RuntimeException if Either is Left.
	 * Logical signature:
	 * <pre>
	 *   Either&lt;E,A&gt; -> A
	 * </pre>
	 * @see fpig.common.types.Either
	 * @see fpig.common.functions.EitherMonad
	 */
	static Closure unright = FpigEitherBase.unright
	
	/**
	 * Unwraps Left value from Either.
	 * <p>
	 * Will throw RuntimeException if Either is Right.
	 * Logical signature:
	 * <pre>
	 *   Either&lt;E,A&gt; -> E
	 * </pre>
	 * @see fpig.common.types.Either
	 * @see fpig.common.functions.EitherMonad
	 */
	static Closure unleft = FpigEitherBase.unleft
}
