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
package fpig.common.functions.impl

import fpig.common.functions.ClosureMonad;
import fpig.common.functions.EitherMonad;
import fpig.common.functions.MaybeMonad;
import fpig.common.types.ClosureHolder;
import fpig.common.types.Either;
import fpig.common.types.Maybe;
import fpig.concepts.MonadDescription;
import fpig.concepts.MonadWithEmptyDescription;
import fpig.funlist.functions.FunListMonad;
import fpig.funlist.types.FunList;
import fpig.monad.functions.BaseM;
import fpig.util.CallUtil
import fpig.util.curring.Parameter_;
import groovy.lang.Closure;

/**
 * Allows registering custom monads. Also works as implementation helper for FpigBase.
 * @author Robert Peszek
 */
class FpigMonads {
	/**
	 * private
	 */
	static Map _monads = [:]

	/**
	 * Allows to configure Fpiglet with new monad so that polymorphic functions such as
	 * {@code FpigBase.b} or {@code FpigBase.unpure} or monadic comprehensions are working with it.
	 * <p>
	 * This is obviously unpure/unsafe method. It changes Fpiglet configuration. 
	 * @param monadClass
	 * @param mdescription
	 */
	static void configureMonad(Class monadClass, MonadDescription mdescription){
		_monads[monadClass] = mdescription
	}
	

	/**
	 * For use by Fpiglet functions and FpigBase.
	 */
	static Closure getBind(){
		return CallUtil.toFunction {Closure c,  m-> 
		   MonadDescription monad = getMonad(m)
		   monad.bind(c, m)
	   }		
	}

	/**
	 * private
	 */
	static MonadDescription getMonad(m){
		MonadDescription monad
		if(m instanceof FunList){
			monad = FunListMonad.instance
		} else if (m instanceof Maybe){
			monad = MaybeMonad.instance
		} else if (m instanceof Either){
		   monad = EitherMonad.instance
		} else if (m instanceof ClosureHolder){
		   monad = ClosureMonad.instance
		} else {
		    def pair = _monads.find {Class key, MonadDescription value-> 
				key.isAssignableFrom(m.getClass())
			}
			if(pair)
				monad = pair.value
			else
				throw new RuntimeException('No MonadDescription implemented for '+ m)
		}
        monad
	}
	
	/**
	 * For use by Fpiglet functions and FpigBase.
	 */
	static Closure getUnpure(){
		return CallUtil.toFunction {m, Closure c->
		   MonadDescription monad = getMonad(m)
		   BaseM mf = BaseM.getInstance(monad)
		   mf.unpure(m, c)
	   }
	}

	/**
	 * For use by Fpiglet functions and FpigBase.
	 */
	static Closure getAsUnpure(){
		CallUtil.flip unpure
	}

	/**
	 * For use by Fpiglet functions and FpigBase.
	 */
	static Closure getMfilter() {
		return CallUtil.toFunction {Closure c, m->
			MonadWithEmptyDescription monad = getMonad(m)
			BaseM mf = BaseM.getInstance(monad)
			mf.mfilter(c, m)
		}
	}
}
