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

import fpig.groovylist.asfunlist.functions.InAndOutOfFunLists;

import fpig.funlist.types.FunList

/**
* Internal Fpiglet use. Mixin used to add OO flavor to FunList. 
* <p>
* Fpilget may decide to drop support for OO interface to lists at one point.
* <p>
* @author Robert Peszek
*/
class OoListInAndOutOfListsPart {

    List fetch(long n) {
        InAndOutOfFunLists.funlistOutTakeC(n, this as FunList)
    }
    List fetchAll() {
        InAndOutOfFunLists.funlistOut(this as FunList)
    }
	
	String toString() {
		toStringImpl(this as FunList) //explicit cast needed for groovy console bits me!
	}
	
	static String toStringImpl(FunList flist) {
		List list = InAndOutOfFunLists.funlistOutTakeC(20, flist)
		if(list.size()==20){
			list = list.take(19) + ['...']
		}
		'f(' + list.toString() + ')'
	}

}
