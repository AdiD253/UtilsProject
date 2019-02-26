package pl.adriandefus.utilsproject

import com.nhaarman.mockitokotlin2.any
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import pl.adriandefus.utilsproject.model.LatLng
import pl.adriandefus.utilsproject.model.SearchServiceResponse
import pl.adriandefus.utilsproject.service.GooglePlacesService

@RunWith(JUnit4::class)
class GooglePlacesRepositoryTest {

    @Mock
    private lateinit var service: GooglePlacesService

    private val responseMock = SearchServiceResponse(
        status = "OK",
        results = listOf(),
        errorMessage = null
    )

    private val errorMock = Throwable("error")

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        `when`(
            service.getPlaces(
                keyword = com.nhaarman.mockitokotlin2.eq("value"),
                location = any(),
                radius = any(),
                appKey = any()
            )
        ).thenReturn(Observable.just(responseMock))

        `when`(
            service.getPlaces(
                keyword = com.nhaarman.mockitokotlin2.eq("error"),
                location = any(),
                radius = any(),
                appKey = any()
            )
        ).thenReturn(Observable.error(errorMock))
    }

    @Test
    fun `should return observable of place object`() {
        MatcherAssert.assertThat(service.getPlaces(
            "value", LatLng(0.0, 0.0), 1000, "key"
        ), `is`(instanceOf(Observable::class.java))
        )
    }

    @Test
    fun `should return places on success response`() {
        val subscriber = TestObserver<SearchServiceResponse>()

        service.getPlaces(
            "value", LatLng(0.0, 0.0), 1000, "key"
        ).subscribe(subscriber)

        subscriber.assertComplete()
        subscriber.assertNoErrors()
        subscriber.assertValue(
            responseMock
        )
    }

    @Test
    fun `should throw exception on error`() {
        val subscriber = TestObserver<SearchServiceResponse>()

        service.getPlaces(
            "error", LatLng(0.0, 0.0), 1000, "key"
        ).subscribe(subscriber)

        subscriber.assertError(errorMock)
    }
}