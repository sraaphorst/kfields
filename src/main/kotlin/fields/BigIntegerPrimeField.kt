// By Sebastian Raaphorst, 2024.

package org.vorpal.fields

import org.vorpal.common.isPrime
import java.math.BigInteger
import java.security.SecureRandom

class BigIntegerPrimeField(override val characteristic: BigInteger) : PrimeField<BigInteger> {
    init {
        require(characteristic.isPrime()) { "Cannot create finite field of characteristic $characteristic" }
    }

    override val zero: BigInteger = BigInteger.ZERO
    override val one: BigInteger = BigInteger.ONE
    override val cardinality: BigInteger = characteristic

    // Make sure that the value from any calculation is in the range [0, characteristic).
    override fun standardize(a: BigInteger): BigInteger =
        ((a % characteristic) + characteristic) % characteristic

    override fun add(a: BigInteger, b: BigInteger): BigInteger = standardize(a + b)
    override fun multiply(a: BigInteger, b: BigInteger): BigInteger = standardize(a * b)
    override fun subtract(a: BigInteger, b: BigInteger): BigInteger = standardize(a - b)
    override fun negate(a: BigInteger): BigInteger = standardize(-a)

    override fun invert(a : BigInteger): BigInteger {
        if (isZero(a)) throw ArithmeticException("Attempt to calculate multiplicative inverse of zero.")
        tailrec fun aux(t: BigInteger = BigInteger.ZERO, newT: BigInteger = BigInteger.ONE,
                        r: BigInteger = characteristic, newR: BigInteger = a): BigInteger = when {
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
    override fun randomElement(): BigInteger {
        fun aux(random: SecureRandom = SecureRandom()): BigInteger {
            val candidate = BigInteger(characteristic.bitLength(), random)
            return if (candidate >= characteristic)
                aux(random)
            else
                candidate
        }
        return aux()
    }

    override fun allElements(): Sequence<BigInteger> =
        generateSequence(BigInteger.ZERO) {
            if (it < characteristic - BigInteger.ONE) it + BigInteger.ONE else null
        }
}