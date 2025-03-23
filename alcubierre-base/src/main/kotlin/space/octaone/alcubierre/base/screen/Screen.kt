package space.octaone.alcubierre.base.screen

import android.os.Parcelable
import space.octaone.alcubierre.base.annotation.AlcubierreInternalApi
import space.octaone.alcubierre.base.screen.extra.ExtrasContainer
import java.util.UUID

/**
 * Abstraction for describing a destination for navigation.
 *
 * Be aware:
 * The library only uses random [screenId] to compare screens.
 * This makes possible to add multiple identical screens to the backstack.
 *
 * Be careful when using the `object` modifier for screens.
 * Adding such object to the backstack multiple times may cause errors.
 */
public abstract class Screen : Parcelable, ExtrasContainer {

    public var screenId: String = UUID.randomUUID().toString()
        @AlcubierreInternalApi set
}
