import gruesome.Gruesome;

//#!/usr/bin/env groovy

def isEven = { i -> i % 2 == 0 }

def genEven = {
  def i = Gruesome.genInt()

  if (i % 2 as int != 0) {
    i + 1
  }
  else {
    i
  }
}

def reverse = { s ->
  ((s.length() - 1) .. 0).collect { i -> s.charAt(i) }.join("")
}

def reversible = { s -> reverse(reverse(s)) == s }

/* Are all integers even? */
Gruesome.forAll(isEven, [Gruesome.genInt])

/* Are all even integers even? */
Gruesome.forAll(isEven, [genEven])

/* Are all strings reversible? */
Gruesome.forAll(reversible, [Gruesome.genString])