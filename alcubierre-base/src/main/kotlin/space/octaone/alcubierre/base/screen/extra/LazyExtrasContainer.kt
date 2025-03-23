package space.octaone.alcubierre.base.screen.extra

/**
 * Lazy implementation of [ExtrasContainer].
 */
public class LazyExtrasContainer : ExtrasContainer {

    private val lazyExtras = lazy(LazyThreadSafetyMode.NONE) {
        BundleParcelableExtras()
    }

    override val extras: ParcelableExtras get() = lazyExtras.value

    override fun hasExtras(): Boolean = lazyExtras.isInitialized()
}
