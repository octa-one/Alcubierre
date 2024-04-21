package com.github.octaone.alcubierre.sample.screen

import com.github.octaone.alcubierre.deeplink.processor.api.Deeplink
import com.github.octaone.alcubierre.sample.fragment.SampleDialogFragment
import com.github.octaone.alcubierre.screen.FragmentDialog
import kotlinx.parcelize.Parcelize

@Parcelize
@Deeplink("myapp://dialog")
class SampleDialog : FragmentDialog(SampleDialogFragment::class)
