package com.moonfly.gamizer.repository

import com.moonfly.gamizer.GamizerDB

class UserPreferencesDataSourceImpl(
    private val gamizerDb: GamizerDB
): UserPreferencesDataSource {

    override suspend fun isGameLiked(id: Int): Boolean {
        return gamizerDb.gamizerDBQueries.select(id.toLong()).executeAsOneOrNull() != null
    }

    override suspend fun changeGameLike(id: Int, liked: Boolean): Boolean {
        if (liked) {
            gamizerDb.gamizerDBQueries.add(id.toLong())
        } else {
            gamizerDb.gamizerDBQueries.delete(id.toLong())
        }
        return true
    }

}