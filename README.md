Fpiglet 0.1.
FUNctional Programming in Groovy

* https://github.com/rpeszek/fpiglet.git
* http://code.google.com/p/fpiglet/  (original/old project location)


Release 0.1 TOC:

 * (Implicitly) curried functions (Curried Functions will change the way you code!)
 * Functional Types accompanied by libraries of (curried) functions:
   * Maybe
   * Either
   * Lazy Functional Lists (FunList, ListFunctions)
 * Functional concepts (and Functional Combinator Libraries):
  * Functor
  * Applicative
  * Monad
 * Functor mapped functional list libraries (functional list library applied to other concepts):
  * Function library for Groovy Lists 
  * Function library for Strings 
  * Function library for InputStream
 * Expressions:
  * Monadic Comprehensions 
  * Functional if-then-elseif-then-else chains
  * Localized scope expressions


Dependencies: 
  
Fpiglet is using Groovy 2.0 (but it should work fine on 1.8) and JUnit4.

_Gruesome_: Fpiglet source tree: ext/gruesome includes a slightly modified copy of https://github.com/mcandre/gruesome 
together with its own (FreeBSD) license file. 
Gruesome is a simplified version of QuickCheck Property-based Testing library ported to Groovy. 
(Integration of Property-based testing into Fpiglet is work in progress and will improve in the future). 


Building:

Gradle build file is included.
Eclipse project files are included, but may require some classpath adjustments. 


Source Tree Structure:

 * src/groovy/ - Fpiglet source
 * ext/gruesome/ -  a slightly modified copy of https://github.com/mcandre/gruesome together with its own (FreeBSD) license file.
 * test/ - All Fpiglet unit test files.
 * resources/ - resource files used in tests
