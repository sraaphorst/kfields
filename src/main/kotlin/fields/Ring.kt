// By Sebastian Raaphorst, 2024.

package org.vorpal.fields

interface Ring<F> {
    val zero: F
    val one: F

    fun isZero(a: F): Boolean =
        standardize(a) == standardize(zero)
    fun isOne(a: F): Boolean =
        standardize(a) == standardize(one)
    fun isUnit(a: F): Boolean

    /**
     * Function to standardize a value in a ring.
     * This can be used in rings modulo n where n is not a prime (else we have a PrimeField)
     * or in polynomial rings.
     */
    fun standardize(a: F): F = a

    fun add(a: F, b: F): F
    fun subtract(a: F, b: F): F
    fun multiply(a: F, b: F): F
    fun negate(b: F): F

    fun pow(base: F, exponent: Long): F {
        tailrec fun powAccum(currBase: F = standardize(base),
                             currExponent: Long = exponent,
                             currAcc: F = one): F {
            if (currExponent == 0L) return currAcc
            val nextAcc = if ((currExponent and 1L) == 1L) multiply(currAcc, currBase) else currAcc
            return powAccum(multiply(currBase, currBase), currExponent shr 1, nextAcc)
        }
        require(exponent >= 0) { "Exponent must be nonnegative." }
        return powAccum(standardize(base), exponent, one)
    }

    operator fun F.plus(other: F): F = add(other, this)
    operator fun F.minus(other: F): F = subtract(this, other)
    operator fun F.times(other: F) : F = multiply(this, other)
}
