@file:Suppress("LocalVariableName")

package com.github.octaone.alcubierre.deeplink.processor.api

/**
 * Base class for a converter from a set of placeholders to an actual navigation target.
 * Implementations will be generated with Alcubierre deeplink processor.
 * As an example,
 * mapOf("id" to 1, "action" to "SayHello") can be converter to ComposeScreen(id = 1, action = "SayHello").
 */
public interface ScreenConverter {

    /**
     * @param _from the set of placeholders obtained by mapping a deeplink to [Deeplink.pattern].
     *
     * @return target of matched Deeplink annotation.
     * It can be Screen, Dialog, ConditionTarget or any custom processor-supported classes.
     * @throws Exception if string values of placeholders cannot be converted to parameter types of the class being converted.
     */
    public fun convert(_from: Map<String, String>): Any
}
