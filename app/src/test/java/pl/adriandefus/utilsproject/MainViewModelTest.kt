package pl.adriandefus.utilsproject

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import android.content.Context
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.verification.VerificationMode
import pl.adriandefus.utilsproject.ui.AnimationStatus
import pl.adriandefus.utilsproject.ui.MainViewModel
import pl.adriandefus.utilsproject.ui.Status

@RunWith(JUnit4::class)
class MainViewModelTest {

    companion object {
        private const val ANIM_ACTIVE = "Animation active"
        private const val ANIM_INACTIVE = "Animation inactive"
    }

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val context = mock(Context::class.java)
    private lateinit var resourceProvider: ResourceProvider

    @Before
    fun init() {
        resourceProvider = ResourceProvider(context)
        Mockito.`when`(resourceProvider.strings.getAnimActive()).thenReturn(ANIM_ACTIVE)
        Mockito.`when`(resourceProvider.strings.getAnimInactive()).thenReturn(ANIM_INACTIVE)
    }

    @Test
    fun should_get_active_order_string() {
        val activeOrderString = "Animation active"

        assertThat(activeOrderString, `is`(resourceProvider.strings.getAnimActive()))
    }

    @Test
    fun should_toggle_animation() {
        val mainViewModel = MainViewModel(resourceProvider)
        val observer = mock<Observer<AnimationStatus>>()

        mainViewModel.animationStatus.observeForever(observer)

        mainViewModel.toggleAnimation()
        verify(mainViewModel).animationStatus.value
        verify(observer).onChanged(mainViewModel.ANIMATION_ACTIVE)

        mainViewModel.toggleAnimation()
        verify(observer).onChanged(mainViewModel.ANIMATION_INACTIVE)

        mainViewModel.animationStatus.removeObserver(observer)
    }
}

inline fun <reified T> mock(): T = Mockito.mock(T::class.java)