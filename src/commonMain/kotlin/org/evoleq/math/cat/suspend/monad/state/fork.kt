package org.evoleq.math.cat.suspend.monad.state

import org.evoleq.math.cat.marker.MathSpeakDsl
import org.evoleq.math.cat.structure.x

@MathSpeakDsl
fun <B1,B2, S> klFork(): KlScopedSuspendedState<Pair<B1,B2>, S, Pair<S,S>> = KlScopedSuspendedState {
    s -> ReturnState(s x s)
}