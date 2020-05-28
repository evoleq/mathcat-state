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