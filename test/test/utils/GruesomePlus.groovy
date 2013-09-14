package test.utils

import fpig.common.functions.FpigBase as fp;
import fpig.common.functions.FromOperators as fo;
import gruesome.Gruesome;

class GruesomePlus extends Gruesome {
	static Closure genMaybes(Closure innerG) { 
		def c = {
			if(!genNothing){
				genNothing = true
				fp.nothing()
			} else {
			    fp.just(innerG())
			}
		}
		c.delegate = [:]
		c
	}
	
	static Closure genPolynomial = { g ->
		def len = Gruesome.genInt().abs() % 20
		def coef = (0 .. len).collect { i -> g() }
	    
		Closure res = {x -> 
			(0 .. len).collect {i -> coef[i] * (x**i) }.sum() 
		}
		res
	}
	
	static def genFloat = { new Random().nextFloat() }
	
	static def genDouble = { new Random().nextDouble() }
	
	
	static void assertForAll(property, generators) {
		def testCases = (0 .. 99).collect { i -> generators.collect({ g -> g() }) }
		def failures = testCases.findAll { testCase -> !apply(property, testCase) }
	
		if (failures.size > 0) {
		  println "*** Failed!"
		  println failures[0]
		}
		else {
		  println "+++ OK, passed 100 tests."
		}
		assert !failures
	}
	
	static void assertSpecific(property, data){
		assert property(data)
	}
}
