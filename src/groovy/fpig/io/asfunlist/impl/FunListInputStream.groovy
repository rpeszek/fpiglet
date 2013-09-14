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
package fpig.io.asfunlist.impl

import fpig.funlist.types.FunList;
import fpig.util.CallUtil;
import groovy.lang.Closure;

import java.io.IOException
import java.io.InputStream;
import java.util.List;

/**
 * Internal implementation class used to achieve transformation between FunList and InputStreams.
 * 
 * @see fpig.funlist.types.FunList
 * @see fpig.io.asfunlist.functions.InAndOutOfFunLists
 * @see fpig.io.asfunlist.functions.FunListToTokenizedInputStreamFunctor
 * @author peszek
 *
 */
class FunListInputStream extends InputStream {
    
	Closure untokenizer
    FunList currentList
    InputStream currentInput
	
	FunListInputStream(Closure _untokenizer, FunList _list){
		currentList = _list
		untokenizer = _untokenizer
	}

    //UGLY imperative code alert
    //TODO change this to recursive trampoline with less state mutation
	@Override
    int read() {
        int res = -1
        Closure nextInput = { ->
            if(currentList.empty){
                return null
            } else {
                def head = currentList.head
                currentList = currentList.tail
                currentInput = untokenizer(head)
            }
        }
        if(!currentInput){
            currentInput = nextInput()
            if(!currentInput){
                return -1
            }
        }
        res = currentInput.read()
        while(res ==-1){
            currentInput = nextInput()
            if(currentInput ==null){
                return -1
            }
            res = currentInput.read()
        }
        return res
    }

}
