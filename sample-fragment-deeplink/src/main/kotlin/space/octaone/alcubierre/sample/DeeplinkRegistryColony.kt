/*
 *  Copyright 2022-2025. Alcubierre Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
