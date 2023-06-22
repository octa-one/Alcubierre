package com.github.octaone.alcubierre.sample.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.octaone.alcubierre.FragmentNavDrive
import com.github.octaone.alcubierre.action.dismissDialog
import com.github.octaone.alcubierre.action.showDialog
import com.github.octaone.alcubierre.sample.Counter
import com.github.octaone.alcubierre.sample.R
import com.github.octaone.alcubierre.sample.SampleApplication
import com.github.octaone.alcubierre.sample.databinding.DialogSampleBinding
import com.github.octaone.alcubierre.sample.screen.SampleDialog
import com.github.octaone.alcubierre.screen.dialogData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.random.Random

class SampleDialogFragment : BottomSheetDialogFragment() {

    private val navDrive: FragmentNavDrive
        get() = SampleApplication.from(requireContext()).navDrive
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