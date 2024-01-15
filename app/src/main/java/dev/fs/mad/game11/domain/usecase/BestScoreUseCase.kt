package dev.fs.mad.game11.domain.usecase

import dev.fs.mad.game11.domain.repository.ScoreRepository
import javax.inject.Inject

class BestScoreUseCase @Inject constructor(
    private val scoreRepository: ScoreRepository
) {
    fun getBestScore(): Int =
        scoreRepository.getScore()

    fun setBestScore(value: Int) {
        scoreRepository.setScore(value)
    }

}