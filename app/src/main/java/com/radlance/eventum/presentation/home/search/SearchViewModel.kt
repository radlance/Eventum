package com.radlance.eventum.presentation.home.search

import androidx.lifecycle.viewModelScope
import com.radlance.eventum.domain.search.EventSearchRepository
import com.radlance.eventum.domain.search.SearchHistoryQuery
import com.radlance.eventum.presentation.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: EventSearchRepository
) : BaseViewModel() {
    val localSearchHistory = repository.loadSearchHistory().stateInViewModel(emptyList())

    fun addQueryToHistory(query: String) {
        viewModelScope.launch {
            val searchHistoryQuery = SearchHistoryQuery(
                query = query,
                queryTime = LocalDateTime.now().toKotlinLocalDateTime()
            )

            repository.addQueryToHistory(searchHistoryQuery)
        }
    }
}