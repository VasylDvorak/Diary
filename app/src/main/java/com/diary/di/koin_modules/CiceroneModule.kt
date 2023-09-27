package com.diary.di.koin_modules


import com.diary.navigation.AndroidScreens
import com.diary.navigation.IScreens
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router


class CiceroneModule {

    fun cicerone(): Cicerone<Router> = Cicerone.create()
    fun navigatorHolder(cicerone: Cicerone<Router>): NavigatorHolder = cicerone.getNavigatorHolder()
    fun router(cicerone: Cicerone<Router>): Router = cicerone.router
    fun screens(): IScreens = AndroidScreens()
}