// By Sebastian Raaphorst, 2024.

package org.vorpal.fields

/**
 * These morphisms are contracts and are not explicitly checked.
 * It is expected that the developer will ensure that the morphisms have the
 * required properties.
 */

interface RingHomomorphism<A, B> : (A) -> B {
    val domain: Ring<A>
    val codomain: Ring<B>

    fun respectsAddition(a1: A, a2: A) =
        codomain.add(this(a1), this(a2)) == this(domain.add(a1, a2))

    fun respectsMultiplication(a1: A, a2: A) =
        codomain.multiply(this(a1), this(a2)) == this(domain.multiply(a1, a2))

    fun preservesZero() = codomain.isZero(this(domain.zero))
    fun preservesOne() = codomain.isOne(this(domain.one))
}

interface RingEndomorphism<A> : RingHomomorphism<A, A>

interface RingMonomorphism<A, B> : RingHomomorphism<A, B>

interface RingEpimorphism<A, B> : RingHomomorphism<A, B>

interface RingIsomorphism<A, B> : RingHomomorphism<A, B> {
    val inverse: RingHomomorphism<B, A>
    fun isInverseCorrect1(a: A): Boolean = inverse(this(a)) == a
    fun isInverseCorrect2(b: B): Boolean = this(inverse(b)) == b
}

interface RingAutomorphism<A> : RingIsomorphism<A, A> {
    override val inverse: RingAutomorphism<A>
}