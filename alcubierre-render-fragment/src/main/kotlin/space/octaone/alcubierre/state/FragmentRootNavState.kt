/*
 *  Copyright 2022-2025. Alcubierre Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.octaone.alcubierre.state

import space.octaone.alcubierre.core.state.DialogNavState
import space.octaone.alcubierre.core.state.RootNavState
import space.octaone.alcubierre.core.state.StackNavState
import space.octaone.alcubierre.screen.FragmentDialog
import space.octaone.alcubierre.screen.FragmentScreen

/**
 * [RootNavState] for fragment navigation.
 */
public typealias FragmentRootNavState = RootNavState<FragmentScreen, FragmentDialog>

/**
 * [StackNavState] for fragment navigation.
 */
public typealias FragmentStackNavState = StackNavState<FragmentScreen>

/**
 * [DialogNavState] for fragment navigation.
 */
public typealias FragmentDialogNavState = DialogNavState<FragmentDialog>
