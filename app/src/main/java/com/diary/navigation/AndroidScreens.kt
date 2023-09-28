package com.diary.navigation

import com.diary.view.lessons.LessonsFragment
import com.diary.view.main_fragment.MainFragment
import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.FragmentScreen

class AndroidScreens : IScreens {
    override fun startMainFragment() = FragmentScreen { MainFragment.newInstance() }

    override fun startLessonsFragment(): Screen = FragmentScreen { LessonsFragment.newInstance() }

}



