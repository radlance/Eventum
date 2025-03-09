package com.radlance.eventum.domain.user

import com.radlance.eventum.domain.remote.FetchResult

interface UserRepository {
    suspend fun getCurrentUserData(): FetchResult<User>
    suspend fun updateUserData(user: User, imageByteArray: ByteArray?): FetchResult<Unit>
}