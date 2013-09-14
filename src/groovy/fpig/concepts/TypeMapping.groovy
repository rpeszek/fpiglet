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
 * Many functional programming concepts are about transforming types.  For example String can be mapped to List of Strings.
 * Implementation of this class signify such a mapping/transformation.
 * <p>
 * This Class is really used for informational purposes only. There is no logic build behind type mappings at this moment.
 * Also how Type Mapping is defined is very likely to change (maybe annotation?).
 * <p>
 * The goal is to convey to programmer reading the code that, hey, we are mapping some type F to some other type T.
 * Java or Groovy does not really have a concept that can easily describe transforming set of Types into some other set of Types.
 * <p>
 * For now, Generics are used do describe how types are mapped. Since fpiglet uses curried functions the typing info gets lost
 * fast and Fpiglet may need to come up with some better mechanism.  
 * <p>
 * So, for now, TypeMapping&lt;String, List&lt;String&gt;&gt; identifies some mapping from String to a list of strings.
 * <pre>
 *  &lt;A&gt; TypeMapping&lt;A, List&lt;A&gt;&gt;
 * </pre> 
 *  would be a more generic type mapping, which could represent the List concept itself.
 * <p>
 * Fpiglet will probably change how it works moving forward.
 * <p> 
 * Type mapping examples are listed in see also.
 * 
 * @see fpig.concepts.FunctorDescription
 * @see fpig.concepts.MonadDescription
 * @see fpig.concepts.ApplicativeDescription
 * 
 * @author Robert Peszek
 * 
 * @param Generic Type A - mapped from
 * @param Generic Type B - mapped to
 */
abstract class TypeMapping<A,B> {

}
