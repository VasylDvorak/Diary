package com.diary.model.lessons_home_works

import android.os.Parcelable
import com.diary.model.home_work.CalendarTime
import kotlinx.android.parcel.Parcelize

@Parcelize
class Lesson(
    var id: Int = 0,
    var subject: String = "",
    var typeofLesson: String ="",
    var description:String="",
    var icons:List<String> = listOf(),
    var dataCalendarStartTime: CalendarTime= CalendarTime(),
    var dataCalendarEndTime: CalendarTime= CalendarTime(),
    var elapsedTime:CalendarTime = CalendarTime()
) : Parcelable
