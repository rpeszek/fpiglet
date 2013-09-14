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

	package fpig.funlist.mixins

import fpig.funlist.functions.BaseFL

import fpig.funlist.types.FunList

/**
* Internal Fpiglet use. Mixin used to add OO flavor to FunList. 
* <p>
* Fpilget may decide to drop support for OO interface to lists at one point.
* The list of methods in this class maybe incomplete.
* @author Robert Peszek
*/
class OoListOperationsPart {
	FunList prepend(t){
		BaseFL.prepend(t, this as FunList)
	}

	
    def filter(Closure predicate) {
        BaseFL.filter(predicate, this as FunList)
    }

	
    def map(Closure expr) {
        BaseFL.map(expr, this as FunList)
    }
	
	def foldL(Closure foldC, initVal){
		BaseFL.foldL(foldC, initVal, this as FunList)
	}
	
	def foldLUntil(Closure predicate, Closure foldC, initVal){
		BaseFL.foldLUntil(predicate, foldC, initVal, this as FunList)
	}
	
	def reduceL(Closure foldC){
		BaseFL.reduceL(foldC, this as FunList)
	}
	
	def foldR(Closure foldC, initVal){
		BaseFL.foldR(foldC, initVal, this as FunList)
	}
	
	def reduceR(Closure foldC){
		BaseFL.reduceR(foldC, this as FunList)
	}

	def foldXR(Closure foldC, initVal){
		BaseFL.foldXR(foldC, initVal, this as FunList)
	}
	
	def reduceXR(Closure foldC){
		BaseFL.reduceXR(foldC, this as FunList)
	}

	def take(n){
		BaseFL.take(n, this as FunList)
	}
	
	def takeWhile(Closure predicate){
		BaseFL.takeWhile(predicate, this as FunList)
	}
	
	def drop(n){
		BaseFL.drop(n, this as FunList)
	}
	
	def dropWhile(Closure predicate){
		BaseFL.dropWhile(predicate, this as FunList)
	}

	def reverse(){
		BaseFL.reverse(this as FunList)
	}

}
