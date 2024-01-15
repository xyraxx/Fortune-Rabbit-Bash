package dev.fs.mad.game11.presentation

import dev.fs.mad.game11.domain.usecase.BestScoreUseCase
import javax.inject.Inject

class GameViewModel @Inject constructor(
    private val bestScoreUseCase: BestScoreUseCase
) {

    fun setBestScore(value: Int) {
        if (value > bestScoreUseCase.getBestScore())
            bestScoreUseCase.setBestScore(value)

    }

}