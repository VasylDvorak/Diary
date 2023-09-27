package com.diary.domain.interactors

import com.diary.model.home_work.CalendarTime
import com.diary.model.home_work.HomeWork
import com.diary.model.lessons_home_works.CommonDataModel
import com.diary.model.lessons_home_works.Lesson
import com.diary.model.repository.Repository
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Calendar

class MainInteractor(
    var repositoryLocal: Repository
) : Interactor {

    override suspend fun getAllLessonsOrHomeWorksOrExaminationList(): List<Lesson> {
        val output =repositoryLocal.getAllLessonsOrHomeWorksOrExaminationList()
          println("repositoryLocal.getAllLessonsOrHomeWorksOrExaminationList()="+Gson().toJson(output))
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
                dataCalendarTimePass = CalendarTime(day = checkDays(it))
            )
        }
    }

    override suspend fun getExaminations(allTasks: List<Lesson>): List<Lesson> {
val examinations = allTasks.filter { (it.typeofLesson == "Экзамен") && (checkDateTime(it)) }

        examinations.forEach {
            var daysDifference = checkDays(it)
            var hoursDifference = checkHours(it, daysDifference)
            var minutesDifference = checkMinutes(it, hoursDifference)
            it.elapsedTime=CalendarTime(day=daysDifference,
                hour = hoursDifference,
                minute = minutesDifference)
        }

        return examinations
    }

    override suspend fun getLessons(allTasks: List<Lesson>): List<Lesson> {
        return allTasks.filter { it.typeofLesson != "Домашнее задание" }
    }

    override suspend fun getLessonsForLessonsFragment(): Flow<List<Lesson>>{
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

    suspend fun checkDateTime(item: Lesson): Boolean {
        val calendar = Calendar.getInstance()
        if (item.dataCalendarEndTime.year - calendar.get(Calendar.YEAR) > 0) {
            return true
        } else if (item.dataCalendarEndTime.month - calendar.get(Calendar.MONTH) > 0) {
            return true
        } else if (item.dataCalendarEndTime.hour - calendar.get(Calendar.DAY_OF_MONTH) > 0) {
            return true
        } else if (item.dataCalendarEndTime.hour - calendar.get(Calendar.HOUR_OF_DAY) > 0) {
            return true
        } else return item.dataCalendarEndTime.minute - calendar.get(Calendar.MINUTE) > 0
    }

    suspend fun checkDays(item: Lesson): Int {
        val calendar = Calendar.getInstance()
        val timeFromItem = Calendar.getInstance()
        timeFromItem.set(
            item.dataCalendarEndTime.year,
            item.dataCalendarEndTime.month,
            item.dataCalendarEndTime.day,
            item.dataCalendarEndTime.hour,
            item.dataCalendarEndTime.minute
        )
        if (item.dataCalendarEndTime.year - calendar.get(Calendar.YEAR) > 0) {
            val yearsDays = (item.dataCalendarEndTime.year - calendar.get(Calendar.YEAR)) * 365
            val daysForItems =  timeFromItem.get(Calendar.DAY_OF_YEAR)
            val daysForCurrentDate = 365 - calendar.get(Calendar.DAY_OF_YEAR)
            return yearsDays + daysForItems + daysForCurrentDate
        } else {
            return timeFromItem.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR)
        }
    }

    suspend fun checkHours(item: Lesson, daysDifference: Int): Int{
        val calendar = Calendar.getInstance()
        if(daysDifference>0){
            return item.dataCalendarEndTime.hour
        }else{
            return item.dataCalendarEndTime.hour - calendar.get(Calendar.HOUR_OF_DAY)
        }
    }

    suspend fun checkMinutes(item: Lesson, hoursDifference: Int): Int{
        val calendar = Calendar.getInstance()
        if(hoursDifference>0){
             return item.dataCalendarEndTime.minute
        }else{
            return item.dataCalendarEndTime.minute - calendar.get(Calendar.MINUTE)
        }
    }
}

