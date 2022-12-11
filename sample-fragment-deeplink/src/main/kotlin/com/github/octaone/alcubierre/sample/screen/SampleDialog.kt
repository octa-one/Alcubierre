package com.github.octaone.alcubierre.sample.screen

import com.github.octaone.alcubierre.codegen.api.Deeplink
import com.github.octaone.alcubierre.sample.fragment.SampleDialogFragment
import com.github.octaone.alcubierre.screen.FragmentDialog
import kotlinx.parcelize.Parcelize

@Parcelize
@Deeplink("myapp://dialog")
object SampleDialog : FragmentDialog(SampleDialogFragment::class)