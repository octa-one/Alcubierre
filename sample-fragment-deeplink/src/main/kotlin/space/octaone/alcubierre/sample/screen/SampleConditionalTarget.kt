package space.octaone.alcubierre.sample.screen

import space.octaone.alcubierre.action.Forward
import space.octaone.alcubierre.base.action.AnyNavAction
import space.octaone.alcubierre.base.state.AnyRootNavState
import space.octaone.alcubierre.condition.ConditionalTarget
import space.octaone.alcubierre.condition.NavCondition
import space.octaone.alcubierre.deeplink.processor.api.Deeplink

@Deeplink("myapp://sample/condition?id={id}")
class SampleConditionalTarget(
    val id: Int
) : ConditionalTarget(SampleNavCondition::class)

class SampleNavCondition : NavCondition {

    override fun resolve(
        target: ConditionalTarget,
        state: AnyRootNavState,
    ): AnyNavAction {
        target as SampleConditionalTarget

        return Forward(listOf(SampleScreen(target.id * -1)))
    }
}
