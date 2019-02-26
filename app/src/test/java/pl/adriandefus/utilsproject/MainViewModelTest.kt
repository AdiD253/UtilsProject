package pl.adriandefus.utilsproject

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import android.content.Context
import io.reactivex.Observable
import net.bytebuddy.matcher.StringMatcher
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.verification.VerificationMode
import pl.adriandefus.utilsproject.model.LatLng
import pl.adriandefus.utilsproject.model.PlaceQueryStatus
import pl.adriandefus.utilsproject.model.ResponseStatus
import pl.adriandefus.utilsproject.model.SearchServiceResponse
import pl.adriandefus.utilsproject.repository.GooglePlacesRepository
import pl.adriandefus.utilsproject.ui.AnimationStatus
import pl.adriandefus.utilsproject.ui.MainViewModel
import pl.adriandefus.utilsproject.util.*

@RunWith(JUnit4::class)
class MainViewModelTest {

    companion object {
        private const val ANIM_ACTIVE = "Animation active"
        private const val ANIM_INACTIVE = "Animation inactive"

        private const val ZERO_RESULTS = "ZERO_RESULTS"
        private const val OVER_QUERY_LIMIT = "OVER_QUERY_LIMIT"
        private const val REQUEST_DENIED = "REQUEST_DENIED"
        private const val INVALID_REQUEST = "INVALID_REQUEST"
        private const val UNKNOWN_ERROR = "UNKNOWN_ERROR"
    }

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var googlePlacesRepository: GooglePlacesRepository

    private lateinit var resourceProvider: ResourceProvider
    private lateinit var mainViewModel: MainViewModel

    @Captor
    private lateinit var placeQueryArgumentCaptor: ArgumentCaptor<PlaceQueryStatus>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        resourceProvider = ResourceProvider(context)
        `when`(resourceProvider.strings.getAnimActive()).thenReturn(ANIM_ACTIVE)
        `when`(resourceProvider.strings.getAnimInactive()).thenReturn(ANIM_INACTIVE)

        mainViewModel = MainViewModel(resourceProvider, googlePlacesRepository)
    }

    @Test
    fun should_resourceprovider_return_correct_values() {
        assertThat(resourceProvider.strings.getAnimActive(), `is`(ANIM_ACTIVE))
        assertThat(resourceProvider.strings.getAnimInactive(), `is`(ANIM_INACTIVE))
    }

    @Test
    fun `should toggle animation`() {
        val mainViewModel = MainViewModel(resourceProvider, googlePlacesRepository)
        val observer = mock<Observer<AnimationStatus>>()

        mainViewModel.animationStatus.observeForever(observer)

        mainViewModel.toggleAnimation()
        verify(observer).onChanged(mainViewModel.animationActive)

        mainViewModel.toggleAnimation()
        verify(observer).onChanged(mainViewModel.animationInactive)

        mainViewModel.animationStatus.removeObserver(observer)
    }

    @Test
    fun `should get places information`() {
        `when`(googlePlacesRepository.searchForPlaces("foo", 1000, LatLng(0.0, 0.0)))
            .thenReturn(
                Observable.just(SearchServiceResponse(results = null, status = "OK", errorMessage = null))
            )
        val observer = mock<Observer<PlaceQueryStatus>>()

        mainViewModel.placeStatus.observeForever(observer)

        mainViewModel.searchForPlace("foo", 1000, LatLng(0.0, 0.0))

        verify(observer).onChanged(mainViewModel.placeQueryProgress)

        verify(observer).onChanged(mainViewModel.placeQuerySuccess)
        assertThat(mainViewModel.placeStatus.value?.responseStatus, `is`(ResponseStatus.SUCCESS))

        mainViewModel.placeStatus.removeObserver(observer)
    }

    @Test
    fun `should get zero results info`() {
        `when`(googlePlacesRepository.searchForPlaces("foo", 1000, LatLng(0.0, 0.0)))
            .thenReturn(
                Observable.just(
                    SearchServiceResponse(
                        results = null,
                        status = ZERO_RESULTS,
                        errorMessage = null
                    )
                )
            )
        val observer = mock<Observer<PlaceQueryStatus>>()

        mainViewModel.placeStatus.observeForever(observer)

        mainViewModel.searchForPlace("foo", 1000, LatLng(0.0, 0.0))

        verify(observer).onChanged(mainViewModel.placeQueryProgress)

        verify(observer).onChanged(mainViewModel.placeQueryError)
        assertThat(mainViewModel.placeStatus.value?.responseStatus, `is`(ResponseStatus.ERROR))
        assertThat(mainViewModel.placeStatus.value?.error, `is`(instanceOf(PlaceSearchZeroResultError::class.java)))

        mainViewModel.placeStatus.removeObserver(observer)
    }

    @Test
    fun `should get over query limit info`() {
        `when`(googlePlacesRepository.searchForPlaces("foo", 1000, LatLng(0.0, 0.0)))
            .thenReturn(
                Observable.just(
                    SearchServiceResponse(
                        results = null,
                        status = OVER_QUERY_LIMIT,
                        errorMessage = null
                    )
                )
            )
        val observer = mock<Observer<PlaceQueryStatus>>()

        mainViewModel.placeStatus.observeForever(observer)

        mainViewModel.searchForPlace("foo", 1000, LatLng(0.0, 0.0))

        verify(observer, atLeast(1)).onChanged(placeQueryArgumentCaptor.capture())
        assertThat(placeQueryArgumentCaptor.value.responseStatus, `is`(ResponseStatus.ERROR))
        assertThat(placeQueryArgumentCaptor.value.error, `is`(instanceOf(PlaceSearchOverQueryError::class.java)))

        mainViewModel.placeStatus.removeObserver(observer)
    }

    @Test
    fun `should get request denied info`() {
        `when`(googlePlacesRepository.searchForPlaces("foo", 1000, LatLng(0.0, 0.0)))
            .thenReturn(
                Observable.just(
                    SearchServiceResponse(
                        results = null,
                        status = REQUEST_DENIED,
                        errorMessage = null
                    )
                )
            )
        val observer = mock<Observer<PlaceQueryStatus>>()

        mainViewModel.placeStatus.observeForever(observer)

        mainViewModel.searchForPlace("foo", 1000, LatLng(0.0, 0.0))

        verify(observer).onChanged(mainViewModel.placeQueryProgress)

        verify(observer).onChanged(mainViewModel.placeQueryError)
        assertThat(mainViewModel.placeStatus.value?.responseStatus, `is`(ResponseStatus.ERROR))
        assertThat(mainViewModel.placeStatus.value?.error, `is`(instanceOf(PlaceSearchRequestDeniedError::class.java)))

        mainViewModel.placeStatus.removeObserver(observer)
    }

    @Test
    fun `should get invalid request info`() {
        `when`(googlePlacesRepository.searchForPlaces("foo", 1000, LatLng(0.0, 0.0)))
            .thenReturn(
                Observable.just(
                    SearchServiceResponse(
                        results = null,
                        status = INVALID_REQUEST,
                        errorMessage = null
                    )
                )
            )
        val observer = mock<Observer<PlaceQueryStatus>>()

        mainViewModel.placeStatus.observeForever(observer)

        mainViewModel.searchForPlace("foo", 1000, LatLng(0.0, 0.0))

        verify(observer).onChanged(mainViewModel.placeQueryProgress)

        verify(observer).onChanged(mainViewModel.placeQueryError)
        assertThat(mainViewModel.placeStatus.value?.responseStatus, `is`(ResponseStatus.ERROR))
        assertThat(mainViewModel.placeStatus.value?.error, `is`(instanceOf(PlaceSearchInvalidRequestError::class.java)))

        mainViewModel.placeStatus.removeObserver(observer)
    }

    @Test
    fun `should get unknown error info`() {
        `when`(googlePlacesRepository.searchForPlaces("foo", 1000, LatLng(0.0, 0.0)))
            .thenReturn(
                Observable.just(
                    SearchServiceResponse(
                        results = null,
                        status = UNKNOWN_ERROR,
                        errorMessage = null
                    )
                )
            )
        val observer = mock<Observer<PlaceQueryStatus>>()

        mainViewModel.placeStatus.observeForever(observer)

        mainViewModel.searchForPlace("foo", 1000, LatLng(0.0, 0.0))

        verify(observer).onChanged(mainViewModel.placeQueryProgress)

        verify(observer).onChanged(mainViewModel.placeQueryError)
        assertThat(mainViewModel.placeStatus.value?.responseStatus, `is`(ResponseStatus.ERROR))
        assertThat(mainViewModel.placeStatus.value?.error, `is`(instanceOf(PlaceSearchUnknownError::class.java)))

        mainViewModel.placeStatus.removeObserver(observer)
    }
}

inline fun <reified T> mock(): T = mock(T::class.java)