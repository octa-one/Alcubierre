package space.octaone.alcubierre

import space.octaone.alcubierre.base.NavDrive
import space.octaone.alcubierre.base.NavDriveOwner
import space.octaone.alcubierre.screen.ComposeDialog
import space.octaone.alcubierre.screen.ComposeScreen

/**
 * [NavDrive] for Compose navigation.
 */
public typealias ComposeNavDrive = NavDrive<ComposeScreen, ComposeDialog>

/**
 * [NavDriveOwner] for Compose navigation.
 */
public typealias ComposeNavDriveOwner = NavDriveOwner<ComposeScreen, ComposeDialog>
