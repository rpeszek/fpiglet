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
package fpig.expressions

import fpig.funlist.types.FunList;
import fpig.funlist.functions.BaseFL as lists;
import fpig.groovylist.asfunlist.functions.InAndOutOfFunLists as glists 
import fpig.util.CallUtil;
import fpig.common.functions.impl.FpigMonads as monads
import fpig.concepts.MonadDescription;
import groovy.lang.Closure;

/*
 def res = comprehend {
	 x << from{ just(3) }
	 y << from{ just(x+1) }
	 output{ just(y-x) }
 }
 is transformed (logically) into:
  {->
	  b({z1-> x=z1
		{->
		   b({z2-> y=z2;
			 {just(y-x)}()}) << {just(x+1)}()
		}()
	  }) << {just(3)}()
  }()
  This looks like function composition and is implemented using foldR and curried functions.
  
  Note: use of mixed monads is not supported
*/

/**
 * Monadic comprehension syntax implementation. 
 * This is implementation class, should not be used directly.
 * <p>
 * See FpigBase for more info.
 * @see fpig.common.functions.FpigBase#comprehend(Closure)
 * @see fpig.common.functions.FpigBase#comprehend(MonadDescription, Closure)
 * @author Robert Peszek
 */
class MComprehensionSyntax {
    
	Closure from(Closure c) {
		c
	}
	
	def restrict(Closure content){
		helper.restrictC = content
	}

	def output(Closure content) {
	    helper.outputC = content
	}	
	
	def outputP(Closure content) {
		helper.outputC = { varM ->
			content.delegate = varM
			content.resolveStrategy = Closure.DELEGATE_FIRST
			def res = content()
			def monad = owner.helper.usedMonad
			return monad.pure( res )
        }
	}

	static def comprehend(MonadDescription useMonad, Closure c){
		def syntax = new MComprehensionSyntax()
		c.delegate = syntax
		c()
		syntax.helper.comprehendImpl(useMonad)
	}

	static def comprehend(Closure c){
		comprehend(null, c)
	}

	//privates
	MComprehensionHelper helper = new MComprehensionHelper()

	def get(String name){
		new MSyntaxHelper(name)
	}
	
	class MSyntaxHelper{
		MSyntaxHelper(String n){
			name = n
		}
		String name
		
		def leftShift(Closure c) {
			helper.expressions = helper.expressions + [[from: c, var: name, restrict:helper.restrictC]]
			helper.restrictC = null
		}
	}


}
