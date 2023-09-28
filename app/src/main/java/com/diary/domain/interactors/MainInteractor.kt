package com.diary.domain.interactors

import com.diary.model.home_work.CalendarTime
import com.diary.model.home_work.HomeWork
import com.diary.model.lessons_home_works.CommonDataModel
import com.diary.model.lessons_home_works.Lesson
import com.diary.model.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.Duration
import java.util.Calendar

class MainInteractor(
    var repositoryLocal: Repository
) : Interactor {

    override suspend fun getAllLessonsOrHomeWorksOrExaminationList(): List<Lesson> {
        return repositoryLocal.getAllLessonsOrHomeWorksOrExaminationList()
    }


    override suspend fun getHomeWorks(allTasks: List<Lesson>): List<HomeWork> {
        var homeWorks = allTasks.filter { it.typeofLesson == "Домашнее задание" }
        return homeWorks.map {
            HomeWork(
                id = it.id,
                subject = it.subject,
                task = it.description,
                icons = it.icons,
                dataCalendarTime = it.dataCalendarStartTime,
                dataCalendarTimePass = CalendarTime(day = checkTimeDifferenceForHomeWork(it))
            )
        }
    }


    override suspend fun getExaminations(allTasks: List<Lesson>): List<Lesson> {
        val examinations = allTasks.filter { it.typeofLesson == "Экзамен" }

        examinations.forEach {
            var timeDifference = checkTimeDifferenceForExamination(it)

            it.elapsedTime = CalendarTime(
                day = timeDifference[0],
                hour = timeDifference[1],
                minute = timeDifference[2]
            )
            it.duration=timeDifference[3].toLong()
        }

        return examinations
    }

    override suspend fun getLessons(allTasks: List<Lesson>): List<Lesson> {
        return allTasks.filter { it.typeofLesson != "Домашнее задание" }
    }

    override suspend fun getLessonsForLessonsFragment(): Flow<List<Lesson>> {
        return MutableStateFlow(getLessons(repositoryLocal.getAllLessonsOrHomeWorksOrExaminationList()))
    }


    override suspend fun getCommonData(): Flow<CommonDataModel> {
        var allTasks = getAllLessonsOrHomeWorksOrExaminationList()
        var homeWorks = getHomeWorks(allTasks)
        var lessons = getLessons(allTasks)
        var examinatuions = getExaminations(lessons)

        return MutableStateFlow(
            CommonDataModel(lessons, homeWorks, examinatuions)
        )
    }

  override  suspend fun checkTimeDifferenceForExamination(item: Lesson): List<Int> {
        val calendar = Calendar.getInstance()
        val timeFromItem = Calendar.getInstance()
        timeFromItem.set(
            item.dataCalendarStartTime.year,
            item.dataCalendarStartTime.month,
            item.dataCalendarStartTime.day,
            item.dataCalendarStartTime.hour,
            item.dataCalendarStartTime.minute
        )

        val days = Duration.between(calendar.toInstant(), timeFromItem.toInstant()).toDays()
        val hours =
            Duration.between(calendar.toInstant(), timeFromItem.toInstant()).toHours() - days * 24
        val minute = Duration.between(calendar.toInstant(), timeFromItem.toInstant())
            .toMinutes() - Duration.between(calendar.toInstant(), timeFromItem.toInstant())
            .toHours() * 60
        val minuteDuration = Duration.between(calendar.toInstant(), timeFromItem.toInstant())
            .toMinutes().toInt()
        return listOf(days.toInt(), hours.toInt(), minute.toInt(), minuteDuration)
    }

   override suspend fun checkTimeDifferenceForHomeWork(item: Lesson): Int {
        val calendar = Calendar.getInstance()
        val timeFromItem = Calendar.getInstance()
        timeFromItem.set(
            item.dataCalendarEndTime.year,
            item.dataCalendarEndTime.month,
            item.dataCalendarEndTime.day,
            item.dataCalendarEndTime.hour,
            item.dataCalendarEndTime.minute
        )
        return Duration.between(calendar.toInstant(), timeFromItem.toInstant()).toDays().toInt()
    }
}
