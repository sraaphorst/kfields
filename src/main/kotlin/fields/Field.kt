// By Sebastian Raaphorst, 2024.

package org.vorpal.fields

/**
 * Generic interface for a field.
 */
interface Field<F>: Ring<F> {
    override fun isUnit(a: F): Boolean =
        !isZero(a)

    fun invert(a: F): F

    override fun pow(a: F, exponent: Long): F {
        return if (exponent >= 0) {
            // Use the Ring's implementation
            super.pow(a, exponent)
        } else {
            // In a field, we can handle negative exponents by inverting 'a'
            pow(invert(a), -exponent)
        }
    }
}
