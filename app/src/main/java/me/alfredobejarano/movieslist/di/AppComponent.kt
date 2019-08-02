package me.alfredobejarano.movieslist.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import me.alfredobejarano.local.di.LocalModule
import me.alfredobejarano.movieslist.remote.di.RemoteModule
import javax.inject.Singleton

/**
 * Created by alfredo on 2019-08-02.
 */
@Singleton
@Component(
    modules = [
        LocalModule::class,
        RemoteModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        fun build(): AppComponent
        @BindsInstance
        fun application(application: Application): Builder

        fun localModule(localModule: LocalModule): Builder
    }

    fun inject(app: Application)
}