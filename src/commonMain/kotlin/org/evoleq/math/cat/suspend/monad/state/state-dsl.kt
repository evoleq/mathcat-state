/**
 * Copyright (c) 2020 Dr. Florian Schmidt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

 