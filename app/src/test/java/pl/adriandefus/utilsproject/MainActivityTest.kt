package pl.adriandefus.utilsproject

import android.content.Context
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import pl.adriandefus.utilsproject.di.viewmodel.UtilsViewModelFactory
import pl.adriandefus.utilsproject.ui.MainActivity
import pl.adriandefus.utilsproject.ui.MainViewModel

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    private val context = mock(Context::class.java)
    private lateinit var resourceProvider: ResourceProvider
    private val activityController = Robolectric.buildActivity(MainActivity::class.java).setup()
    private lateinit var mainViewModel: MainViewModel

    @Before
    fun init() {
        mainViewModel = mock()
        resourceProvider = ResourceProvider(context)
    }

    @Test
    fun should_view_model_on_cleared_be_performed_on_activity_destroyed() {
        activityController.get().viewModel = mainViewModel
        activityController.destroy()

        verify(activityController.get().viewModel).onClearedFunc()
    }
}