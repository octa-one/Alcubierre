package com.github.octaone.alcubierre.screen.extra

/**
 * Container for generic data to add new values to the Screen or Dialog.
 */
public interface ExtrasContainer {

    public val extras: ParcelableExtras

    public fun hasExtras(): Boolean
}