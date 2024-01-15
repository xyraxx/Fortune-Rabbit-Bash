package dev.fs.mad.game11

import android.app.Application
import dev.fs.mad.game11.di.AppComponent
import dev.fs.mad.game11.di.DaggerAppComponent
import dev.fs.mad.game11.di.module.DataModule

class App : Application() {
    val appComponent: AppComponent =
        DaggerAppComponent.builder()
            .dataModule(DataModule(this))
            .build()
}