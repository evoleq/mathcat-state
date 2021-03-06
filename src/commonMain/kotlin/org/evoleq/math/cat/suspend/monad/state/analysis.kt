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
import org.evoleq.math.cat.suspend.morphism.by

data class Report<O, R>(val  report: R, val output: O)

typealias Analysis<S, I, O, R> = KlScopedSuspendedState<S, I, Report<O, R>>
typealias Conclusion<S, O, R> = KlScopedSuspendedState<S, Report<O, R>, O>

/**
 * Constructor function for [Conclusion]
 */
@MathCatDsl
@Suppress("FunctionName")
fun <S, O, R> Conclusion(conclusion: suspend CoroutineScope.(Report<O, R>, S)->Pair<O, S>): Conclusion<S, O, R> = KlScopedSuspendedState<S, Report<O, R>, O> {
    report -> ScopedSuspendedState { state -> conclusion(report,state) }
}

/**
 * Analyze the output of a [ScopedSuspendedState], more precise, a [KlScopedSuspendedState]
 */
@MathCatDsl
suspend infix fun <S, I, O, R> KlScopedSuspendedState<S, I, O>.analyze(
    analysis: suspend CoroutineScope.(O)->R
): Analysis<S, I, O, R> = KlScopedSuspendedState { input ->
    by(this@analyze)(input).bind { output ->
        ScopedSuspendedState<S, Report<O, R>> { state ->
            Pair(Report(analysis(output), output), state)
        }
    }
}

/**
 * Analyze output and final state of a [ScopedSuspendedState], more precise, a [KlScopedSuspendedState]
 */
@MathCatDsl
suspend infix fun <S, I, O, R> KlScopedSuspendedState<S, I, O>.analyze(
    analysis: suspend CoroutineScope.(O, S)->R
): Analysis<S, I, O, R> = KlScopedSuspendedState { input ->
    by(this@analyze)(input).bind { output ->
        ScopedSuspendedState<S, Report<O, R>> { state ->
            Pair(Report(analysis(output, state), output), state)
        }
    }
}

/**
 * Draw conclusions from an [Analysis]
 */
@MathCatDsl
suspend fun <S, I, O, R> Analysis<S, I, O, R>.conclude(
    conclusion: suspend CoroutineScope.(Report<O, R>, S)->Pair<O, S>
): KlScopedSuspendedState<S, I, O> =
    this * Conclusion( conclusion )

/**
 * Complete an [Analysis] with an optional [Conclusion]
 */
@MathCatDsl
suspend fun <S, I, O, R> Analysis<S, I, O, R>.complete(
    conclusion: suspend CoroutineScope.(Report<O, R>, S)->Pair<O, S> = { report, state -> Pair(report.output,state)}
): KlScopedSuspendedState<S, I, O> =
    this * Conclusion( conclusion )