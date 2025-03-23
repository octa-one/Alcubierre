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

package space.octaone.alcubierre.util

import space.octaone.alcubierre.base.annotation.AlcubierreInternalApi
import java.util.Collections
import kotlin.reflect.KClass

@AlcubierreInternalApi
public inline fun <reified V : Any> Map<KClass<*>, *>.getAndCast(): V? = get(V::class) as? V

internal fun <K, V> Map<K, V>.optimizeReadOnlyMap() = when (size) {
    0 -> emptyMap()
    1 -> with(iterator().next()) { Collections.singletonMap(key, value) }
    else -> this
}
