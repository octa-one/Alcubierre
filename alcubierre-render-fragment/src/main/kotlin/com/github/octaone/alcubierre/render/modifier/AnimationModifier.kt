package com.github.octaone.alcubierre.render.modifier

import com.github.octaone.alcubierre.screen.FragmentScreen
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.github.octaone.alcubierre.fragment.R

class AnimationModifier(
    @AnimatorRes @AnimRes private val enter: Int,
    @AnimatorRes @AnimRes private val exit: Int,
    @AnimatorRes @AnimRes private val popEnter: Int,
    @AnimatorRes @AnimRes private val popExit: Int
) : FragmentTransactionModifier {

    constructor(): this(
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
