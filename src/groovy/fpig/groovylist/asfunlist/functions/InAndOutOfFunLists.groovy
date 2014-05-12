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

package fpig.groovylist.asfunlist.functions

import fpig.funlist.functions.BaseFL;
import fpig.funlist.types.FunList
import fpig.funlist.types.LazyList
import fpig.util.CallUtil

/**
* Functions for moving in and out between Groovy {@code Lists} and functional {@code FunList} (Lazy List)
* <p>
* Examples:
* <pre>
* def manipulation = funlistOut << take(3) << filter {it % 5==0} << map {1 + it * it} << funlistIn
* assert [5,10,50] == manipulation(1..10000)
* </pre>
* funlistIn and funlistOut mark boundaries between functional and OO code.
* <p>
* An equivalent of the above code is:
* <pre>
* def manipulation = funlistOutTake(3) << filter {it % 5==0} << map {1 + it * it} << funlistIn
* </pre>
* The above code can also be done using withFunList method defined on this class:
* <pre>
* def manipulation = withFunList( take(3) << filter {it % 5==0} << map {1 + it * it} )
* </pre>
* or
* <pre>
* def manipulation = withFunListTake(3)( filter {it % 5==0} << map {1 + it * it} )
* </pre>
* Also, please note that {@link fpig.common.functions.FpigBase} provides an overload of the {@code f} method
* which allows to simply write
* <pre>
*  def flist = f([1,2,3])
* </pre>
* instead of
* <pre>
*  def flist = funlistIn << [1,2,3]
* </pre>
* @see fpig.groovylist.asfunlist.functions.FunListToListFunctor
* @see fpig.common.functions.FpigBase#f(java.util.List)
* @author Robert Peszek
*/

class InAndOutOfFunLists {
    static Closure funlistIn = {List fromL, Map props=null->
        FunList result = BaseFL.empty();
        fromL.reverseEach{ result = BaseFL.prepend(it, result) } //TODO should prepend be used or build?
        result
    }
	
    static Closure funlistOutTakeC = { long n, FunList flist ->
        def result = []; int i=0; boolean fetchAll = n<0; def current = flist
        while(fetchAll || i<n) {
            //very ugly imperative code, sorry!
            i +=1;
            if(!current.empty) {
                result.add(current.head);
				if(fetchAll || i<n)
                current = current.tail
            }
            else {
                break
            }
        }
        result
    }
    static Closure funlistOutTake = CallUtil.toFunction funlistOutTakeC
    static Closure funlistOut = funlistOutTake(-1)
	
	/**
	 * Executes closure f passing it list converted to FunList. 
	 * Converts the result back to Groovy list if the result is a FunList, otherwise returns the result.
	 * <p>
	 * Expected signature of the argument closure:
	 * <pre>
	 *   FunList -> FunList or something else
	 * </pre>
	 */
	static Closure withFunList = CallUtil.toFunction {Closure f, groovyList ->
		def output = f << InAndOutOfFunLists.funlistIn << groovyList
		if(output instanceof FunList)
		   InAndOutOfFunLists.funlistOut output
		else 
		   output
	}
	
	/**
	 * Same as withFunList, only restricts output to n records.
	 */
	static Closure withFunListTake = CallUtil.toFunction {n, Closure f, groovyList ->
		InAndOutOfFunLists.funlistOutTake(n) << f << InAndOutOfFunLists.funlistIn << groovyList	
	}
}
