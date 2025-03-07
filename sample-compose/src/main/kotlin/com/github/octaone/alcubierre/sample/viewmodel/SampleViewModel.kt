package com.github.octaone.alcubierre.sample.viewmodel

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext

@HiltViewModel(assistedFactory = SampleViewModel.Factory::class)
class SampleViewModel @AssistedInject constructor(
    @ApplicationContext private val context: Context,
    private val savedStateHandle: SavedStateHandle,
    @Assisted val someId: Int
) : ViewModel() {

    init {
        // savedStateHandle contains values passed from `getSavedStateDefaultArguments`
        check(someId == savedStateHandle.get<Int>("SOME_ID"))
    }

    @AssistedFactory
    interface Factory {
        fun create(someId: Int): SampleViewModel
    }
}
