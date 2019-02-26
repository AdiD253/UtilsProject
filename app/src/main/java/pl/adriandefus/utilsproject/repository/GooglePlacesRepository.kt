package pl.adriandefus.utilsproject.repository

import io.reactivex.Observable
import pl.adriandefus.utilsproject.model.LatLng
import pl.adriandefus.utilsproject.model.SearchServiceResponse

interface GooglePlacesRepository {

    fun searchForPlaces(place: String, radius: Int, location: LatLng): Observable<SearchServiceResponse>
}