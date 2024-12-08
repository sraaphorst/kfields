// By Sebastian Raaphorst, 2024.

package org.vorpal.fields

interface ModularRing<T>: Ring<T> {
    val characteristic: T
}