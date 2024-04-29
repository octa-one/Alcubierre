package com.github.octaone.alcubierre.render

import com.github.octaone.alcubierre.screen.ComposeScreen

/**
 * Inputs for a transition.
 * @param initialScreen The current screen to be replaced.
 * @param targetScreen The target animation screen.
 * Both fields are null, which means there is no screen in the initial or target state.
 *
 * You can customize the transition based on specific screens.
 */
public class ScreenTransitionScope(
    public val initialScreen: ComposeScreen?,
    public val targetScreen: ComposeScreen?
)
