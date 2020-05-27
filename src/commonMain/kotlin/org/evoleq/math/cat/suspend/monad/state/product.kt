package org.evoleq.math.cat.suspend.monad.state

import org.evoleq.math.cat.marker.MathSpeakDsl
import org.evoleq.math.cat.suspend.morphism.by

@MathSpeakDsl
infix fun <S1, S2, T1, T2> ScopedSuspendedState<S1, T1>.x(other: ScopedSuspendedState<S2, T2>): ScopedSuspendedState<Pair<S1,S2>, Pair<T1, T2>> =
    ScopedSuspendedState {
        pair ->
            val r1 = by(this@x)(pair.first)
            val r2 = by(other)(pair.second)
            Pair(Pair(r1.first,r2.first),Pair(r1.second,r2.second))
    }

@MathSpeakDsl
infix fun <B1,B2,S1,S2,T1,T2> KlScopedSuspendedState<B1,S1,T1>.x(other: KlScopedSuspendedState<B2,S2,T2>):KlScopedSuspendedState<Pair<B1,B2>,Pair<S1,S2>,Pair<T1,T2>> =
    KlScopedSuspendedState {
        pair -> by(this@x)(pair.first) x by(other)(pair.second)
    }