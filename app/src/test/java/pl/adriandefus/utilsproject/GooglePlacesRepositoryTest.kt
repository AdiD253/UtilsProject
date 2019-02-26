package pl.adriandefus.utilsproject

import com.nhaarman.mockitokotlin2.any
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.observers.TestObserver
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import pl.adriandefus.utilsproject.model.LatLng
import pl.adriandefus.utilsproject.model.SearchServiceResponse
import pl.adriandefus.utilsproject.repository.GooglePlacesRepository
import pl.adriandefus.utilsproject.repository.GooglePlacesRepositoryImpl
import pl.adriandefus.utilsproject.service.GooglePlacesService
import org.junit.AfterClass

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

    private lateinit var repository: GooglePlacesRepository

    companion object {
        @BeforeClass
        @JvmStatic
        fun before() {
            RxAndroidPlugins.reset()
            RxJavaPlugins.reset()

            RxJavaPlugins.setIoSchedulerHandler {
                Schedulers.trampoline()
            }
            RxAndroidPlugins.setInitMainThreadSchedulerHandler {
                Schedulers.trampoline()
            }
        }

        @AfterClass
        @JvmStatic
        fun after() {
            RxAndroidPlugins.reset()
            RxJavaPlugins.reset()
        }
    }

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

        repository = GooglePlacesRepositoryImpl("foo", service)
    }

    @Test
    fun `should return observable of place object`() {
        MatcherAssert.assertThat(
            repository.searchForPlaces(
                "value", 1000, LatLng(0.0, 0.0)
            ), `is`(instanceOf(Observable::class.java))
        )
    }

    @Test
    fun `should return places on success response`() {
        val testObserver = TestObserver<SearchServiceResponse>()

        repository.searchForPlaces("value", 1000, LatLng(0.0, 0.0))
            .subscribe(testObserver)

        testObserver.assertComplete()
        testObserver.assertNoErrors()
        testObserver.assertValue(
            responseMock
        )
    }

    @Test
    fun `should throw exception on error`() {
        val testObserver = TestObserver<SearchServiceResponse>()

        repository.searchForPlaces(
            "error", 1000, LatLng(0.0, 0.0)
        ).subscribe(testObserver)

        testObserver.assertError(errorMock)
    }
}