package pl.adriandefus.utilsproject

import android.arch.lifecycle.ViewModel
import android.support.test.espresso.matcher.ViewMatchers.assertThat
import org.hamcrest.CoreMatchers.isA
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import pl.adriandefus.utilsproject.ui.MainActivity
import pl.adriandefus.utilsproject.ui.MainViewModel

@RunWith(RobolectricTestRunner::class)
class ViewModelFactoryTest {

    private val activityController = Robolectric.buildActivity(MainActivity::class.java).setup()
    private val factory = activityController.get().viewModelFactory

    @Test
    fun should_factory_create_MainViewModel() {
        assertThat(factory.create(MainViewModel::class.java), isA(MainViewModel::class.java))
    }

    @Test
    fun should_factory_create_FooViewModel() {
        assertThat(factory.create(ViewModel::class.java), isA(ViewModel::class.java))
    }

    @Test(expected = IllegalArgumentException::class)
    fun should_factory_throw_exception_if_passed_illegal_viewmodel_type() {
        factory.create(AnyViewModel::class.java)
    }
}

class AnyViewModel : ViewModel()