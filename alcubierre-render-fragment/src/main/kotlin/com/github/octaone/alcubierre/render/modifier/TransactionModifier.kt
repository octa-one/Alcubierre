package com.github.octaone.alcubierre.render.modifier

import com.github.octaone.alcubierre.screen.FragmentScreen
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

interface TransactionModifier {

    fun modify(
        transaction: FragmentTransaction,
        screen: FragmentScreen,
        fragment: Fragment
    ): FragmentTransaction
}
