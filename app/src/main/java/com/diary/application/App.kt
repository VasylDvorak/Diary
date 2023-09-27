package com.diary.application

import android.app.Application
import com.diary.di.ConnectKoinModules.appModule
import com.diary.di.ConnectKoinModules.application
import com.diary.di.ConnectKoinModules.ciceroneModule
import com.diary.di.ConnectKoinModules.lessonsFragmentModule
import com.diary.di.ConnectKoinModules.lessonsScreen
import com.diary.di.ConnectKoinModules.mainFragmentModule
import com.diary.di.ConnectKoinModules.mainScreen
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(
                listOf(
                    application,
                    appModule,
                    mainFragmentModule,
                    lessonsFragmentModule,
                    lessonsScreen,
                    mainScreen,
                    ciceroneModule
                )
            )
        }
    }
}

