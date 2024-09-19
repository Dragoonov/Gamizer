package com.moonfly.gamizer.repository

import com.moonfly.gamizer.GamizerDB

class UserPreferencesDataSourceImpl(
    private val gamizerDb: GamizerDB
): UserPreferencesDataSource {

    override suspend fun isGameLiked(id: Int): Response<Boolean> {
        return try {
            Response.Success(gamizerDb.gamizerDBQueries.select(id.toLong()).executeAsOneOrNull() != null)
        } catch (e: IllegalStateException) {
            Response.Error.SerializationError
        }
    }

    override suspend fun changeGameLike(id: Int, liked: Boolean): Response<Unit> {
        if (liked) {
            gamizerDb.gamizerDBQueries.add(id.toLong())
        } else {
            gamizerDb.gamizerDBQueries.delete(id.toLong())
        }
        return Response.Success(Unit)
    }

}