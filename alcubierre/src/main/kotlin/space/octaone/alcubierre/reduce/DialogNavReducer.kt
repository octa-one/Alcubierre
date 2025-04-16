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

package space.octaone.alcubierre.reduce

import space.octaone.alcubierre.action.DismissDialog
import space.octaone.alcubierre.action.ShowDialog
import space.octaone.alcubierre.core.action.AnyNavAction
import space.octaone.alcubierre.core.reduce.NavReducer
import space.octaone.alcubierre.core.screen.Dialog
import space.octaone.alcubierre.core.state.AnyDialogNavState
import space.octaone.alcubierre.core.state.DialogNavState
import space.octaone.alcubierre.util.optimizeReadOnlyList

/**
 * [space.octaone.alcubierre.core.reduce.NavReducer] for dialog specific actions. Responsible for [DialogNavState].
 *
 * [ShowDialog] action inserts a new dialog into the queue based on [Dialog.priority],
 * but the current visible dialog will not be closed even if it has a lower priority than the new one.
 * Visible dialog has the highest priority value.
 *
 * [DismissDialog] action simply removes current (first) dialog from the queue.
 */
public class DialogNavReducer : NavReducer<AnyDialogNavState> {

    override fun reduce(state: AnyDialogNavState, action: AnyNavAction): AnyDialogNavState = when (action) {
        is ShowDialog -> {
            if (state.queue.isEmpty()) {
                state.copy(queue = listOf(action.dialog))
            } else {
                var insertIndex = state.queue.indexOfFirst { it.priority < action.dialog.priority && !it.isShowing}
                if (insertIndex == -1) insertIndex = state.queue.size
                state.modifyQueue { add(insertIndex, action.dialog) }
            }
        }
        is DismissDialog -> {
            if (state.queue.size <= 1) {
                DialogNavState.EMPTY
            } else {
                state.modifyQueue { removeAt(0) }
            }
        }
        else -> state
    }

    private inline fun AnyDialogNavState.modifyQueue(update: MutableList<Dialog>.() -> Unit): AnyDialogNavState =
        copy(queue = queue.toMutableList().apply(update).optimizeReadOnlyList())
}
