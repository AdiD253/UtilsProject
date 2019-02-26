package pl.adriandefus.utilsproject.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.rxkotlin.subscribeBy
import pl.adriandefus.utilsproject.ResourceProvider
import pl.adriandefus.utilsproject.model.LatLng
import pl.adriandefus.utilsproject.model.PlaceQueryStatus
import pl.adriandefus.utilsproject.model.ResponseStatus
import pl.adriandefus.utilsproject.repository.GooglePlacesRepository
import pl.adriandefus.utilsproject.ui.Status.*
import pl.adriandefus.utilsproject.util.OpenForTesting
import pl.adriandefus.utilsproject.util.handleRxPlaceSearchResponse
import pl.adriandefus.utilsproject.util.post
import javax.inject.Inject

@OpenForTesting
class MainViewModel @Inject constructor(
    resourceProvider: ResourceProvider,
    val placesRepository: GooglePlacesRepository
) : ViewModel() {

    val animationActive = AnimationStatus(ACTIVE, resourceProvider.strings.getAnimActive())
    val animationInactive = AnimationStatus(INACTIVE, resourceProvider.strings.getAnimInactive())

    val placeQuerySuccess = PlaceQueryStatus(ResponseStatus.SUCCESS)
    val placeQueryError = PlaceQueryStatus(ResponseStatus.ERROR)
    val placeQueryProgress = PlaceQueryStatus(ResponseStatus.IN_PROGRESS)

    private val _animationStatus = MutableLiveData<AnimationStatus>()
    val animationStatus: LiveData<AnimationStatus>
        get() = _animationStatus

    private val _placeStatus = MutableLiveData<PlaceQueryStatus>()
    val placeStatus: LiveData<PlaceQueryStatus>
        get() = _placeStatus

    fun toggleAnimation() {
        when (_animationStatus.value?.status) {
            ACTIVE -> _animationStatus post animationInactive
            else -> _animationStatus post animationActive
        }
    }

    fun searchForPlace(place: String, radius: Int, location: LatLng) {
        placesRepository.searchForPlaces(place, radius, location)
            .doOnSubscribe {
                _placeStatus post PlaceQueryStatus(
                    ResponseStatus.IN_PROGRESS
                )
            }
            .handleRxPlaceSearchResponse()
            .subscribeBy(
                onError = {
                    val placeQueryStatus = placeQueryError.apply {
                        error = it
                    }
                    _placeStatus post placeQueryStatus
                },
                onNext = {
                    val placeQueryStatus = placeQuerySuccess.apply {
                        results = it.results
                    }
                    _placeStatus post placeQueryStatus
                }
            )
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