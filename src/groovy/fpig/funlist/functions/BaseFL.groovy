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
package fpig.funlist.functions

import fpig.util.CallUtil
import fpig.common.functions.impl.FpigMaybeBase;
import fpig.common.types.Nothing;
import fpig.funlist.types.FunList
import fpig.funlist.functions.impl.*

/**
 * Base functional list (FunLists) library.
 * <p>
 * This documentation uses Logical signatures. 
 * <p> Wiki:
 * <br> <a href="http://code.google.com/p/fpiglet/wiki/ListFunctions">http://code.google.com/p/fpiglet/wiki/ListFunctions</a>
 * <br> <a href="http://code.google.com/p/fpiglet/wiki/LogicalSignatures">http://code.google.com/p/fpiglet/wiki/LogicalSignatures</a>
 * @see fpig.funlist.types.FunList
 * @author Robert Peszek
 */
class BaseFL {
	
	/**
	 * Returns Empty List
	 * <p>
	 * Logical signature
	 * <pre>
	 * empty ::  -> []
	 * </pre>
	 * @return FunList - 'the' EmptyList (there is only one!)
	 */
	static Closure empty =  { -> FunList.EMPTYLIST}
	
	/**
	 * Verifies if list is empty
	 * <p>
	 * Logical signature
	 * <pre>
	 * isEmpty :: [a] -> boolean
	 * </pre>
	 * @param FunList 
	 * @return boolean
	 */
	static Closure isEmpty = CallUtil.toFunction {FunList l -> l.empty}

	/**
	 * Returns list head. Throws runtime exception (EmptyListException) if called on empty list.
	 * <p>
	 * {@link #headM} alternative returns Maybe
	 * <p>
	 * Logical signature
	 * <pre>
	 * head :: [a] -> a
	 * </pre>
	 * @param  * @return A
	 */
	static Closure head = CallUtil.toFunction {FunList l -> 
		if(l.empty)
		    throw new EmptyListException("no head on empty list")
		else 
			l.head
	}
	
	/**
	 * Exception safe version of head function. Returns list Maybe head.
	 *  
	 * <p>
	 * Logical signature
	 * <pre>
	 * headM :: [a] -> Maybe a
	 * </pre>
	 * @see fpig.common.types.Maybe
	 * @param FunList
	 * @return Maybe
	 */
	static Closure headM = CallUtil.toFunction {FunList l ->
		if(l.empty)
			FpigMaybeBase.nothing()
		else
			FpigMaybeBase.just(l.head)
	}

	
	/**
	 * Returns list tail. Throws runtime exception (EmptyListException) if called on empty list.
	 * <p>
	 * Logical signature
	 * <pre>
	 * tail :: [a] -> [a]
	 * </pre>
	 * @param FunList 
	 * @return FunList tail
	 */
	static Closure tail = CallUtil.toFunction {FunList l -> 
		if(l.empty)
			throw new EmptyListException("no tail on empty list")
		else
			l.tail
	}

	/**
	 * Exception safe version of tail function. Returns list Maybe tail.
	 * <p>
	 * Logical signature
	 * <pre>
	 * tailM :: [a] -> Maybe [a]
	 * </pre>
	 * @param FunList 
	 * @return Maybe FunList 
	 */
	static Closure tailM = CallUtil.toFunction {FunList l -> 
		if(l.empty)
			FpigMaybeBase.nothing()
		else
			FpigMaybeBase.just(l.tail)		
	 }

	
	/**
	 * Returns length of a (finite) list
	 * <p>
	 * Word of caution: it maybe tempting to use this function to verify that list is not empty. 
	 * This is bad idea from performance point of view.  Use {@link #isEmpty} instead!
	 * <p>
	 * Logical signature
	 * <pre>
	 *  length :: [a] -> long
	 * </pre>
	 * Stack Overflow safe. Implementation uses trampoline.
	 * @param FunList 
	 * @return long
	 */
	static Closure length = Length.length

	
	/**
	 * Verifies if two (finite) lists have the same elements.
	 * <p>
	 * FunList does not implement Java collection equals contract. 
	 * The reason for it is that it is hard to know if comparison of 2 lists can be accomplished 
	 * in finite time.
	 * <p>
	 * Logical signature
	 * <pre>
	 *  eq :: [a] -> [a] -> boolean
	 * </pre>
	 *  Stack Overflow safe. Implementation uses trampoline.
	 * @param FunList
	 * @param FunList
	 * @return boolean
	 */
	static Closure eq = Equal.eq

	/**
	 * Returns list from n to m, this assumes only that: 
	 * <ul> <li> m implements {@code comparedTo} so that  m &lt; n resolves to false at one point, 
	 * <li> n implements {@code next()} so {@code ++n } returns incremented values.
	 * </ul>
	 * <p>
	 * Logical signature
	 * <pre>
	 *  range :: (a implements compareTo and next) =>  a -> a -> [a]
	 * </pre>
	 * @param A - n (implements next)
	 * @param B - m (implements compareTo)
	 * @return FunList
	 */
	static Closure range = Range.range

	
	
	/**
	 * Returns concatenated (flattened) list, expects list of lists (either Groovy List of FunLists or FunList of FunLists).
	 * <p>
	 * Logical signature:
	 * <pre>
	 *  concat ::  [[a]] -> [a]
	 * </pre>
	 * @param  List of Lists - List of FunList or FunList of FunList 
	 * @return flattened FunList
	 */
	static Closure concat = Concat.concat
	
    /**
	 * Standard functional fold-left.
	 * <p>
	 * Logical signature:
	 * <pre>
	 *  foldL :: (a -> b -> a) -> a -> [b] -> a
	 * </pre>
	 *  Stack Overflow safe. Implementation uses trampoline.
	 *  Will not work with infinite lists (logical limitation).
	 * @param Closure - folding function accepting accumulator A and list elements B returning new accumulator A
	 * @param A - initial value
	 * @param FunList - 
	 * @return A - accumulated value
	 */
	static Closure foldL = FoldL.foldL

	/**
	 * Folds until stopping rule is met or until the end of the list.
	 * <p>
	 * Logical signature:
	 * <pre>
	 *  foldLUntil :: (a->boolean) -> (a -> b -> a) -> a -> [b] -> a
	 * </pre>
	 *  Stack Overflow safe. Implementation uses trampoline.
	 *  Will not work with infinite lists (logical limitation).
	 * @param Closure - stopping rule accepting accumulator A (previous folded value), returning boolean
	 * @param Closure - folding function accepting accumulator A and list elements B, returning new accumulator A
	 * @param A - initial value
	 * @param FunList
	 * @return A - final accumulated value
	 */
	static Closure foldLUntil = FoldL.foldLUntil

	/**
	 * Same as foldL only initialized with list head.
	 * <p>
	 * Logical signature:
	 * <pre>
	 *  reduceL :: (a -> b -> a) -> [b] -> a
	 * </pre>
	 * @param Closure - folding function accepting accumulator A and list elements B returning new accumulator A
	 * @param FunList 
	 * @return A - final accumulated value
	 */
	static Closure reduceL = FoldL.reduceL

	/**
	 * Version of right fold for finite lists.
	 * <p>
	 * Logical signature:
	 * <pre>
	 * foldR :: (b -> a -> a) -> a -> [b] -> a
	 * </pre>
	 * This is not an equivalent of {@code foldL << reverse} !
	 * <p>
	 * {@code foldR} is implemented as chained function composition.
	 * <p>
	 * Will stack overflow. 
	 * @param Closure - folding function accepting list elements B and accumulator A, returning new accumulator A
	 * @param A - initial value
	 * @param FunList
	 * @return A- final accumulated value
	 */
	static Closure foldR = FoldR.foldR

	/**
	 * Same as foldR, only does not require initialization.
	 * <p>
	 * Logical signature:
	 * <pre>
	 *  reduceR :: (b -> a -> a) -> [b] -> a
	 * </pre>
	 * @param Closure - folding function accepting list elements B and accumulator A, returning new accumulator A
	 * @param FunList 
	 * @return A- final accumulated value
	 */
	static Closure reduceR = FoldR.reduceR

	/**
	 * Version of fold which works with infinite lists
	 * <p>
	 * Logical signature:
	 * <pre>
	 * 	foldXR :: (b -> (_->a) -> a) -> a -> [b] -> a
 	 * </pre>
	 * Implemented by looking at foldR as chained function composition.
	 * <br>
	 * The trick is that the accumulator needs to be a closure to allow for function composition 
	 * chain to terminate.
	 * <br>
	 * Will stack overflow. 
	 * @param Closure - folding function accepting list elements B and a Closure which needs to be evaluated to retrieve A, returning new accumulator A
	 * @param A - initial value
	 * @param FunList
	 * @return A- final accumulated value
	 */
	static Closure foldXR = FoldXR.foldXR

	/**
	 * Same as foldXR, only without initialization.
	 * <p>
	 * Logical signature:
	 * <pre>
	 *  reduceXR :: (b -> (_->a) -> a) -> [b] -> a
	 * </pre>
	 */
	static Closure reduceXR = FoldXR.reduceXR


	/**
	 * Standard filter function
	 * <p>
	 * Logical signature:
	 * <pre>
	 * filter :: (a->boolean) -> [a] -> [a]
	 * </pre>
	 *  Stack Overflow safe. Implementation uses combination of lazy recursion and trampolines.
	 *  Works on infinite lists.
	 *  @param Closure - predicate accepting list elements A, returning boolean
	 *  @param FunList
	 *  @return FunList
	 */
	static Closure filter = Filter.filter


	/**
	 * Standard map function
	 * <p>
	 * Logical signature
	 * <pre>
	 * map :: (a->b) -> [a] -> [b]
	 * </pre>
	 * Stack Overflow safe. Implementation uses lazy recursion.
	 * <br>
	 * Works on infinite lists
	 *  @param Closure - mapping function accepts list elements A, returning B
	 *  @param FunList
	 *  @return FunList
	 */
	static Closure map = Map.map

	
	/**
	* Standard take function. Returns specified number of elements from the beginning of the list or the whole list if the list has fewer elements.
	* <p>
	* Logical signature
	* <pre>
	* take :: Number -> [a] -> [a]
	* </pre>
	* Stack Overflow safe. Implementation uses lazy recursion.
	 * <br>
	* Works on infinite lists
	* @param Number n
	* @param FunList
	* @return FunList
	*/
    static Closure take = Take.take

	/**
	* Standard takeWhile function. Returns list elements while predicate is satisfied, stops as soon as the predicate returns false.
	* <p>
	* Logical signature
	* <pre>
	* takeWhile :: (a->boolean) -> [a] -> [a]
	* </pre>
	* Stack Overflow safe. Implementation uses lazy recursion.
	* <br>
	* Works on infinite lists
	* @param Closure - predicate function
	* @param FunList
	* @return FunList
	*/
	static Closure takeWhile = Take.takeWhile

	/**
	* Standard drop function. Returns tail of list after dropping specified number {@code n} of first elements.
	* Will return empty list if {@code n} is larger than list length.
	* <p>
	* Logical signature
	* <pre>  
	* drop :: Number -> [a] -> [a]
	* </pre>
	* Stack Overflow safe. Implementation uses trampoline.
	* <br>
	* Works on infinite lists.
	* @param Number n
	* @param FunList
	* @return FunList
	*/
	static Closure drop = Drop.drop

	/**
	* Standard dropWhile function. Returns tail of list removing elements up to the first occurrence where predicate returns false.
	* <p>
	* Logical signature
	* <pre>  
	* dropWhile :: (a->boolean) -> [a] -> [a]
	* </pre>
	* Stack Overflow safe. Implementation uses trampoline.
	* <br>
	* Works on infinite lists.
	* @param Closure - predicate function
	* @param FunList
	* @return FunList
	*/
	static Closure dropWhile = Drop.dropWhile

	/**
	 * Standard prepend function. Returns new list with element prepended in front.
 	 * <p>
	 * Logical signature:
	 * <pre>  
	 * prepend :: a -> [a] -> [a]
	 * </pre>
	 * Stack overflow safe.
	 * <br>
	 * Works on infinite lists.
	 * @param Generic Type A
	 * @param FunList A
	 * @return FunList A
	 */
	static Closure prepend = CallUtil.toFunction {a, FunList l -> 	
		l.build(a, { l })
	}

	//Derived Methods
	/** 
	 * Identical to {@code prepend}. Simplifies syntax for prepending with <<.
 	 * <p>
	 * Example:
	 * <pre>  
	 * e(0) << e(1) << e(2) << empty() //creates equivalent of [0,1,2]
	 * </pre>
	 * Logical signature:
 	 * <pre>  
	 * e :: a -> [a] -> [a]
	 * </pre>
	 */
	static Closure e = prepend
	
	/**
	 * Reverse function (reverses order of elements in the list)
 	 * <p>
	 * Logical signature
	 * <pre>  
	 * reverse :: [a] -> [a]
	 * </pre>
	 * Stack overflow safe.
	 * Obviously, will not work on infinite lists.
	 */
	static Closure reverse = foldL(CallUtil.flip(prepend), FunList.EMPTYLIST)
	
	/**
	 * zipWith function, accepts a function of 2 arguments and 2 lists, returns list combining elements of both lists by applying the function.
	 * to 'running' arguments from both lists. If list are not of equal sizes, the elements from the end of the longer list will be ignored.
 	 * <p>
 	 * Example:
 	 * <pre>
 	 * def list1 = f ([10, 1, 2])
 	 * def list2 = f ([1,2,3,4])
 	 * assert [11, 3, 5] == funlistOut << zipWith(PLUS, list1, list2)
	 * </pre>
	 * Logical Signature 
	 * <pre>  
	 * zipWith :: (a -> b -> c) -> [a] -> [b] -> [c]
	 * </pre>
	 * @param Closure - zip function accepting A, B returning C
	 * @param FunList 
	 * @param FunList 
	 * @return FunList 
	 */
	static Closure zipWith = Zip.zipWith
}
