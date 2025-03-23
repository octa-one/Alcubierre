package space.octaone.alcubierre.sample

import com.joom.colonist.AcceptSettlersViaCallback
import com.joom.colonist.Colonist
import com.joom.colonist.Colony
import com.joom.colonist.OnAcceptSettler
import com.joom.colonist.ProduceSettlersViaConstructor
import com.joom.colonist.SelectSettlersBySuperType
import space.octaone.alcubierre.deeplink.processor.api.DeeplinkRegistry

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
