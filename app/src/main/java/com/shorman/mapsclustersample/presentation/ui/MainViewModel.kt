package com.shorman.mapsclustersample.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shorman.mapsclustersample.domain.model.MarkerItemModel
import com.shorman.mapsclustersample.domain.usecase.GetClusterDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getClusterDataUseCase: GetClusterDataUseCase
): ViewModel() {

    private val _clusteringData = MutableStateFlow<List<MarkerItemModel>?>(null)
    val clusteringData = _clusteringData.asStateFlow()

    private val _loading = MutableSharedFlow<Boolean>()
    val loading = _loading.asSharedFlow()

    private val _error = MutableSharedFlow<Throwable>()
    val error = _error.asSharedFlow()

    init {
        getClusterData()
    }

    private fun getClusterData() = viewModelScope.launch {
        _loading.emit(true)
        try {
            _clusteringData.emit(getClusterDataUseCase())
        } catch (e: Exception) {
            _error.emit(e)
        }
        _loading.emit(false)
    }

}