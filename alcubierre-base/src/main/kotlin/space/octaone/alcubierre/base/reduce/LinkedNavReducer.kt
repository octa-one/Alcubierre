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

package space.octaone.alcubierre.base.reduce

import space.octaone.alcubierre.base.annotation.AlcubierreInternalApi

/**
 * Linked list implementation of [NavReducer].
 * The reduce starts at the head and traverses to the tail.
 *
 * Each reducer has access to the head to have an ability to restart the entire chain.
 * As an example, there's a chain:
 * HeadReducer -> ForwardReducer -> DeeplinkReducer
 * DeeplinkReducer waits for an action with the deeplink, processes it and triggers a chain of reducers with Forward action.
 * But there are no more reducers in the downstream, so, Forward reducing is impossible.
 * This is the case when it is safer to restart the chain from the head with a new action.
 */
public abstract class LinkedNavReducer<S> : NavReducer<S> {

    /**
     * First reducer in the chain.
     */
    public lateinit var head: NavReducer<S>
        @AlcubierreInternalApi set

    /**
     * Next reducer in the chain.
     */
    public lateinit var next: NavReducer<S>
        @AlcubierreInternalApi set
}
