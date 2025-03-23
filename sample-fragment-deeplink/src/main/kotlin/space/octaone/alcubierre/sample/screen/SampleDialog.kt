package space.octaone.alcubierre.sample.screen

import kotlinx.parcelize.Parcelize
import space.octaone.alcubierre.deeplink.processor.api.Deeplink
import space.octaone.alcubierre.sample.fragment.SampleDialogFragment
import space.octaone.alcubierre.screen.FragmentDialog

@Parcelize
@Deeplink("myapp://dialog")
class SampleDialog : FragmentDialog(SampleDialogFragment::class)
