package com.radlance.eventum.data.user

import com.radlance.eventum.data.database.remote.RemoteMapper
import com.radlance.eventum.data.database.remote.entity.UserEntity
import com.radlance.eventum.domain.remote.FetchResult
import com.radlance.eventum.domain.user.User
import com.radlance.eventum.domain.user.UserRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.storage.storage
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.put
import java.util.UUID
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : UserRepository, RemoteMapper() {
    override suspend fun getCurrentUserData(): FetchResult<User> {
        val currentUser = supabaseClient.auth.currentSessionOrNull()?.user
        return if (currentUser != null) {
            val json = Json { ignoreUnknownKeys = true }
            val userData = currentUser.userMetadata?.let {
                json.decodeFromString<UserEntity>(
                    it.toString()
                ).toUser()
            }
            FetchResult.Success(userData?.copy(email = currentUser.email ?: "") ?: User())
        } else {
            FetchResult.Unauthorized()
        }
    }

    override suspend fun updateUserData(
        user: User,
        imageByteArray: ByteArray?
    ): FetchResult<Unit> {
        val currentUser = supabaseClient.auth.currentSessionOrNull()?.user
        return try {
            if (currentUser != null) {
                imageByteArray?.let {
                    val fileName =
                        "profile_image_${UUID.randomUUID().toString().replace("-", "")}.jpg"
                    val bucket = supabaseClient.storage["Eventum"]
                    bucket.upload(fileName, imageByteArray, options = { upsert = true })
                    val savedImageUrl = bucket.publicUrl(fileName)
                    supabaseClient.auth.updateUser {
                        data {
                            put("avatar_url", savedImageUrl)
                        }
                    }
                }

                supabaseClient.auth.updateUser {
                    data {
                        put("name", user.firstName)
                        put("last_name", user.lastName)
                        put("address", user.address)
                        put("phone_number", user.phoneNumber)
                    }
                }

                FetchResult.Success(Unit)
            } else {
                FetchResult.Error(Unit)
            }
        } catch (e: Exception) {
            FetchResult.Error(Unit)
        }
    }
}