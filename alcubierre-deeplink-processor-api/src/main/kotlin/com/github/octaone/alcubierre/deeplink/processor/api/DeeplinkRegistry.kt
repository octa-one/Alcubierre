package com.github.octaone.alcubierre.deeplink.processor.api

/**
 * Registry for all [ScreenConverter], declared in the module.
 * Implementations will be generated with Alcubierre deeplink processor.
 */
public interface DeeplinkRegistry {

    public val screenConverters: Map<String, ScreenConverter>
}
