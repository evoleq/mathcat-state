package org.evoleq.math.cat.suspend.monad.state
/*
import org.evoleq.math.cat.suspend.morphism.KlScopedSuspended
import org.evoleq.math.cat.suspend.morphism.ScopedSuspended
import org.evoleq.math.cat.suspend.morphism.by

suspend operator fun <S, T, U> ScopedSuspendedState<S, T>.times(other: KlScopedSuspendedState<T,S, U>): ScopedSuspended<S, Pair<U,T>> =
    ScopedSuspended {
        s -> with(by(this@times)(s)){
            by(by(other)(second))(first)
        }
    }

suspend fun <S, I, T, U > KlScopedSuspendedState<S,I,T>.transfer(other: KlScopedSuspendedState<T, S, U>): KlScopedSuspended<S, I,Pair<U, T>> = KlScopedSuspended {
    i: I -> ScopedSuspended { s: S ->
        with(by(by(this@transfer)(i))(s)){
            by(by(other)(second))(first)
        }
    }
}

suspend fun <S,I,T, U, V> KlScopedSuspended<S, I,Pair<U, T>>.act(other: KlScopedSuspendedState<T, U, V>): KlScopedSuspended<S, I, Pair<V, T>> = KlScopedSuspended {
    i: I -> ScopedSuspended { s: S ->
        with(by(by(this@act)(i))(s)) {
            by(by(other)(first))(second)
        }
    }
}

 */

 