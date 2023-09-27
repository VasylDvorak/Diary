package com.diary.model.data

import android.content.Context
import com.diary.model.lessons_home_works.Lesson
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.mp.KoinPlatform.getKoin
import java.util.Calendar

internal const val ASSETS_LESSONS = "lessons.json"

class AssetsRepoImpl : AssetsRepo {

    private val context = getKoin().get<Context>()

    override suspend fun getAllLessonsOrHomeWorksOrExaminationList(): List<Lesson> {
        val databaseJson: String =
            context.assets.open(ASSETS_LESSONS).bufferedReader().use { it.readText() }
        return insertionSort(getLessonsOrHomeWorksOrExaminationList(databaseJson).toMutableList())
    }

    override suspend fun getLessonsOrHomeWorksOrExaminationList(databaseJson: String): List<Lesson> {
        return Gson().fromJson(databaseJson, object : TypeToken<List<Lesson>>() {}.type) ?: listOf()
    }


    override suspend fun insertionSort(items: MutableList<Lesson>): List<Lesson> {

        if (items.isEmpty() || items.size < 2) {
            if (checkDateTime(items[0])) {
                return items
            } else {
                return listOf()
            }
        }

        for (count in 1 until items.count()) {
            val item = items[count]
            var i = count
            if (!checkDateTime(item)) {
                items.removeAt(i)
                continue
            }
            if (items.isEmpty() || items.size < 2) {
                if (checkDateTime(items[0])) {
                    return items
                } else {
                    return listOf()
                }
            }

            while (i > 0 && item.dataCalendarEndTime.year < items[i - 1].dataCalendarEndTime.year) {
                items[i] = items[i - 1]
                i -= 1
            }
            items[i] = item
        }
        for (count in 1 until items.count()) {
            val item = items[count]
            var i = count

            if (!checkDateTime(item)) {
                items.removeAt(i)
                continue
            }
            if (items.isEmpty() || items.size < 2) {
                if (checkDateTime(items[0])) {
                    return items
                } else {
                    return listOf()
                }
            }

            while (i > 0 && item.dataCalendarEndTime.month < items[i - 1].dataCalendarEndTime.month) {
                items[i] = items[i - 1]
                i -= 1
            }
            items[i] = item
        }

        for (count in 1 until items.count()) {
            val item = items[count]
            var i = count

            if (!checkDateTime(item)) {
                items.removeAt(i)
                continue
            }
            if (items.isEmpty() || items.size < 2) {
                if (checkDateTime(items[0])) {
                    return items
                } else {
                    return listOf()
                }
            }

            while (i > 0 && item.dataCalendarEndTime.day < items[i - 1].dataCalendarEndTime.day) {
                items[i] = items[i - 1]
                i -= 1
            }
            items[i] = item
        }

        for (count in 1 until items.count()) {
            val item = items[count]
            var i = count

            if (!checkDateTime(item)) {
                items.removeAt(i)
                continue
            }
            if (items.isEmpty() || items.size < 2) {
                if (checkDateTime(items[0])) {
                    return items
                } else {
                    return listOf()
                }
            }

            while (i > 0 && item.dataCalendarEndTime.hour < items[i - 1].dataCalendarEndTime.hour) {
                items[i] = items[i - 1]
                i -= 1
            }
            items[i] = item
        }

        for (count in 1 until items.count()) {
            val item = items[count]
            var i = count

            if (!checkDateTime(item)) {
                items.removeAt(i)
                continue
            }
            if (items.isEmpty() || items.size < 2) {
                if (checkDateTime(items[0])) {
                    return items
                } else {
                    return listOf()
                }
            }

            while (i > 0 && item.dataCalendarEndTime.minute < items[i - 1].dataCalendarEndTime.minute) {
                items[i] = items[i - 1]
                i -= 1
            }
            items[i] = item
        }
        return items
    }

    override suspend fun checkDateTime(item: Lesson): Boolean {
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
}

