package com.github.octaone.alcubierre.sample.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.octaone.alcubierre.LocalNavDrive
import com.github.octaone.alcubierre.action.dismissDialog
import com.github.octaone.alcubierre.action.showDialog
import com.github.octaone.alcubierre.sample.Counter
import com.github.octaone.alcubierre.screen.ComposeDialogContent
import com.github.octaone.alcubierre.screen.ComposeNameDialog
import com.github.octaone.alcubierre.screen.HideRequest
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

@Parcelize
data class SampleDialog(
    val someId: Int,
    override val priority: Int
) : ComposeNameDialog(SampleDialogContent::class)

class SampleDialogContent : ComposeDialogContent<SampleDialog>() {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content(dialog: SampleDialog, hideRequest: HideRequest, onDismissRequest: () -> Unit) {
        val sheetState = rememberModalBottomSheetState()
        hideRequest.HideEffect { sheetState.hide() }

        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = onDismissRequest
        ) {
            val navDrive = LocalNavDrive.current
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 32.dp)
            ) {
                Text(text = "${dialog.someId} (p=${dialog.priority})")
                TextButton(
                    modifier = MaxWidthModifier,
                    onClick = { navDrive.dismissDialog() }
                ) {
                    Text(text = "Dismiss")
                }
                TextButton(
                    modifier = MaxWidthModifier,
                    onClick = {
                        navDrive.showDialog(SampleDialog(Counter.increment(), Random.nextInt(0, 10)))
                    }
                ) {
                    Text(text = "Show one more dialog")
                }
            }
        }
    }
}

private val MaxWidthModifier = Modifier.fillMaxWidth()
