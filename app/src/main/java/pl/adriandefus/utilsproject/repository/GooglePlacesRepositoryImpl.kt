package pl.adriandefus.utilsproject.repository

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pl.adriandefus.utilsproject.model.LatLng
import pl.adriandefus.utilsproject.model.SearchServiceResponse
import pl.adriandefus.utilsproject.service.GooglePlacesService
import pl.adriandefus.utilsproject.util.OpenForTesting
import javax.inject.Inject
import javax.inject.Named

@OpenForTesting
class GooglePlacesRepositoryImpl @Inject constructor(
    @Named("placesApiKey") val placesApiKey: String,
    val service: GooglePlacesService
) : GooglePlacesRepository {

    override fun searchForPlaces(place: String, radius: Int, location: LatLng): Observable<SearchServiceResponse> {
        return service.getPlaces(place, location, radius, placesApiKey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}