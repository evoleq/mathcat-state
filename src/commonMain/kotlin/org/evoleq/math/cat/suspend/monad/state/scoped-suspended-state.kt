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
import kotlinx.coroutines.coroutineScope
import org.evoleq.math.cat.marker.MathCatDsl
import org.evoleq.math.cat.structure.x
import org.evoleq.math.cat.suspend.morphism.*

/**
 * Scoped suspended state
 */
interface ScopedSuspendedState<S, T> : ScopedSuspended<S, Pair<T, S>>

/**
 * Constructor function for the [ScopedSuspendedState]
 */
@MathCatDsl
@Suppress("FunctionName")
fun <S, T> ScopedSuspendedState(state: suspend CoroutineScope.(S)->Pair<T, S>): ScopedSuspendedState<S, T> = object : ScopedSuspendedState<S, T> {
    override val morphism: suspend CoroutineScope.(S) -> Pair<T, S>
        get() = state

}
/**********************************************************************************************************************
 *
 * Functorial structure
 *
 **********************************************************************************************************************/
/**
 * Map [ScopedSuspendedState]
 */
@MathCatDsl
suspend infix fun <S, T, T1> ScopedSuspendedState<S, T>.map(f: suspend CoroutineScope.(T) -> T1): ScopedSuspendedState<S, T1> = ScopedSuspendedState {
    s ->  ((f x by(Id<S>())) o by(this@map))(s)
}

/**********************************************************************************************************************
 *
 * Applicative structure
 *
 **********************************************************************************************************************/

/**
 * Apply function of the applicative [ScopedSuspendedState]
 */
@MathCatDsl
suspend fun <R, S, T> ScopedSuspendedState<R, suspend CoroutineScope.(S)->T>.apply(): suspend CoroutineScope.(ScopedSuspendedState<R, S>)->ScopedSuspendedState<R, T> = {
    state -> this@apply bind {f -> state map f}
}

/**
 * Apply function of the applicative [ScopedSuspendedState]
 */
@MathCatDsl
suspend infix fun <R, S, T> ScopedSuspendedState<R, suspend CoroutineScope.(S)->T>.apply(
    next: ScopedSuspendedState<R, S>
): ScopedSuspendedState<R, T> =
    coroutineScope { this@apply.apply()(next) }


/**********************************************************************************************************************
 *
 * Monadic structure
 *
 **********************************************************************************************************************/
/**
 * Return function of the [ScopedSuspendedState] monad
 */
@Suppress("FunctionName")
@MathCatDsl
fun <S, T> ReturnState(t: T): ScopedSuspendedState<S, T> = ScopedSuspendedState {
    s -> Pair(t,s)
}

/**
 * Multiplication of the [ScopedSuspendedState] monad
 */
@MathCatDsl
fun <S, T> ScopedSuspendedState<S, ScopedSuspendedState<S, T>>.multiply() : ScopedSuspendedState<S, T> =
    ScopedSuspendedState { s -> by(this@multiply)(s).evaluate() }

/**
 * Bind function of the [ScopedSuspendedState] monad
 */
@MathCatDsl
suspend infix fun <S, T, U> ScopedSuspendedState<S, T>.bind(
    arrow: suspend CoroutineScope.(T)->ScopedSuspendedState<S, U>
): ScopedSuspendedState<S, U> = (this map arrow).multiply()

/**
 * Kleisli [ScopedSuspendedState]
 */
interface KlScopedSuspendedState<B, S, T> : ScopedSuspended<S, ScopedSuspendedState<B, T>> {
    /**
     * [KlScopedSuspendedState] is functorial, i.e. for fixed S, I the map T -> KlScopedSuspendedState<S, I T> is a functor.
     * This is true because this map is just a composition Hom_i ° State<S, _> of the hom- and the state-functor
     */
    @MathCatDsl
    suspend infix fun <S, I, O1, O2> KlScopedSuspendedState<S, I, O1>.map(f: suspend CoroutineScope.(O1)->O2): KlScopedSuspendedState<S, I , O2> =
        KlScopedSuspendedState {
            input -> by(this@map)(input) map f
        }
}

/**
 * Constructor function for [KlScopedSuspendedState]
 */
@MathCatDsl
@Suppress("FunctionName")
fun<B, S, T>  KlScopedSuspendedState(arrow: suspend CoroutineScope.(S)-> ScopedSuspendedState<B, T>): KlScopedSuspendedState<B, S, T> = object : KlScopedSuspendedState<B, S, T> {
    override val morphism: suspend CoroutineScope.(S) -> ScopedSuspendedState<B, T>
        get() = arrow
}

/**
 * Identity element of the [ScopedSuspendedState] monad
 */
@Suppress("FunctionName")
@MathCatDsl
fun <B, S> KlReturnState(): KlScopedSuspendedState<B, S, S> = KlScopedSuspendedState {
    s -> ReturnState(s)
}

/**
 * Multiplication of the [ScopedSuspendedState] monad
 */
suspend operator fun <B, R, S, T> KlScopedSuspendedState<B, R, S>.times(other: KlScopedSuspendedState<B, S, T>): KlScopedSuspendedState<B, R, T> =
    KlScopedSuspendedState {
            r -> by(this@times)(r).map(by(other)).multiply()
    }