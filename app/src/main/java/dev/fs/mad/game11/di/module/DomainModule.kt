package dev.fs.mad.game11.di.module

import dev.fs.mad.game11.data.repository.ScoreRepositoryImpl
import dev.fs.mad.game11.domain.repository.ScoreRepository
import dev.fs.mad.game11.domain.usecase.BestScoreUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class DomainModule {

    @Singleton
    @Binds
    abstract fun bindScoreRepository(impl: ScoreRepositoryImpl): ScoreRepository

    companion object {
        @Singleton
        @Provides
        fun provideBestScoreUseCase(
            scoreRepository: ScoreRepository
        ): BestScoreUseCase =
            BestScoreUseCase(scoreRepository)
    }
}