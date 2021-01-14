package me.alfredobejarano.movieslist

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import me.alfredobejarano.movieslist.di.Injector
import javax.inject.Inject

class MovieApplication : Application(), HasAndroidInjector {
    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
        Injector.init(this)
        Injector.component.inject(this)
    }
}