package space.octaone.alcubierre

import space.octaone.alcubierre.base.NavDrive
import space.octaone.alcubierre.base.NavDriveOwner
import space.octaone.alcubierre.screen.FragmentDialog
import space.octaone.alcubierre.screen.FragmentScreen

/**
 * [NavDrive] for fragment navigation.
 */
public typealias FragmentNavDrive = NavDrive<FragmentScreen, FragmentDialog>

/**
 * [NavDriveOwner] for fragment navigation.
 */
public typealias FragmentNavDriveOwner = NavDriveOwner<FragmentScreen, FragmentDialog>
