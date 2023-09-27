package com.diary.model.repository

import com.diary.model.data.AssetsRepo
import com.diary.model.lessons_home_works.Lesson

class RepositoryImplementation(private val dataSource: AssetsRepo) :
    Repository {
    override suspend fun getAllLessonsOrHomeWorksOrExaminationList(): List<Lesson> {
        return dataSource.getAllLessonsOrHomeWorksOrExaminationList()
    }

}

