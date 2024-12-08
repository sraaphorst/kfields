// By Sebastian Raaphorst, 2024.

package org.vorpal.common

import java.math.BigInteger
import kotlin.math.ceil
import kotlin.math.sqrt

/**
 * Create a Sequence of all primes as Long.
 * This if not purely functional as this is more efficient.
 */
fun allLongPrimes(): Sequence<Long> = sequence {
        val composites = mutableMapOf<Long, MutableList<Long>>()
        var candidate = 2L

        while (true) {
            when {
                // If candidate is not in the composites map, it is prime.
                candidate !in composites -> {
                    yield(candidate)

                    // Mark the first multiple of this prime as composite.
                    composites[candidate * candidate] = mutableListOf(candidate)
                }
                else -> {
                    composites[candidate]?.forEach { prime ->
                        composites.computeIfAbsent(prime + candidate) { mutableListOf() }.add(prime)
                    }
                    composites.remove(candidate)
                }
            }
            candidate++
        }
    }

fun Long.isPrime(): Boolean {
    if (this < 0)
        throw ArithmeticException("Primes must all be nonzero.")
    val maxValue = ceil(sqrt(this.toDouble())).toLong()
    return (2..maxValue).all { this % it > 0L}
}

fun allBigIntegerPrimes(): Sequence<BigInteger> =
    generateSequence(BigInteger.TWO) { it.nextProbablePrime() }

/**
 * Assert with a high level of certainty that the BigInteger is probably prime.
 */
fun BigInteger.isPrime(certainty: Int = 20): Boolean =
    isProbablePrime(certainty)
