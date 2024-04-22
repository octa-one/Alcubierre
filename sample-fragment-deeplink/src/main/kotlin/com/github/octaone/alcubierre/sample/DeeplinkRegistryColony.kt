package com.github.octaone.alcubierre.sample

import com.github.octaone.alcubierre.deeplink.processor.api.DeeplinkRegistry
import com.joom.colonist.AcceptSettlersViaCallback
import com.joom.colonist.Colonist
import com.joom.colonist.Colony
import com.joom.colonist.OnAcceptSettler
import com.joom.colonist.ProduceSettlersViaConstructor
import com.joom.colonist.SelectSettlersBySuperType

@Colony
@SelectSettlersBySuperType(DeeplinkRegistry::class)
@ProduceSettlersViaConstructor
@AcceptSettlersViaCallback
@Target(AnnotationTarget.CLASS)
annotation class DeeplinkRegistryColony

@DeeplinkRegistryColony
class DeeplinkRegistryCollector {

    val registries = mutableListOf<DeeplinkRegistry>()

    init {
        Colonist.settle(this)
    }

    @OnAcceptSettler(colonyAnnotation = DeeplinkRegistryColony::class)
    fun accept(registry: DeeplinkRegistry) {
        registries += registry
    }
}
