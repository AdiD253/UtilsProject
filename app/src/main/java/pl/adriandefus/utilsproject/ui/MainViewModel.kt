package pl.adriandefus.utilsproject.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import pl.adriandefus.utilsproject.ResourceProvider
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private val _animationStatus = MutableLiveData<AnimationStatus>()
    val animationStatus: LiveData<AnimationStatus>
        get() = _animationStatus

    private val _statusInfo = MutableLiveData<String>()
    val statusInfo: LiveData<String>
        get() = _statusInfo

    fun toggleAnimation() {
        when (animationStatus.value) {
            AnimationStatus.ACTIVE -> {
                _animationStatus.value = AnimationStatus.INACTIVE
                _statusInfo.value = resourceProvider.strings.getAnimInactive()
            }
            else -> {
                _animationStatus.value = AnimationStatus.ACTIVE
                _statusInfo.value = resourceProvider.strings.getAnimActive()
            }
        }
    }
}

enum class AnimationStatus {
    ACTIVE, INACTIVE
}