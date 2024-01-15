package dev.fs.mad.game11.domain.repository

interface ScoreRepository {
    fun getScore(): Int

    fun setScore(value: Int)

}