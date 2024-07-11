package com.github.octaone.alcubierre.screen

import androidx.compose.runtime.Composable

internal fun ComposeNameScreen.getContent(classLoader: ClassLoader): ComposeScreenContent<*> {
    val savedContent = content
    if (savedContent != null) return savedContent

    val contentClass = composeContentClass ?: Class.forName(requireNotNull(composeContentName), false, classLoader)
    val content = contentClass.getDeclaredConstructor().newInstance() as ComposeScreenContent<*>

    this.content = content
    return content
}

internal fun ComposeNameDialog.getContent(classLoader: ClassLoader): ComposeDialogContent<*> {
    val savedContent = content
    if (savedContent != null) return savedContent

    val contentClass = composeContentClass ?: Class.forName(requireNotNull(composeContentName), false, classLoader)
    val content = contentClass.getDeclaredConstructor().newInstance() as ComposeDialogContent<*>

    this.content = content
    return content
}

@Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE")
@Composable
internal inline fun <D : ComposeDialog> ComposeDialogContent<D>.GenericContent(
    dialog: ComposeDialog,
    hideRequest: HideRequest,
    noinline onDismissRequest: () -> Unit
) {
    Content(dialog as D, hideRequest, onDismissRequest)
}

@Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE")
@Composable
internal inline fun <S : ComposeScreen> ComposeScreenContent<S>.GenericContent(
    screen: ComposeScreen
) {
    Content(screen as S)
}
