package org.evoleq.math.cat.suspend.monad.state

import kotlinx.coroutines.coroutineScope
import org.evoleq.math.cat.marker.MathSpeakDsl
import org.evoleq.math.cat.structure.x
import org.evoleq.math.cat.suspend.morphism.by

@MathSpeakDsl
fun <B, S, T> B.perform(klState: KlScopedSuspendedState<B, S, T>): Pair<B, KlScopedSuspendedState<B, S, T>> = this x klState

@MathSpeakDsl
suspend infix fun <B, S, T> Pair<B, KlScopedSuspendedState<B, S, T>>.on(data: S): Pair<T, B> = coroutineScope {
    by(by(second)(data))(first)
}