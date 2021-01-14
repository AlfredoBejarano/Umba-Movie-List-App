package me.alfredobejarano.movieslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel

/**
 * ViewModel class that helps fragments communicate between one each other without interfaces.
 */
class NavHostViewModel : ViewModel() {
    var searchViewIsOpen = false

    private val _searchQueryLiveData = MediatorLiveData<String>()
    val searchQueryLiveData = _searchQueryLiveData as LiveData<String>

    private val _closeSearchViewLiveData = MediatorLiveData<Unit>()
    val closeSearchViewLiveData = _closeSearchViewLiveData as LiveData<Unit>

    fun reportQueryChange(query: String) {
        _searchQueryLiveData.value = query
    }

    fun closeSearchView() {
        _closeSearchViewLiveData.value = Unit
    }
}