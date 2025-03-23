package space.octaone.alcubierre.condition

import space.octaone.alcubierre.base.action.AnyNavAction
import space.octaone.alcubierre.base.action.NavAction
import space.octaone.alcubierre.base.state.AnyRootNavState

/**
 * An interface for classes that encapsulate conditional navigation logic.
 */
public interface NavCondition {

    /**
     * Condition resolving function.
     * @param target [ConditionalTarget] on which this [NavCondition] has been triggered.
     * @param state Current state of navigation. For example, you can use it to check currently visible screen.
     * @return [NavAction] to be performed as a result of condition evaluation. Can be null if nothing should happen.
     */
    public fun resolve(target: ConditionalTarget, state: AnyRootNavState): AnyNavAction?
}

/**
 * Factory for instantiating [NavCondition] from its class.
 * @see DefaultNavConditionFactory
 */
public interface NavConditionFactory {

    /**
     * Create a new instance of a [NavCondition].
     */
    public fun create(conditionalTarget: ConditionalTarget): NavCondition
}
