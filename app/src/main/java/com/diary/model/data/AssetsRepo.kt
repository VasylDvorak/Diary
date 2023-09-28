package com.diary.model.data

import com.diary.model.lessons_home_works.Lesson


interface AssetsRepo {
    suspend fun getAllLessonsOrHomeWorksOrExaminationList(): List<Lesson>
    suspend fun getLessonsOrHomeWorksOrExaminationList(databaseJson: String): List<Lesson>
    suspend fun calculateDuration(databaseRaw: MutableList<Lesson>): MutableList<Lesson>
}