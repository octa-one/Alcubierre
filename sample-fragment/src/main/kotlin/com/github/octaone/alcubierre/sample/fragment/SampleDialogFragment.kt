package com.github.octaone.alcubierre.sample.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.octaone.alcubierre.NavDrive
import com.github.octaone.alcubierre.action.dismissDialog
import com.github.octaone.alcubierre.sample.R
import com.github.octaone.alcubierre.sample.SampleApplication
import com.github.octaone.alcubierre.sample.databinding.DialogSampleBinding
import com.github.octaone.alcubierre.sample.screen.SampleDialog
import com.github.octaone.alcubierre.screen.dialogData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SampleDialogFragment : BottomSheetDialogFragment() {

    private val navDrive: NavDrive get() = SampleApplication.from(requireContext()).navDrive
    private val dialog : SampleDialog by dialogData()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.dialog_sample, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = DialogSampleBinding.bind(view)

        binding.text.text = dialog.someId.toString()

        binding.btnDismiss.setOnClickListener {
            navDrive.dismissDialog()
        }
    }
}