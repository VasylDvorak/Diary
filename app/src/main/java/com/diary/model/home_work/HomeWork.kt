package com.diary.model.home_work

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HomeWork(
    var id: Int = 0,
    var subject: String = "",
    var task:String="",
    var icons:List<String> = listOf(),
    var dataCalendarTime:CalendarTime=CalendarTime(),
    var dataCalendarTimePass:CalendarTime=CalendarTime()
) : Parcelable