/*
 *  Copyright 2022-2025. Alcubierre Contributors
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

package space.octaone.alcubierre.render.modifier

import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import space.octaone.alcubierre.fragment.R
import space.octaone.alcubierre.screen.FragmentScreen

/**
 * Adds custom animations to a transaction.
 * Default constructor uses animations similar to the default animations from Jetpack Navigation.
 */
public class AnimationModifier(
    @AnimatorRes @AnimRes private val enter: Int,
    @AnimatorRes @AnimRes private val exit: Int,
    @AnimatorRes @AnimRes private val popEnter: Int,
    @AnimatorRes @AnimRes private val popExit: Int
) : FragmentTransactionModifier {

    public constructor(): this(
        R.animator.alcubierre_default_enter_anim, R.animator.alcubierre_default_exit_anim,
        R.animator.alcubierre_default_pop_enter_anim, R.animator.alcubierre_default_pop_exit_anim
    )

    override fun modify(
        transaction: FragmentTransaction,
        screen: FragmentScreen,
        fragment: Fragment
    ): FragmentTransaction =
        transaction.setCustomAnimations(enter, exit, popEnter, popExit)
}
