package pl.adriandefus.utilsproject.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.support.annotation.VisibleForTesting
import pl.adriandefus.utilsproject.ResourceProvider
import pl.adriandefus.utilsproject.ui.Status.*
import pl.adriandefus.utilsproject.util.OpenForTesting
import pl.adriandefus.utilsproject.util.post
import javax.inject.Inject

@OpenForTesting
class MainViewModel @Inject constructor(
    resourceProvider: ResourceProvider
) : ViewModel() {

    val animationActive = AnimationStatus(ACTIVE, resourceProvider.strings.getAnimActive())
    val animationInactive = AnimationStatus(INACTIVE, resourceProvider.strings.getAnimInactive())

    private val _animationStatus = MutableLiveData<AnimationStatus>()
    val animationStatus: LiveData<AnimationStatus>
        get() = _animationStatus

    fun toggleAnimation() {
        when (_animationStatus.value?.status) {
            ACTIVE -> _animationStatus post animationInactive
            else -> _animationStatus post animationActive
        }
    }

    override fun onCleared() {
        _animationStatus.postValue(null)
        super.onCleared()
    }
}

class AnimationStatus(
    val status: Status,
    val statusInfo: String
)

enum class Status {
    ACTIVE, INACTIVE
}