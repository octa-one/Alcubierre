package com.github.octaone.alcubierre.render.modifier

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.github.octaone.alcubierre.screen.FragmentScreen

public interface FragmentTransactionModifier {

    public fun modify(
        transaction: FragmentTransaction,
        screen: FragmentScreen,
        fragment: Fragment
    ): FragmentTransaction
}
