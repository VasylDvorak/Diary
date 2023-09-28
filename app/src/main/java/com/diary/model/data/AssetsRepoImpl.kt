package com.diary.model.data

import android.content.Context
import com.diary.model.lessons_home_works.Lesson
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.mp.KoinPlatform.getKoin
import java.time.Duration
import java.util.Calendar

internal const val ASSETS_LESSONS = "lessons.json"

class AssetsRepoImpl : AssetsRepo {

    override suspend fun getAllLessonsOrHomeWorksOrExaminationList(): List<Lesson> {
        val databaseJson: String =
            getKoin().get<Context>().assets.open(ASSETS_LESSONS).bufferedReader().use { it.readText() }
        val databaseRaw = getLessonsOrHomeWorksOrExaminationList(databaseJson).toMutableList()
        if(databaseRaw.isNullOrEmpty()){
            return listOf()
        }
        val databaseRawWithDuartion = calculateDuration(databaseRaw)
        databaseRawWithDuartion.removeAll { it.duration <= 0L }
        if(databaseRawWithDuartion.isNullOrEmpty()){
            return listOf()
        }
        if(databaseRawWithDuartion.size == 1){
            return databaseRaw
        }
        databaseRawWithDuartion.sortBy { it.duration }
        return databaseRawWithDuartion
    }

    override suspend fun getLessonsOrHomeWorksOrExaminationList(databaseJson: String): List<Lesson> {
        return Gson().fromJson(databaseJson, object : TypeToken<List<Lesson>>() {}.type) ?: listOf()
    }
    override suspend fun calculateDuration(databaseRaw: MutableList<Lesson>): MutableList<Lesson> {
        val calendar = Calendar.getInstance()
        val timeFromItem = Calendar.getInstance()
        databaseRaw.forEach {
            timeFromItem.set(
                it.dataCalendarEndTime.year,
                it.dataCalendarEndTime.month,
                it.dataCalendarEndTime.day,
                it.dataCalendarEndTime.hour,
                it.dataCalendarEndTime.minute
            )
            it.duration =
                Duration.between(calendar.toInstant(), timeFromItem.toInstant()).toMinutes()
        }
        return databaseRaw
    }
}

