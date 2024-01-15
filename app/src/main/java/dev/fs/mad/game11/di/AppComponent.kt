package dev.fs.mad.game11.di

import dev.fs.mad.game11.di.module.DataModule
import dev.fs.mad.game11.di.module.DomainModule
import dev.fs.mad.game11.ui.GameFragment
import dev.fs.mad.game11.ui.ResultFragment
import dev.fs.mad.game11.ui.StartFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class, DomainModule::class])
interface AppComponent {
    fun inject(gameFragment: GameFragment)
    fun inject(startFragment: StartFragment)
    fun inject(resultFragment: ResultFragment)

}