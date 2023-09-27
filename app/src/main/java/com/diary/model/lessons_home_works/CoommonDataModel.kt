package com.diary.model.lessons_home_works

import android.os.Parcelable
import com.diary.model.home_work.HomeWork
import kotlinx.android.parcel.Parcelize

@Parcelize
class CommonDataModel(
    var lessons: List<Lesson> = listOf(),
    var homeWorkList: List<HomeWork> = listOf(),
    var examinations: List<Lesson> = listOf(),
) : Parcelable
