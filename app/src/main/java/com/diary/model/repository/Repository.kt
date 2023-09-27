package com.diary.model.repository

import com.diary.model.lessons_home_works.Lesson


interface Repository{
    suspend fun getAllLessonsOrHomeWorksOrExaminationList(): List<Lesson>
}
