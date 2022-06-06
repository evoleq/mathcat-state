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
package org.evoleq.math.cat.monad.state

import org.evoleq.math.cat.marker.MathCatDsl
import org.evoleq.math.cat.morphism.Id
import org.evoleq.math.cat.morphism.Morphism
import org.evoleq.math.cat.morphism.by
import org.evoleq.math.cat.morphism.o
import org.evoleq.math.cat.structure.x


/**
 * Scoped suspended state
 */
interface State<S, T> : Morphism<S, Pair<T, S>>

/**
 * Constructor function for the [State]
 */
@MathCatDsl
@Suppress("FunctionName")
fun <S, T> State(state: (S)->Pair<T, S>): State<S, T> = object : State<S, T> {
    override val morphism: (S) -> Pair<T, S>
        get() = state

}
/**********************************************************************************************************************
 *
 * Functorial structure
 *
 **********************************************************************************************************************/
/**
 * Map [State]
 */
@MathCatDsl
infix fun <S, T, T1> State<S, T>.map(f: (T) -> T1): State<S, T1> = State {
        s ->  ((f x by(Id<S>())) o by(this@map))(s)
}

/**********************************************************************************************************************
 *
 * Applicative structure
 *
 **********************************************************************************************************************/

/**
 * Apply function of the applicative [State]
 */
@MathCatDsl
fun <R, S, T> State<R, (S)->T>.apply(): (State<R, S>)->State<R, T> = {
        state -> this@apply bind {f -> state map f}
}

/**
 * Apply function of the applicative [State]
 */
@MathCatDsl
infix fun <R, S, T> State<R,(S)->T>.apply(
    next: State<R, S>
): State<R, T> =
   this@apply.apply()(next)


/**********************************************************************************************************************
 *
 * Monadic structure
 *
 **********************************************************************************************************************/
/**
 * Return function of the [State] monad
 */
@Suppress("FunctionName")
@MathCatDsl
fun <S, T> ReturnState(t: T): State<S, T> = State {
        s -> Pair(t,s)
}

/**
 * Multiplication of the [State] monad
 */
@MathCatDsl
fun <S, T> State<S, State<S, T>>.multiply() : State<S, T> =
    State { s -> by(this@multiply)(s).evaluate() }

/**
 * Bind function of the [State] monad
 */
@MathCatDsl
infix fun <S, T, U> State<S, T>.bind(
    arrow: (T)->State<S, U>
): State<S, U> = (this map arrow).multiply()

/**
 * Kleisli [State]
 */
interface KlState<B, S, T> : Morphism<S, State<B, T>> {

}

/**
 * Constructor function for [KlState]
 */
@MathCatDsl
@Suppress("FunctionName")
fun<B, S, T>  KlState(arrow: (S)-> State<B, T>): KlState<B, S, T> = object : KlState<B, S, T> {
    override val morphism: (S) -> State<B, T>
        get() = arrow
}

/**
 * [KlState] is functorial, i.e. for fixed S, I the map T -> KlState<S, I T> is a functor.
 * This is true because this map is just a composition Hom_i ° State<S, _> of the hom- and the state-functor
 */
@MathCatDsl
infix fun <S, I, O1, O2> KlState<S, I, O1>.map(f: (O1)->O2): KlState<S, I , O2> =
    KlState {
            input -> by(this@map)(input) map f
    }

/**
 * Identity element of the [State] monad
 */
@Suppress("FunctionName")
@MathCatDsl
fun <B, S> KlReturnState(): KlState<B, S, S> = KlState {
        s -> ReturnState(s)
}

/**
 * Multiplication of the [State] monad
 */
operator fun <B, R, S, T> KlState<B, R, S>.times(other: KlState<B, S, T>): KlState<B, R, T> =
    KlState {
            r -> by(this@times)(r).map(by(other)).multiply()
    }