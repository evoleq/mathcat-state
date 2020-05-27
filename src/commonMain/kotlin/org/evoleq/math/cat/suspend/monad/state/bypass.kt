package org.evoleq.math.cat.suspend.monad.state

import kotlinx.coroutines.CoroutineScope
import org.evoleq.math.cat.marker.MathSpeakDsl
import org.evoleq.math.cat.suspend.morphism.by

@MathSpeakDsl
infix fun <S,T,D> ScopedSuspendedState<S, T>.collect(data: D): ScopedSuspendedState<S, Pair<T, D>> = ScopedSuspendedState {
    s -> with(by(this@collect)(s)) {
        Pair(Pair(first,data),second)
    }
}

@MathSpeakDsl
infix fun <B,S,T, D, E> KlScopedSuspendedState<B,S,T>.bypass(f: suspend CoroutineScope.(D)->E): KlScopedSuspendedState<B,Pair<S,D>, Pair<T,E>> =
    KlScopedSuspendedState {
        pair -> by(this@bypass)(pair.first) collect f(pair.second)
    }