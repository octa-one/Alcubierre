package com.github.octaone.alcubierre.deeplink.processor

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

public class DeeplinkProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): DeeplinkProcessor =
        DeeplinkProcessor(environment)
}
