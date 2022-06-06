package org.evoleq.math.cat.monad.state

import kotlinx.coroutines.coroutineScope
import org.evoleq.math.cat.marker.MathCatDsl
import org.evoleq.math.cat.morphism.Morphism
import org.evoleq.math.cat.morphism.by

/**
 * Evaluation
 */
@MathCatDsl
fun <S, T> Pair<Morphism<S, T>,S>.evaluate(): T = by(first)(second)