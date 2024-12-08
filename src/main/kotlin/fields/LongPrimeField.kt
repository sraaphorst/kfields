// By Sebastian Raaphorst, 2024.

package org.vorpal.fields

import org.vorpal.common.isPrime
import java.math.BigInteger
import java.security.SecureRandom
import kotlin.math.absoluteValue

class LongPrimeField(override val characteristic: Long) : PrimeField<Long> {
    init {
        characteristic.isPrime()
    }

    override val zero = 0L
    override val one = 1L
    override val cardinality: BigInteger = characteristic.toBigInteger()

    // Make sure that the value from any calculation is in the range [0, characteristic).
    override fun standardize(a: Long): Long =
        ((a % characteristic) + characteristic) % characteristic

    override fun add(a: Long, b: Long): Long = standardize(a + b)
    override fun multiply(a: Long, b: Long): Long = standardize(a * b)
    override fun subtract(a: Long, b: Long): Long = standardize(a - b)
    override fun negate(a: Long): Long = standardize(-a)

    override fun invert(a : Long): Long {
        if (isZero(a)) throw ArithmeticException("Attempt to calculate multiplicative inverse of zero.")
        tailrec fun aux(t: Long = 0L, newT: Long = 1L,
                        r: Long = characteristic, newR: Long = a): Long = when {
            isZero(newR) -> standardize(t)
            else -> {
                val quotient = r / newR
                aux(newT, t - quotient * newT, newR, r - quotient * newR)
            }
        }
        return aux()
    }

    /**
     * Secure random number generator that uses rejection sampling to avoid modulo bias.
     */
    override fun randomElement(): Long {
        fun aux(random: SecureRandom = SecureRandom()): Long {
            val candidate = random.nextLong().absoluteValue
            return if (candidate >= Long.MAX_VALUE - (Long.MAX_VALUE % characteristic))
                aux(random)
            else
                candidate % characteristic
        }
        return aux()
    }

    override fun allElements(): Sequence<Long> =
        LongRange(0L, characteristic).asSequence()
}
