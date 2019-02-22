package pl.adriandefus.utilsproject.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import pl.adriandefus.utilsproject.ResourceProvider
import pl.adriandefus.utilsproject.ui.Status.*
import pl.adriandefus.utilsproject.util.OpenForTesting
import pl.adriandefus.utilsproject.util.post
import javax.inject.Inject

@OpenForTesting
class MainViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    val ANIMATION_ACTIVE = AnimationStatus(ACTIVE, resourceProvider.strings.getAnimActive())
    val ANIMATION_INACTIVE = AnimationStatus(INACTIVE, resourceProvider.strings.getAnimInactive())

    private val _animationStatus = MutableLiveData<AnimationStatus>()
    val animationStatus: LiveData<AnimationStatus>
        get() = _animationStatus

    fun toggleAnimation() {
        when (_animationStatus.value?.status) {
            ACTIVE -> _animationStatus post ANIMATION_INACTIVE
            else -> _animationStatus post ANIMATION_ACTIVE
        }
    }

    override fun onCleared() {
        onClearedFunc()
        super.onCleared()
    }

    fun onClearedFunc() {

    }
}

class AnimationStatus(
    val status: Status,
    val statusInfo: String
)

enum class Status {
    ACTIVE, INACTIVE
}