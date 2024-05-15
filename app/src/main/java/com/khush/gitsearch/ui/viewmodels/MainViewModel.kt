package com.khush.gitsearch.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.khush.gitsearch.common.dispatcher.DispatcherProvider
import com.khush.gitsearch.common.networkhelper.NetworkHelper
import com.khush.gitsearch.data.repository.MainRepository
import com.khush.gitsearch.data.model.MainData
import com.khush.gitsearch.ui.paging.MainPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val networkHelper: NetworkHelper
): ViewModel() {

    private val _mainItem = MutableStateFlow<PagingData<MainData>>(PagingData.empty())
    val mainItem: StateFlow<PagingData<MainData>> = _mainItem
    private val query = MutableStateFlow("")

    init {
        viewModelScope.launch {
            query
                .debounce(500)
                .filter {
                    return@filter it.isNotEmpty()
                }
                .distinctUntilChanged()
                .flatMapLatest { searchQuery ->
                    val pager: Pager<Int, MainData> = Pager(
                        config = PagingConfig(
                            pageSize = 10
                        )
                    ) {
                        MainPagingSource(
                            searchQuery,
                            mainRepository,
                            networkHelper,
                            dispatcherProvider
                        )
                    }
                    pager.flow
                }
                .cachedIn(viewModelScope).collect {
                    _mainItem.emit(it)
                }
        }
    }

    fun searchRepo(searchQuery: String) {
        viewModelScope.launch {
            query.value = searchQuery
        }
    }

}




