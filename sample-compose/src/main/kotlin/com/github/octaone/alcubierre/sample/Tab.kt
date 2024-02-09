package com.github.octaone.alcubierre.sample

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector

enum class Tab(
    val id: Int,
    val selectedIcon: ImageVector,
    val icon: ImageVector
) {

    TAB_0(0, Icons.Filled.Home, Icons.Outlined.Home),
    TAB_1(1, Icons.Filled.DateRange, Icons.Outlined.DateRange),
    TAB_2(2, Icons.Filled.Face, Icons.Outlined.Face)
}

