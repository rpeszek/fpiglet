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
package fpig.concepts

/**
 * Work in progress. This class 'describes' a monoid. 
 * This class is currently not used within Fpiglet. 
 * <p>
 * @author Robert Peszek
 *
 * @param Generic Type A - mapped from
 * @param Generic Type B - mapped to
 */
abstract class MonoidDescription<A,B> extends TypeMapping<A,B> {
   
    abstract Closure getEmpty()
	
	/**
	 * Logical Signature:
	 * <pre>
	 *  m -> m -> m
	 * </pre>
	 */
	abstract Closure getAppend()
}

