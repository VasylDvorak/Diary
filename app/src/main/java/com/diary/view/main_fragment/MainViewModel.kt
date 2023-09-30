package com.diary.view.main_fragment

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import com.diary.domain.interactors.MainInteractor
import com.diary.model.lessons_home_works.CommonDataModel
import com.diary.view.base_for_dictionary.BaseViewModel
import com.diary.view.base_for_dictionary.countDownInterval
import com.diary.view.base_for_dictionary.millisInFuture
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

private const val QUERY = "query"

class MainViewModel(var interactor: MainInteractor) : BaseViewModel<CommonDataModel>() {

    private val liveDataForViewToObserve: LiveData<CommonDataModel> = _liveDataForViewToObserve

    fun subscribe(): LiveData<CommonDataModel> {
        return liveDataForViewToObserve
    }

    fun getRestoredData(): CommonDataModel? = savedStateHandle[QUERY]
    fun setQuery(query: CommonDataModel) {
        savedStateHandle[QUERY] = query
    }

    // Обрабатываем ошибки
    override fun handleError(error: Throwable) {
        _liveDataForViewToObserve.postValue(CommonDataModel())
    }


    public override fun onCleared() {
        _liveDataForViewToObserve.value = CommonDataModel()
        super.onCleared()
    }

    override fun getData(): LiveData<CommonDataModel> {
        timer = object : CountDownTimer(
                millisInFuture, countDownInterval
            ) {
                override fun onTick(millisUntilFinished: Long) {
                    coroutineScope.launch {
                        dataFromNetwork().catch {
                            emit(CommonDataModel())
                        }
                            .filterNotNull().collect { result ->
                                _liveDataForViewToObserve.postValue(result)
                            }
                    }
                }
                override fun onFinish() {
                    timer.start()
                }
            }
            timer.start()
        return super.getData()
    }

    fun dataFromNetwork(): Flow<CommonDataModel> {
        return flow {
            emit(interactor.getCommonData().first())
        }
    }
}