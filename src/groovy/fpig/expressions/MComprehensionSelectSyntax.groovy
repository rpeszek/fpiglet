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

import org.codehaus.groovy.runtime.metaclass.NewMetaMethod;

import fpig.funlist.types.FunList;
import fpig.funlist.functions.BaseFL as lists;
import fpig.groovylist.asfunlist.functions.InAndOutOfFunLists as glists 
import fpig.util.CallUtil;
import fpig.common.functions.impl.FpigMonads as monads
import fpig.concepts.MonadDescription;
import groovy.lang.Closure;


/**
 * Monadic comprehension syntax implementation. 
 * This is implementation class, should not be used directly.
 * <p>
 * See FpigBase for more info.
 * @see fpig.common.functions.FpigBase#comprehend(Closure)
 * @see fpig.common.functions.FpigBase#comprehend(MonadDescription, Closure)
 * @author Robert Peszek
 */
class MComprehensionSelectSyntax {
    
	static def select(Closure content) {
		MComprehensionSelectSyntax _syntax = new MComprehensionSelectSyntax()
		_syntax.helper.outputC = content
		_syntax
	}

	static def selectP(Closure content) {
		MComprehensionSelectSyntax _syntax = new MComprehensionSelectSyntax()
		_syntax.helper.outputC = {varM ->
			content.delegate = varM
			content.resolveStrategy = Closure.DELEGATE_FIRST
			_syntax.helper.usedMonad.pure( content() )
	    }
		_syntax
	}

	def using(MonadDescription monad){
		this.useMonad = monad
		this
	}
	
	def from(Closure c){
		c.delegate = this
		c()
		helper.comprehendImpl(this.useMonad)
	}

	def where(Closure content){
		helper.restrictC = content
	}

	
	//privates
	MComprehensionHelper helper = new MComprehensionHelper()
	MonadDescription useMonad

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
			c
		}
	}

}
