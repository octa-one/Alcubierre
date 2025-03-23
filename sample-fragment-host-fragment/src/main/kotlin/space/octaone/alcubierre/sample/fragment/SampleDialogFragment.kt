package space.octaone.alcubierre.sample.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import space.octaone.alcubierre.action.dismissDialog
import space.octaone.alcubierre.action.showDialog
import space.octaone.alcubierre.findNavDrive
import space.octaone.alcubierre.sample.Counter
import space.octaone.alcubierre.sample.R
import space.octaone.alcubierre.sample.databinding.DialogSampleBinding
import space.octaone.alcubierre.sample.screen.SampleDialog
import space.octaone.alcubierre.screen.dialogData
import kotlin.random.Random

class SampleDialogFragment : BottomSheetDialogFragment() {

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
            findNavDrive().dismissDialog()
        }

        binding.btnShowDialog.setOnClickListener {
            findNavDrive().showDialog(SampleDialog(Counter.increment(), Random.nextInt(0, 10)))
            parentFragmentManager.setFragmentResult("state", Bundle.EMPTY)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        parentFragmentManager.setFragmentResult("state", Bundle.EMPTY)
    }
}
