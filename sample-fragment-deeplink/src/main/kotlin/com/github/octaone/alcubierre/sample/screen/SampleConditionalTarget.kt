package com.github.octaone.alcubierre.sample.screen

import com.github.octaone.alcubierre.action.AnyNavAction
import com.github.octaone.alcubierre.action.Forward
import com.github.octaone.alcubierre.condition.ConditionalTarget
import com.github.octaone.alcubierre.condition.NavCondition
import com.github.octaone.alcubierre.deeplink.processor.api.Deeplink
import com.github.octaone.alcubierre.state.AnyRootNavState

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
