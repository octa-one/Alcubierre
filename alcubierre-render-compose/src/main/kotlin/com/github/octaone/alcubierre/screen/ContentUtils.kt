package com.github.octaone.alcubierre.screen

import androidx.compose.runtime.Composable

internal fun ComposeScreen.getContent(classLoader: ClassLoader): ComposeScreenContent<*> {
    val savedContent = content
    if (savedContent != null) return savedContent

    val content = if (this is ComposeScreenContent<*>) {
        this
    } else {
        val contentClass = composeContentClass ?: Class.forName(composeContentName, false, classLoader)
        contentClass.getDeclaredConstructor().newInstance() as ComposeScreenContent<*>
    }

    this.content = content
    return content
}

internal fun ComposeDialog.getContent(classLoader: ClassLoader): ComposeDialogContent<*> {
    val savedContent = content
    if (savedContent != null) return savedContent

    val content = if (this is ComposeDialogContent<*>) {
        this
    } else {
        val contentClass = composeContentClass ?: Class.forName(composeContentName, false, classLoader)
        contentClass.getDeclaredConstructor().newInstance() as ComposeDialogContent<*>
    }

    this.content = content
    return content
}

@Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE")
@Composable
internal inline fun <D : ComposeDialog> ComposeDialogContent<D>.Content(
    dialog: ComposeDialog,
    noinline onDismissRequest: () -> Unit
) {
    (dialog as D).Content(dialog.hideRequest, onDismissRequest)
}

@Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE")
@Composable
internal inline fun <S : ComposeScreen> ComposeScreenContent<S>.Content(
    screen: ComposeScreen
) {
    (screen as S).Content()
}
