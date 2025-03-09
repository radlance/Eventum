package com.radlance.eventum

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import com.radlance.eventum.data.auth.AuthRepositoryImpl
import com.radlance.eventum.data.common.DataStoreRepositoryImpl
import com.radlance.eventum.data.onboarding.NavigationRepositoryImpl
import com.radlance.eventum.data.event.RemoteEventRepository
import com.radlance.eventum.navigation.base.NavigationViewModel
import com.radlance.eventum.presentation.authorization.common.AuthResultMapper
import com.radlance.eventum.presentation.authorization.common.AuthViewModel
import com.radlance.eventum.presentation.common.EventViewModel
import com.radlance.eventum.common.ResourceManagerImpl
import com.radlance.eventum.data.database.local.EventumDatabase
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.serializer.KotlinXSerializer
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

const val TEST_ONBOARDING_DATASTORE: String = "test_onboarding_datastore"
private val context: Context = ApplicationProvider.getApplicationContext()

@OptIn(ExperimentalCoroutinesApi::class)
val testDataStore = PreferenceDataStoreFactory.create(
    scope = CoroutineScope(UnconfinedTestDispatcher()),
    produceFile = { context.preferencesDataStoreFile(TEST_ONBOARDING_DATASTORE) }
)

class ViewModelFactory {
    private val supabaseClient = createSupabaseClient(
        supabaseUrl = "https://osoknxtwcppulkimwpjo.supabase.co",
        supabaseKey = BuildConfig.SUPABASE_KEY
    ) {
        install(Auth)
        install(Postgrest)
        install(Storage)
        defaultSerializer = KotlinXSerializer()
    }

    fun createNavigationViewModel(): NavigationViewModel {

        val dataStoreRepositoryImpl = DataStoreRepositoryImpl(testDataStore)
        val navigationRepository = NavigationRepositoryImpl(dataStoreRepositoryImpl)
        val navigationViewModel = NavigationViewModel(navigationRepository)

        return navigationViewModel
    }

    fun createAuthViewModel(): AuthViewModel {
        val authRepository = AuthRepositoryImpl(supabaseClient.auth)
        val resourceManager = ResourceManagerImpl(context)
        val mapper = AuthResultMapper(resourceManager)

        val authViewModel = AuthViewModel(authRepository, mapper)

        return authViewModel
    }

    fun createEventViewModel(): EventViewModel {
        val database = EventumDatabase.getInstance(context)
        return EventViewModel(RemoteEventRepository(supabaseClient, database.dao()))
    }
}