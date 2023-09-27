package com.diary.model.home_work

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class CalendarTime(
    var year: Int = 0,
    var month: Int = 0,
    var day: Int = 0,
    var hour: Int = 0,
    var minute: Int = 0
) : Parcelable

