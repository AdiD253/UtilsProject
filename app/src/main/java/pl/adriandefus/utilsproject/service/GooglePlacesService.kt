package pl.adriandefus.utilsproject.service

import io.reactivex.Observable
import pl.adriandefus.utilsproject.model.LatLng
import pl.adriandefus.utilsproject.model.SearchServiceResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePlacesService {

    @GET("/maps/api/place/nearbysearch/json")
    fun getPlaces(
        @Query("keyword") keyword: String,
        @Query("location") location: LatLng,
        @Query("radius") radius: Int,
        @Query("key") appKey: String
    ): Observable<SearchServiceResponse>
}