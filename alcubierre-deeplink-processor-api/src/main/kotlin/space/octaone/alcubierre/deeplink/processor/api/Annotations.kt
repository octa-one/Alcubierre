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

package space.octaone.alcubierre.deeplink.processor.api

/**
 * An annotation that allows you to specify a set of deeplinks that can be used to open the screen.
 * Only works with applied KSP processor!
 * ```
 * @Deeplink("myapp://feature/{id}")
 * @Deeplink("https://my.app/feature/{id}")
 * data class FeatureScreen(val id: String)
 * ```
 */
@Target(AnnotationTarget.CLASS)
@Repeatable
public annotation class Deeplink(val pattern: String)

/**
 * An annotation that allows you to give a class field a custom name contained in deeplink placeholders.
 * ```
 * @Deeplink("myapp://feature/{ID}")
 * data class FeatureScreen(@DeeplinkParam(name = "ID") val id: String)
 * ```
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
public annotation class DeeplinkParam(val name: String)
