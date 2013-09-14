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
package fpig.funlist.functions.impl

import fpig.util.CallUtil
import fpig.groovylist.asfunlist.functions.GroovyListAsFunList;
import fpig.groovylist.asfunlist.functions.InAndOutOfFunLists;
import fpig.funlist.types.FunList
import fpig.funlist.functions.impl.*
import static fpig.expressions.IfElseSyntax.*

/**
 * Internal Fpiglet use.
 * <p>
 * See {@link fpig.funlist.functions.BaseFL} for information on filer function
 * 
 * @author Robert Peszek
 */
class Concat {

	//trampoline makes the code hard to read, remove it and the code will work only for short lists where predicate fails sequentially in a row.
	static Closure getConcatCT() {
		return {FunList l ->
			def empty = FunList.EMPTYLIST
			if(l.empty)
				empty
			else {
				def firstList = l.head
				if(firstList instanceof FunList){
					if(firstList.empty) {
						Concat.concatCT(l.tail)
					} else {
						empty.build(firstList.head, { def newBigList = empty.build(firstList.tail, {l.tail}); Concat.concatCT( newBigList )})
					}
				} else {
				    empty
				}
			}
		}
	}
	
	//needs re-wrapping in a closure because of trampoline use and of how callUtility works
	static Closure concat = CallUtil.toFunction {list -> 
		def funlist = 
		_if_{list instanceof List} >> 
		_then_ {
			InAndOutOfFunLists.funlistIn << list
		} >> _else_ {
		    list
		}
		Concat.concatCT(funlist)
		}


}
