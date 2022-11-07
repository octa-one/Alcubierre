package com.github.octaone.alcubierre.render.modifier

import com.github.octaone.alcubierre.screen.FragmentScreen
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

object EmptyModifier : FragmentTransactionModifier {

    override fun modify(
        transaction: FragmentTransaction,
        screen: FragmentScreen,
        fragment: Fragment
    ): FragmentTransaction = transaction
}
