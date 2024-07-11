@file:Suppress("LeakingThis")

package com.github.octaone.alcubierre.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import androidx.savedstate.SavedStateRegistryOwner
import com.github.octaone.alcubierre.NavDriveOwner
import kotlin.reflect.KClass

/**
 * [Dialog] implementation for Compose.
 * Compared to some other navigation solutions, this library does not force a specific implementation of dialogs.
 * Because of this, you must provide the library with some implementation details, such as how to hide the dialog when the state changes.
 * See [HideRequest] for more information.
 *
 * @param composeContentName The fully qualified name of the [ComposeScreenContent] class.
 * @param composeContentClass The class of the [ComposeScreenContent], if it exists, matches [composeContentName].
 *
 * In some app architectures the [Dialog] classes used for navigation may be separate from its implementation ([ComposeDialogContent]).
 * In such case, you can pass a class name or a class of the content.
 * Once navigated, the content will be created using the default constructor via reflection.
 * The same idea is used in FragmentFactory.
 *
 * But if the dialog and content are placed in the same module, it's better to combine them into one class.
 * For example:
 * ```
 * class SomeDialog : ComposeDialog(), ComposeDialogContent<SomeDialog> () {
 *
 *     @Composable
 *     fun SomeScreen.Content(hideRequest: HideRequest, onDismissRequest: () -> Unit) {
 *         val state = rememberModalBottomSheetState()
 *         hideRequest.HideEffect { state.hide() } // This step is important!
 *     }
 * }
 * ```
 * Each dialog has its own [lifecycleManager], the implementation can be overridden by child classes.
 * The default implementation consists of [LifecycleOwner], [ViewModelStoreOwner], [SavedStateRegistryOwner].
 */
@Stable
public abstract class ComposeNameDialog(
    public val composeContentName: String?,
    public val composeContentClass: Class<out ComposeDialogContent<*>>?
) : ComposeDialog() {

    public constructor(contentName: String) : this(contentName, null)
    public constructor(contentClass: KClass<out ComposeDialogContent<*>>) : this(null, contentClass.java)

    internal var content: ComposeDialogContent<*>? = null

    @Composable
    final override fun Content(hideRequest: HideRequest, onDismissRequest: () -> Unit) {
        val classLoader = LocalContext.current.classLoader
        getContent(classLoader).GenericContent(this, hideRequest, onDismissRequest)
    }
}

/**
 * Separate interface for classes containing composable contents.
 * It is separate from [ComposeDialog] because in some app architectures
 * the Dialog classes used for navigation may be separate from its implementation.
 * If this is not the case, you can implement this interface in the Dialog class.
 */
public abstract class ComposeDialogContent<D : ComposeDialog> {

    /**
     * Content of the [ComposeDialog].
     * @param hideRequest see [HideRequest].
     * @param onDismissRequest callback, that can be
     * passed to any Dialog implementation (eg ModalBottomSheet) that requires it.
     * Internally, this callback calls [NavDriveOwner.requestDismissDialog].
     */
    @Composable
    public abstract fun Content(dialog: D, hideRequest: HideRequest, onDismissRequest: () -> Unit)
}
