package pl.adriandefus.utilsproject.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import pl.adriandefus.utilsproject.ResourceProvider
import pl.adriandefus.utilsproject.ui.Status.*
import pl.adriandefus.utilsproject.util.post
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private val _animationStatus = MutableLiveData<AnimationStatus>()
    val animationStatus: LiveData<AnimationStatus>
        get() = _animationStatus

    fun toggleAnimation() {
        when (_animationStatus.value?.status) {
            ACTIVE -> _animationStatus post
                    AnimationStatus(INACTIVE, resourceProvider.strings.getAnimInactive())
            else -> _animationStatus post
                    AnimationStatus(ACTIVE, resourceProvider.strings.getAnimActive())
        }
    }

    override fun onCleared() {
        Log.d("UTILS", resourceProvider.strings.getOnCleared())
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