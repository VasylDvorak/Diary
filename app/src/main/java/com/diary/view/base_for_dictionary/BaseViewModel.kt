package com.diary.view.base_for_dictionary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow

abstract class BaseViewModel<T : Any>(
    open var _liveDataForViewToObserve: MutableLiveData<T> = MutableLiveData(),
    var savedStateHandle: SavedStateHandle = SavedStateHandle(),
) : ViewModel() {

    protected var queryStateFlow = MutableStateFlow("")
    protected var job: Job = Job()
    var coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onCleared() {
        super.onCleared()
        cancelJob()
    }

    protected fun cancelJob() {
        queryStateFlow = MutableStateFlow("")
    }

    open fun getData(): LiveData<T> = _liveDataForViewToObserve



    abstract fun handleError(error: Throwable)


    protected val viewModelCoroutineScope = CoroutineScope(
        Dispatchers.Main
                + SupervisorJob()
                + CoroutineExceptionHandler { _, throwable ->
            handleError(throwable)
        })
}
