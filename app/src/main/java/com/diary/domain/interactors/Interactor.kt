package com.diary.domain.interactors


import com.diary.model.home_work.HomeWork
import com.diary.model.lessons_home_works.CommonDataModel
import com.diary.model.lessons_home_works.Lesson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface Interactor {
    suspend fun getAllLessonsOrHomeWorksOrExaminationList(): List<Lesson> = listOf()
    suspend fun getHomeWorks(allTasks: List<Lesson>): List<HomeWork> = listOf()
    suspend fun getExaminations(allTasks: List<Lesson>): List<Lesson> = listOf()
    suspend fun getLessons(allTasks: List<Lesson>): List<Lesson> = listOf()
    suspend fun getCommonData(): Flow<CommonDataModel> = flow{}
    suspend fun getLessonsForLessonsFragment(): Flow<List<Lesson>>
}