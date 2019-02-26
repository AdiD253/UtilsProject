package pl.adriandefus.utilsproject

import android.arch.lifecycle.Observer
import android.support.test.espresso.matcher.ViewMatchers.assertThat
import android.widget.Button
import com.nhaarman.mockitokotlin2.atLeast
import com.nhaarman.mockitokotlin2.atMost
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.isA
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowToast
import pl.adriandefus.utilsproject.di.viewmodel.UtilsViewModelFactory
import pl.adriandefus.utilsproject.ui.AnimationStatus
import pl.adriandefus.utilsproject.ui.MainActivity
import pl.adriandefus.utilsproject.ui.MainViewModel

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    private val activityController = Robolectric.buildActivity(MainActivity::class.java).setup()
    private val viewModel = activityController.get().viewModel

    @Mock
    private lateinit var observer: Observer<AnimationStatus>

    @Captor
    private lateinit var animationStatusArgumentCaptor: ArgumentCaptor<AnimationStatus>

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        viewModel.animationStatus.observeForever(observer)
    }

    @Test
    fun should_view_model_on_cleared_be_performed_on_activity_destroyed() {
        activityController.destroy()
        verify(observer).onChanged(null)
    }

    @Test
    fun should_view_be_notified_on_click_performed() {
        val helloWorldBtn = activityController.get().findViewById<Button>(R.id.helloWorldButton)
        viewModel.animationStatus.observeForever(observer)

        helloWorldBtn.performClick()
        verify(observer, atMost(1)).onChanged(animationStatusArgumentCaptor.capture())
        assertThat(ShadowToast.getTextOfLatestToast(), equalTo(animationStatusArgumentCaptor.value.statusInfo))

        helloWorldBtn.performClick()
        verify(observer, atLeast(2)).onChanged(animationStatusArgumentCaptor.capture())
        assertThat(ShadowToast.getTextOfLatestToast(), equalTo(animationStatusArgumentCaptor.value.statusInfo))
    }

    @After
    fun after() {
        viewModel.animationStatus.removeObserver(observer)
    }
}