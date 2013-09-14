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

import java.util.List;

import org.codehaus.groovy.runtime.metaclass.NewMetaMethod;

import fpig.funlist.types.FunList;
import fpig.funlist.functions.BaseFL as lists;
import fpig.monad.functions.BaseM;
import fpig.groovylist.asfunlist.functions.InAndOutOfFunLists as glists 
import fpig.util.CallUtil;
import fpig.common.functions.impl.FpigMonads
import fpig.common.types.ClosureHolder;
import fpig.concepts.MonadDescription;
import groovy.lang.Closure;


/**
 * Internal Fpiglet Class. 
 * @author Robert Peszek
 */
class MComprehensionHelper {
    
	//privates
	def vars = [:]
	List expressions = []
	Closure outputC
	Closure restrictC
    MonadDescription specificMonad
	MonadDescription usedMonad
	
	def getMonads() {
		if(specificMonad) {
		  BaseM.getInstance(specificMonad)
		} else 
		  FpigMonads
	}
	/**
	 * Monadic syntax comprehension - work in progress
	 * @param c
	 * @return
	 */
	def comprehendImpl(MonadDescription specificMonad = null){
		this.specificMonad = specificMonad
		this.usedMonad = specificMonad
		FunList doExpressions = glists.funlistIn << this.expressions
		boolean closureBoxing = false
		
		def monadDesc = {mx ->
			if(!usedMonad){
				usedMonad = FpigMonads.getMonad(mx)
			}
			mx
		}
		def autoBox = {mx ->
			if(mx instanceof Closure){
				closureBoxing = true
				new ClosureHolder(mx)
			} else {
			    mx
			}
		}
		def autoUnBox = {mx ->
			if(closureBoxing && mx instanceof ClosureHolder){
				mx.cVal
			} else {
			    mx
			}		
		}
		Closure foldC = CallUtil.toFunction {expr, acc ->
			Closure nested = CallUtil.toFunction {varM, z -> 
				varM[expr.var] = z
				acc(varM.clone())
			}
			Closure result = {Closure restrictFilter ->
				return {varM ->
					expr.from.delegate = varM 
					expr.from.resolveStrategy = Closure.DELEGATE_FIRST
					if(expr.restrict){
						expr.restrict.delegate = varM
						expr.restrict.resolveStrategy = Closure.DELEGATE_FIRST
					}
					monads.bind(nested(varM)) << restrictFilter << monadDesc << autoBox(expr.from()) 
				}
			}
			Closure restrictFilter = expr.restrict ? monads.mfilter(expr.restrict): Closure.IDENTITY
			return result(restrictFilter)
		}
		
		def innertC
		if(this.restrictC){
			 innertC = {varM ->
				 this.restrictC.delegate = varM
				 this.restrictC.resolveStrategy = Closure.DELEGATE_FIRST
				 this.outputC.delegate = varM
				 this.outputC.resolveStrategy = Closure.DELEGATE_FIRST
				 if(this.restrictC(varM))
				   this.outputC(varM)
				 else 
				   usedMonad.emptyM //do we need to throw specific exception if not used with MonadWithEmptyDescription monad?
			 }
		} else {
		     innertC = {varM ->
				 this.outputC.delegate = varM
				 this.outputC.resolveStrategy = Closure.DELEGATE_FIRST
				 this.outputC(varM)
		     }
		}
		def res = lists.foldR(foldC, innertC) << doExpressions
		autoUnBox(res(this.vars))
	}
	
}
