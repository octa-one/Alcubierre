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

package space.octaone.alcubierre.sample.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import space.octaone.alcubierre.FragmentNavDrive
import space.octaone.alcubierre.action.dismissDialog
import space.octaone.alcubierre.action.showDialog
import space.octaone.alcubierre.sample.Counter
import space.octaone.alcubierre.sample.R
import space.octaone.alcubierre.sample.SampleApplication
import space.octaone.alcubierre.sample.databinding.DialogSampleBinding
import space.octaone.alcubierre.sample.screen.SampleDialog
import space.octaone.alcubierre.screen.dialogData
import kotlin.random.Random

class SampleDialogFragment : BottomSheetDialogFragment() {

    private val navDrive: FragmentNavDrive
        get() = SampleApplication.Companion.from(requireContext()).navDrive
    private val dialog : SampleDialog by dialogData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentFragmentManager.setFragmentResult("state", Bundle.EMPTY)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.dialog_sample, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = DialogSampleBinding.bind(view)

        binding.text.text = "${dialog.someId} (p=${dialog.priority})"

        binding.btnDismiss.setOnClickListener {
            navDrive.dismissDialog()
        }

        binding.btnShowDialog.setOnClickListener {
            navDrive.showDialog(SampleDialog(Counter.increment(), Random.nextInt(0, 10)))
            parentFragmentManager.setFragmentResult("state", Bundle.EMPTY)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        parentFragmentManager.setFragmentResult("state", Bundle.EMPTY)
    }
}
