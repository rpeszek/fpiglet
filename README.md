Fpiglet 0.1.
FUNctional Programming in Groovy

http://code.google.com/p/fpiglet/  (Good source of documentation for Fpiglet)
https://github.com/rpeszek/fpiglet.git
git@github.com:rpeszek/fpiglet.git



Release 0.1 TOC:

 * (Implicitly) curried functions (Curried Functions will change the way you code!)
 * Functional Types accompanied by libraries of (curried) functions:
   ** Maybe
   ** Either
   ** Lazy Functional Lists (FunList, ListFunctions)
 * Functional concepts:
  ** Functor Polymorphism
  ** Applicative Polymorphism
  ** Monad Polymorphism
 * Concept-based functional libraries
  ** Monadic function library
  ** Applicative function library
 * Functor mapped functional list libraries (functional list library applied to other concepts):
  ** Function library for Groovy Lists 
  ** Function library for Strings 
  ** Function library for InputStream
 * Expressions:
  ** Monadic Comprehensions 
  ** Functional if-then-elseif-then-else chains
  ** Localized scope expressions


Dependencies: 
  
Fpiglet is using Groovy 2.0 (but it should work fine on 1.8) and JUnit4.

_Gruesome_: Fpiglet source tree: ext/gruesome includes a slightly modified copy of https://github.com/mcandre/gruesome 
together with its own (FreeBSD) license file. 
Gruesome is a simplified version of QuickCheck Property-based Testing library ported to Groovy. 
(Integration of Property-based testing into Fpiglet is work in progress and will improve in the future). 


Building:
Eclipse project file is included, but you will need to make some classpath adjustments. 
If you prefer IDE agnostic build tool, gradle build file is included as well (). 


Source Tree Structure:
  src/groovy - Fpiglet source
  ext/gruesome -  a slightly modified copy of https://github.com/mcandre/gruesome together with its own (FreeBSD) license file.
  test - All Fpiglet test files.
