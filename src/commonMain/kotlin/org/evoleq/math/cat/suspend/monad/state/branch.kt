package org.evoleq.math.cat.suspend.monad.state

import org.evoleq.math.cat.adt.Sum
import org.evoleq.math.cat.marker.MathSpeakDsl


@MathSpeakDsl
fun <B1,B2, S> klBranch(): KlScopedSuspendedState<Sum<B1, B2>, S, S> = KlScopedSuspendedState {
    s -> ReturnState(s)
}