package pl.adriandefus.utilsproject

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import android.content.Context
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
import pl.adriandefus.utilsproject.ui.AnimationStatus
import pl.adriandefus.utilsproject.ui.MainViewModel

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
    fun should_resourceprovider_return_correct_values() {
        assertThat(resourceProvider.strings.getAnimActive(), `is`(ANIM_ACTIVE))
        assertThat(resourceProvider.strings.getAnimInactive(), `is`(ANIM_INACTIVE))
    }

    @Test
    fun should_toggle_animation() {
        val mainViewModel = MainViewModel(resourceProvider)
        val observer = mock<Observer<AnimationStatus>>()

        mainViewModel.animationStatus.observeForever(observer)

        mainViewModel.toggleAnimation()
        verify(observer).onChanged(mainViewModel.animationActive)

        mainViewModel.toggleAnimation()
        verify(observer).onChanged(mainViewModel.animationInactive)

        mainViewModel.animationStatus.removeObserver(observer)
    }
}

inline fun <reified T> mock(): T = Mockito.mock(T::class.java)