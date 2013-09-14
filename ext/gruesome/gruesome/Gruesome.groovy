package gruesome
import java.util.Random

class Gruesome {
  static def VERSION = "0.0.1"

  static def genInt = { new Random().nextInt() }
  static def genBool = { new Random().nextBoolean() }
  static def genByte = { (Gruesome.genInt().abs() % 256) as int }
  static def genChar = { (Gruesome.genByte() % 128) as char }

  static def genArray = { g ->
    def len = Gruesome.genInt().abs() % 100
    (0 .. len).collect { i -> g() }
  }

  static def genString = { Gruesome.genArray(Gruesome.genChar).join("") }

  static apply(clos, args) {
    def c = clos
    args.each { a -> c = c.curry(a) }

    c()
  }

  static forAll(property, generators) {
    def testCases = (0 .. 99).collect { i -> generators.collect({ g -> g() }) }
    def failures = testCases.findAll { testCase -> !apply(property, testCase) }

    if (failures.size > 0) {
      println "*** Failed!"
      println failures[0]
    }
    else {
      println "+++ OK, passed 100 tests."
    }
  }
}