package com.diary.di


import android.content.Context
import com.diary.di.koin_modules.AppModule
import com.diary.di.koin_modules.CiceroneModule
import com.diary.di.koin_modules.LessonsFragmentModule
import com.diary.di.koin_modules.MainFragmentModule
import com.diary.di.koin_modules.NAME_CICERONE_MODULE_CICERONE
import com.diary.domain.interactors.MainInteractor
import com.diary.model.data.AssetsRepoImpl
import com.diary.model.repository.Repository
import com.diary.model.repository.RepositoryImplementation
import com.diary.view.lessons.LessonsFragment
import com.diary.view.lessons.LessonsViewModel
import com.diary.view.main_fragment.MainFragment
import com.diary.view.main_fragment.MainViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.mp.KoinPlatform.getKoin


const val mainScreenScopeName = "mainScreenScope"
const val lessonsScreenScopeName = "descriptionScreenScope"

object ConnectKoinModules {

    val application = module {
        single<Repository> { RepositoryImplementation(AssetsRepoImpl()) }
    }

    val mainScreen = module {
        scope(named<MainFragment>()) {
            scoped { MainInteractor(get()) }
            viewModel { MainViewModel(get()) }
        }
    }

    val mainScreenScope by lazy {
        getKoin()
            .getOrCreateScope(mainScreenScopeName, named<MainFragment>())
    }

    val lessonsScreen = module {
        scope(named<LessonsFragment>()) {
            scoped { MainInteractor(get()) }
            viewModel { LessonsViewModel(get()) }
        }
    }

    val lessonsScreenScope by lazy {
        getKoin()
            .getOrCreateScope(lessonsScreenScopeName, named<LessonsFragment>())
    }

    val appModule = module {
        scope(named<Context>()) {
            scoped { AppModule().applicationContext(context = androidApplication()) }
        }
    }

    val ciceroneModule = module {

        single(qualifier = named(NAME_CICERONE_MODULE_CICERONE)) { CiceroneModule().cicerone() }
        single {
            CiceroneModule().navigatorHolder(
                cicerone =
                get(named(NAME_CICERONE_MODULE_CICERONE))
            )
        }
        single { CiceroneModule().router(cicerone = get(named(NAME_CICERONE_MODULE_CICERONE))) }
        single { CiceroneModule().screens() }
    }


    val mainFragmentModule = module {
        single { MainFragmentModule().mainFragment() }

    }

    val lessonsFragmentModule = module {
        single { LessonsFragmentModule().lessonsFragment() }

    }
}