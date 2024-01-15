package dev.fs.mad.game11.presentation

import dev.fs.mad.game11.domain.usecase.BestScoreUseCase
import javax.inject.Inject

class StartViewModel @Inject constructor(
    private val bestScoreUseCase: BestScoreUseCase
) {

    fun getBestScore(): Int =
        bestScoreUseCase.getBestScore()

}