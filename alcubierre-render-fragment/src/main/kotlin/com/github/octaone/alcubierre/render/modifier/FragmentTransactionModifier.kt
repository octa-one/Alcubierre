package com.github.octaone.alcubierre.render.modifier

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.github.octaone.alcubierre.screen.FragmentScreen

/**
 * Interface for [FragmentTransaction] modifier.
 * Possible use cases:
 * Configuring animation, eg [AnimationModifier].
 * Configuring shared element transitions.
 */
public interface FragmentTransactionModifier {

    /**
     * Called before committing [transaction].
     *
     * @param transaction [FragmentTransaction] to modify.
     * @param screen The screen from which the fragment was created in the current [transaction].
     * @param fragment The fragment in the current [transaction].
     * @return Modified [FragmentTransaction] to commit to the FragmentManager.
     */
    public fun modify(
        transaction: FragmentTransaction,
        screen: FragmentScreen,
        fragment: Fragment
    ): FragmentTransaction
}
