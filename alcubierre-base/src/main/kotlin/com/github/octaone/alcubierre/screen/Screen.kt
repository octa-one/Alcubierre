package com.github.octaone.alcubierre.screen

import android.os.Parcelable

/**
 * Базовый класс экрана (конкретного назначения для навигации).
 */
interface Screen : Parcelable {
    val screenId: String
}
