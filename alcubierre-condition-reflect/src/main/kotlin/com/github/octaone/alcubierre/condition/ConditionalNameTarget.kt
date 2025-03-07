@file:OptIn(AlcubierreConditionalNameConstructor::class)

package com.github.octaone.alcubierre.condition

import com.github.octaone.alcubierre.condition.annotation.AlcubierreConditionalNameConstructor

/**
 * “Unsafe” version of [ConditionalTarget], allowing the fully qualified name to be used to reference the [NavCondition] class.
 */
public abstract class ConditionalNameTarget(
    conditionName: String
) : ConditionalTarget(conditionName, null)
