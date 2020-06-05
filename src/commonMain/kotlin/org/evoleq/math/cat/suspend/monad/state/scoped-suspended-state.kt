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
import org.evoleq.math.cat.marker.MathCatDsl
import org.evoleq.math.cat.suspend.morphism.ScopedSuspended
import org.evoleq.math.cat.suspend.morphism.by


interface ScopedSuspendedState<S, T> : ScopedSuspended<S, Pair<T, S>> {
    suspend infix fun <T1> map(f: suspend CoroutineScope.(T) -> T1): ScopedSuspendedState<S, T1> = ScopedSuspendedState {
            s -> with(by(this@ScopedSuspendedState)(s)){
            Pair(f(first),second)
        }
    }
    
    suspend fun <U> bind(arrow: suspend CoroutineScope.(T)->ScopedSuspendedState<S, U>): ScopedSuspendedState<S, U> = (this map arrow).multiply()
}
@Suppress("FunctionName")
@MathCatDsl
fun <S, T> ScopedSuspendedState(state: suspend CoroutineScope.(S)->Pair<T, S>): ScopedSuspendedState<S, T> = object : ScopedSuspendedState<S, T> {
    override val morphism: suspend CoroutineScope.(S) -> Pair<T, S>
        get() = state

}
@Suppress("FunctionName")
@MathCatDsl
fun <S, T> ReturnState(t: T): ScopedSuspendedState<S, T> = ScopedSuspendedState {
    s -> Pair(t,s)
}

@MathCatDsl
fun <S, T> ScopedSuspendedState<S, ScopedSuspendedState<S, T>>.multiply() : ScopedSuspendedState<S, T> = ScopedSuspendedState {
        s -> with(by(this@multiply)(s)){
    by(first)(second)
}
}

interface KlScopedSuspendedState<B, S, T> : ScopedSuspended<S, ScopedSuspendedState<B, T>>

@MathCatDsl
@Suppress("FunctionName")
fun<B, S, T>  KlScopedSuspendedState(arrow: suspend CoroutineScope.(S)-> ScopedSuspendedState<B, T>): KlScopedSuspendedState<B, S, T> = object : KlScopedSuspendedState<B, S, T> {
    override val morphism: suspend CoroutineScope.(S) -> ScopedSuspendedState<B, T>
        get() = arrow
}

@Suppress("FunctionName")
@MathCatDsl
fun <B, S> KlReturnState(): KlScopedSuspendedState<B, S, S> = KlScopedSuspendedState {
    s -> ReturnState(s)
}

suspend operator fun <B, R, S, T> KlScopedSuspendedState<B, R, S>.times(other: KlScopedSuspendedState<B, S, T>): KlScopedSuspendedState<B, R, T> =
    KlScopedSuspendedState {
            r -> by(this@times)(r).map(by(other)).multiply()
    }