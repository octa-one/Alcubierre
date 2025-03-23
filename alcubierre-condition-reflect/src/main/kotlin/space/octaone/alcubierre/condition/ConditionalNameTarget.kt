package space.octaone.alcubierre.condition

/**
 * “Unsafe” version of [ConditionalTarget], allowing the fully qualified name to be used to reference the [NavCondition] class.
 */
public abstract class ConditionalNameTarget(
    conditionName: String
) : ConditionalTarget(conditionName, null)