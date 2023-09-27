package com.diary.navigation

import com.github.terrakok.cicerone.Screen

interface IScreens {

    fun startMainFragment(): Screen
    fun startLessonsFragment(): Screen
}
