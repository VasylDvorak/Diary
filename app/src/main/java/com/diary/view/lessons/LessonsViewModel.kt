package com.diary.view.lessons

import android.app.SearchManager.QUERY
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import com.diary.domain.interactors.MainInteractor
import com.diary.model.lessons_home_works.CommonDataModel
import com.diary.model.lessons_home_works.Lesson
import com.diary.view.base_for_dictionary.BaseViewModel
import com.diary.view.base_for_dictionary.countDownInterval
import com.diary.view.base_for_dictionary.millisInFuture
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

private const val QUERY_LESSONS = "query_lessons"

class LessonsViewModel(var interactor: MainInteractor) : BaseViewModel<List<Lesson>>() {

    private val liveDataForViewToObserve: LiveData<List<Lesson>> = _liveDataForViewToObserve

    fun subscribe(): LiveData<List<Lesson>> {
        return liveDataForViewToObserve
    }

    fun getRestoredData(): List<Lesson>? = savedStateHandle[QUERY]
    fun setQuery(query: List<Lesson>) {
        savedStateHandle[QUERY] = query
    }

    // Обрабатываем ошибки
    override fun handleError(error: Throwable) {
        _liveDataForViewToObserve.postValue(listOf())
    }


    public override fun onCleared() {
        _liveDataForViewToObserve.value = listOf()
        super.onCleared()
    }


    override fun getData(): LiveData<List<Lesson>> {
        timer = object : CountDownTimer(
            millisInFuture, countDownInterval
        ) {
            override fun onTick(millisUntilFinished: Long) {
                coroutineScope.launch {
                    dataFromNetwork().catch {
                        emit(listOf())
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

    fun dataFromNetwork(): Flow<List<Lesson>> {
        return flow {
            emit(interactor.getLessonsForLessonsFragment().first())
        }
    }
}