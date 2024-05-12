# Introduction

**Alcubierre: An Android Navigation library.**\
Built to be straightforward and extensible.\
Based on [Redux principles](https://redux.js.org/tutorials/fundamentals/part-2-concepts-data-flow).

<details>
  <summary><b>Features</b></summary>

* State based.
* Not a framework, doesn't force you to any architectural decisions.
* Fully extensible, you decide where to store state, how to reduce it, and how to render it.
* Supports Fragment and Compose navigation with exactly the same API.
* Supports multi-module applications.
* Supports multiple backstack navigation. Fragment render works on native [FragmentManager Api](https://developer.android.com/guide/fragments/fragmentmanager#multiple-back-stacks).
* Supports dialogs. You can you use any type of Compose dialogs, not only Dialog composable like in some other libraries.
* Supports saving and restoring its internal state.
* Deeplinking. And it is fast, thanks to trie matching.
* Conditional navigation. Also, you can create a deeplink for any condition.
* Provides `LifecycleOwner`, `ViewModelStoreOwner` and `SavedStateRegistryOwner` for each compose screen and dialog.
* Supports animated transitions, moreover, the compose screen `Lifecycle` is aware of the transition states. For example, the `ViewModelStore` of a Compose screen will be destroyed only after the exit transition is complete, and `ON_RESUME` will be called only after the enter transition is complete.

  ---

What can be done later:
* Shared element transitions. Some work has already been done, can be implemented with FragmentTransactionModifier for Fragments navigation.
* Predictive back animations.
* Better way to communicate between screens. Fragments have a Fragment Result Api, but this does not apply to Compose screens.
* Kotlin Multiplatform.
  This can be done after stabilizing the library.
  Base components use only `Bundle` from Android runtime, it can be easily replaced with a new type that is platform-independent.
  Compose module can be split into several modules with android lifecycle implementation and some generic ones.
</details>


## Components

The library consists of 4 main components and a unidirectional data flow between them:
 ```
 ┌─────────┐      ┌──────────┐      ┌─────────┐
 │ Actions │─────▶│ Reducers │─────▶│  State  │
 └─────────┘      └──────────┘      └─────────┘
      ▲             ▲      │             │
      │             └──────┘             │
      │                                  │
      │           ┌──────────┐           │
      └───────────│  Render  │◀──────────┘
                  └──────────┘
```

### State
Let begin with the state:
It can be described by the following class:
```Kotlin
class RootNavState(
    val dialogState: Queue<Dialog>,
    val stackStates: Map<Int, Stack<Screen>>,
    val currentStackId: Int
)
```

Screen and Dialog classes are used to describe navigation locations.
They can be a reference to some fragment or a container of a composite function.
But specific implementations do not affect the state structure.

These classes are then organized into stacks.
Alcubierre supports multibackstack navigation, so you can have multiple stacks.

Things are a bit more complicated with dialogs. In Android, dialogs are drawn on top of content in a separate window.
This means that a dialog cannot be part of the content or stack. It was decided to put them in a separate queue.
The queue is used to handle multiple dialogs being displayed at the same time. Since it is recommended to display only one dialog at a time, all subsequent dialogs added will be placed in a queue sorted by priority.

```
Queue<Dialog>:

      ┌─── Currently visible
      ▼
 ┌────────────┐   ┌────────────┐
 │Dialog1     │   │Dialog2     │
 │priority = 9│   │priority = 5│
 └────────────┘   └────────────┘
```
```
Map<Int, Stack<Screen>>:

 ┌───────┐
 │Screen3│       ┌─── Currently visible 
 └───────┘       ▼    assuming that stack 2 is selected
 ┌───────┐   ┌───────┐
 │Screen2│   │Screen5│
 └───────┘   └───────┘ 
 ┌───────┐   ┌───────┐
 │Screen1│   │Screen4│
 └───────┘   └───────┘ 
 _________   _________
  stack 1     stack 2
```

\
All of this may seem too complicated for your usecases.
But you have full control over everything that happens with the state.
So you can disable dialogs or remove all operations related to multibackstack.

### Render
The state is observed by Render.
Render is the entity responsible for translating the state into content visible to the user.
There are two implementations: for fragment navigation and for Compose.
You can learn more about implementation details by checking KDocs. 

### Actions
User can generate actions from the UI.
For example, the user can click the back arrow on the toolbar. Then the current screen should be popped out of the selected stack.
This is done by dispatching Actions to the [library entry point](#entry-point).
An action is a class that describes something that happened in the navigation state.
For example, we can describe simple forward navigation as `class Forward(val target: Screen)` or backward as `object Back`.

### Reducers
The last step is to process the actions and update the state. This process is known as reducing or folding.
A reducer is a function that receives the current state and an action instance, decides how to update the state if necessary, and returns the new state:
`(state, action) => newState`. You can think of a reducer as an event listener which handles events based on the received action.

It is also important that reducers can be chained together. This allows you to create complex logic for processing incoming actions.

Here's a simple example of a stack switching reducer:
```Kotlin
class SelectStackReducer : NavReducer<RootNavState>() {

    override fun reduce(state: RootNavState, action: NavAction): RootNavState = when (action) {
        is SelectStack -> state.copy(currentStackId = action.stackId)
        else -> state
    }
}
```
The `com.github.octaone.alcubierre:alcubierre` module contains a standard set of Actions and Reducers for them.

## Entry point
The entry point is the interface to interact with the navigation library from your code.
It can be described as follows
```Kotlin
interface NavDrive {

    val state: RootNavState

    fun dispatch(action: NavAction)
}
```
In other words, `NavDrive` includes `Reducers` and `State` components from the diagram.

`NavDrive` has a default implementation `AlcubierreNavDriveOwner`, and there are several ways to initialize and use it.
The `AlcubierreNavDriveOwner` can be coupled with `Render` or it can be separated from `Render`.\
As an example, Jetpack navigation uses the coupled approach. Speaking of Fragments navigation, the `NavController` can only be accessed from `NavHostFragment`.
Similarly, you can use `AlcubierreNavDriveFragment` which is responsible for creating the owner, restoring and saving its state and rendering the state.
But you can also create the `AlcubierreNavDriveOwner` manually somewhere in the application (DI container, Application class, etc.).
This way, it will be possible to dispatch navigation actions from anywhere in the application, not just Fragments.
But it is important to manually handle saving and restoring state.
An example can be found in the `sample-fragment` module.

## Type safety notice
All the previous examples miss the fact that library classes are typed with two types `out S : Screen` and `out D : Dialog`.
But with a little bit of unsafe casts, the base actions and reducers have been made implementation independent. So you can use the same reducers for Fragment and Compose screens. 
