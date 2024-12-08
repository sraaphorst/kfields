// By Sebastian Raaphorst, 2024.

package org.vorpal.fields

import java.math.BigInteger

interface FiniteField<T> : Field<T> {
    val cardinality: BigInteger

    fun randomElement(): T
    fun allElements(): Sequence<T>
}
