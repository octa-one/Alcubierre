package com.github.octaone.alcubierre.screen

import android.os.Parcelable
import com.github.octaone.alcubierre.annotation.AlcubierreInternalApi
import com.github.octaone.alcubierre.screen.extra.ExtrasContainer
import java.util.UUID

/**
 * Base screen (specific destination for navigation)
 */
public abstract class Screen : Parcelable, ExtrasContainer {

    public var screenId: String = UUID.randomUUID().toString()
        @AlcubierreInternalApi set
}
