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
import static fpig.funlist.functions.FunListMonad.*
import static fpig.common.functions.FromOperators.*
import static fpig.common.functions.Projections.*
import fpig.common.functions.impl.FpigMonads;
import fpig.common.types.ClosureHolder;
import fpig.concepts.FunctorDescription;
import fpig.concepts.MonadAsApplicative;
import fpig.concepts.MonadDescription;
import fpig.funlist.functions.FunListMonad;
import fpig.funlist.functions.Infinity;
import fpig.funlist.types.FunList;
import fpig.funlist.types.LazyList;
import fpig.monad.functions.BaseM;
import groovy.lang.Closure;


/**
 * Shows examples of functional syntax converted to Groovy/Java Lists.
 *
 * Functor working under the hood is not shown.
 * 
 * @author Robert Peszek
 */
class MonadicComposition_ExpoTests extends GroovyTestCase{
   
	void testAndShowTossACoin(){
	   //heads =1, tails=-1, for heads player gets a dollar, tails player looses one dollar.
	   //after 4 tosses, what is the likelihood of being $2 or more ahead
	   
	   // Closure toss = { x-> e(x+1) << e(x-1) << empty()} //alternative first line
	   def p = FunListMonad.instance.pure
	   Closure toss = { x-> f([x+1, x-1])}  
	   
	   def after4 = b(toss) << b(toss) << b(toss) << b(toss) << p(0)
	   def res = (length << filter (LARGER(1)) <<after4) / (length << after4)
	   assert res + 0.01 > 5/16 && res - 0.01 < 5/16 
	   
	   //better code allows for chains of arbitrary length
	   def composeNtimes = f {n, Closure c -> reduceR (COMPOSE) << take(n) << repeat( c )}
	   def resAfterN = {n-> 
		   def afterN = composeNtimes(n, b(toss)) << p(0)
		   (length << filter (LARGER(1)) << afterN) / (length << afterN) 
	   }
	   assert resAfterN(4) + 0.01 > 5/16 && resAfterN(4) - 0.01 < 5/16
	   
	   //alternative using foldLM
	   BaseM m = BaseM.getInstance(FunListMonad.instance)
	   def foldNTosses = f {n -> m.foldLM (apply, 0) << take(n) << repeat(toss)}
	   def resFoldNTosses = {n->
	         def afterN = foldNTosses(n)
			 (length << filter (LARGER(1)) << afterN) / (length << afterN)
	   }
	   assert resFoldNTosses(4) + 0.01 > 5/16 && resAfterN(4) - 0.01 < 5/16
   }
	
	/**
	 * Non-deterministic computation returning all possible positions of a knight on chess board without boundaries
	 * @return
	 */
   Closure getKnightMove() {
	   FunList possiblePositions = f([[1,2],[2,1],[-1,2],[2,-1],[1,-2],[-2,1],[-1,-2],[-2,-1]])
	   Closure vplus = f {a, b -> [a[0] + b[0], a[1] + b[1]]}
	   assert vplus([2,3], [-1,1]) == [1,4]
	   def stillOnBoard  = {v -> 
		   def (x,y) = v
		   x in 0..7 && y in 0..7
	   }
	   return {v -> filter(stillOnBoard) << map(vplus(v)) << possiblePositions}
   }
   
   void testAndShowListAsMonad_knightExample() {
	   //ininite check board, find all positions where knight can get in 3 moves
       //we use groovy lists [x, y] for checkboard position
	   def p = FunListMonad.instance.pure
	   Closure vdistanceSq = f {a, b -> (a[0] - b[0])**2 + (a[1] - b[1])**2}
	   	   
	   def in3moves =  b(knightMove) << b(knightMove) << b(knightMove) << p([0,0]) 
	   
	   //verify that we can get to position [6,3] in 3 moves.
	   assert 0 ==  reduceL(MIN) << map(vdistanceSq([6,3])) << in3moves
	   
	   //more generic way by using reduceR to compose arbitrary number of knightMove functions
	   def composeNtimes = f {n, Closure c -> reduceR (COMPOSE) << take(n) << repeat( c )}
	   def inNmoves = f {n, initVal -> composeNtimes(n,  b(knightMove)) << p(initVal)}
	   
	   assert 0 == reduceL(MIN) << map(vdistanceSq([6,3])) << inNmoves(3) << [0,0]
	   
	   //another generic version using foldLM (left monadic fold)
	   BaseM m = BaseM.getInstance(FunListMonad.instance)
	   def foldNMoves = f {n -> m.foldLM (apply, [0,0]) << take(n) << repeat(knightMove)}

	   assert 0 == reduceL(MIN) << map(vdistanceSq([6,3])) << foldNMoves(3)
  }
  
  def getKnightPath() {
	   //path is a List of moves, latest moves are in front.
	   //logical signature: [Position] -> [[Position]]
	   return {path -> 
		   if(isEmpty(path)) {
		      f [empty()] //equivalent of e(empty()) << empty() //returns one element list: [empty]
		   } else {
			   def position = head(path)
			   FunList nextPos = knightMove(position)
			   map(prepend(_, path), nextPos)
		   }
	   }
  }
	 
  def getPathLeadsTo() {
	  return f {v, path ->
		  if(isEmpty(path))
			  false
		  else {
			   v == head(path)
		  }
	  }
  }  
   
  void testAndShowListAsMonad_knightExample2() {
	  //this code will remember all 'paths' which lead from one position to another
	  def p = FunListMonad.instance.pure
	  
	  def initialPath = p(p([0,0]))
	  //List of Lists with each elementen being a path
	  def in3movesAllPaths =  b(knightPath) << b(knightPath) << b(knightPath) << initialPath
	  assert false == isEmpty << filter(pathLeadsTo([6,3])) << in3movesAllPaths	
	  
	  //more generic way by using reduceR to compose arbitrary number of knightMove functions
	  def composeNtimes = f {n, Closure c -> reduceR (COMPOSE) << take(n) << repeat( c )}
	  def inNmovesAllPaths = f {n -> composeNtimes(n,  b(knightPath)) <<initialPath}
	  
	  assert  false == isEmpty << filter(pathLeadsTo([6,3])) << inNmovesAllPaths(3)
	  println funlistOut << head << filter(pathLeadsTo([6,3])) << inNmovesAllPaths(3)
	  
	  //another generic version using foldLM (left monadic fold)
	  BaseM m = BaseM.getInstance(FunListMonad.instance)
	  def foldNMoves = f {n -> m.foldLM (apply, p([0,0])) << take(n) << repeat(knightPath)}

	  assert  false == isEmpty << filter(pathLeadsTo([6,3])) << foldNMoves(3)	  
	  println funlistOut << head << filter(pathLeadsTo([6,3])) << foldNMoves(3)	  
  } 
  
  void testAndShowListAsMonad_knightExample3() {
	  //what is the shortest sequence of moves of knight from position [1,1] to [6,5]?
	  def p = FunListMonad.instance.pure
	  def initialPath = p([1,1])
	  def destination = [6,5]
	  BaseM m = BaseM.getInstance(FunListMonad.instance)
	  def stoppingRule = {pathList -> !(isEmpty << filter(pathLeadsTo(destination)) << pathList) }
	  def shortestPaths =  filter(pathLeadsTo(destination)) << m.foldLMUntil(stoppingRule, apply, initialPath) << repeat(knightPath)
	  assert  0 < length << shortestPaths
	  println funlistOut << head << shortestPaths
  }
 
  void testAndShowListAsMonad_asApplicative() {
	  BaseM m = BaseM.getInstance(FunListMonad.instance)
	  
      //add elements from both lists
	  def res = m.ap2 (f([PLUS]), f([1,2,3]), f([4,5]))
	  assert [5,6,6,7,7,8] == funlistOut << res
	  
	  //add and then multiply elements from both lists
	  def res2 = m.ap2 (f([PLUS, TIMES]), f([1,2,3]), f([4,5]))
	  assert [5,6,6,7,7,8,4,5,8,10,12,15] == funlistOut << res2
	  
	  def res3 = m.apN(f([PLUS, TIMES]), [f([1,2,3]), f([4,5])])
	  assert [5,6,6,7,7,8,4,5,8,10,12,15] == funlistOut << res3

	  def res4 = m.apN (f([PLUS, TIMES]), f([ f([1,2,3]), f([4,5])]))
	  assert [5,6,6,7,7,8,4,5,8,10,12,15] == funlistOut << res4
  }
  
  //See MonadicComprehensions_ExpoTest for better ClosureMonad examples 
  void testAndShowClosuresAsMonads() {
	  //fH is a closure holder, this disambiguates << operator, but you can think that this function returns a closure 
	 def testC = {x -> fH({x+1}) }
	 
	 ClosureHolder res = b(testC) << b(testC) << fH({5})
	 assert res.cVal() == 7 
  }
  
  
  void testAndShowCreateYourOwnMonad() {
	  
	  MonadDescription m = new MonadDescription(){
		  public Closure getPure() {
			  {v -> new MyBox(v)}
		  }
  
		  public Closure getBind() {
              {Closure fn, MyBox mb -> fn(mb.value)}
		  }		  
	  }	  
	  FpigMonads.configureMonad(MyBox.class, m)
	  
	  Closure myBoxingFunction = { int v ->
		  new MyBox(v+1)
	  }
	  
	  def res = b(myBoxingFunction) << b(myBoxingFunction) << m.pure(1)
	  
	  assert res instanceof MyBox
	  
	  def unBoxedValue
	  unpure(res){
		  unBoxedValue = it
	  }
	  assert unBoxedValue == 3
  }
  
  class MyBox {
	  MyBox(v){
		  value = v
	  }
	  def value
  }

}
