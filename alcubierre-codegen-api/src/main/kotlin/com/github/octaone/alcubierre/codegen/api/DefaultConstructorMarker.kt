package com.github.octaone.alcubierre.codegen.api

/**
 * Маркер синтетического конструктора, поддерживающего default values
 */
val DEFAULT_CONSTRUCTOR_MARKER: Class<*> =
    Class.forName("kotlin.jvm.internal.DefaultConstructorMarker")
