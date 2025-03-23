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

package space.octaone.alcubierre.base.state

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.WriteWith
import space.octaone.alcubierre.base.screen.Dialog
import space.octaone.alcubierre.base.screen.internal.DialogParceler

/**
 * State of a dialogs queue.
 *
 * @param queue Dialogs queue, if multiple dialog actions were called at the same time.
 * The first dialog in the queue is the one the user sees.
 * Default reducer fills the queue based on the priority field.
 *
 * [equals] only uses [Dialog.dialogId] to compare dialogs.
 */
@Parcelize
public data class DialogNavState<out D : Dialog>(
    val queue: List<@WriteWith<DialogParceler> D>
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DialogNavState<*>

        if (queue.size != other.queue.size) return false
        for (i in queue.indices) {
            if (queue[i].dialogId != other.queue[i].dialogId) return false
        }

        return true
    }

    override fun hashCode(): Int {
        var result = 1
        for (i in queue.indices) result = 31 * result + queue[i].dialogId.hashCode()
        return result
    }

    public companion object {
        public val EMPTY: DialogNavState<Nothing> = DialogNavState(emptyList())
    }
}
