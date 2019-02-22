package pl.adriandefus.utilsproject

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.RootMatchers.withDecorView
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.airbnb.lottie.LottieAnimationView
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import pl.adriandefus.utilsproject.ui.MainActivity

@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentedTest {

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(MainActivity::class.java, true, true)

    private lateinit var resourceProvider: ResourceProvider

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        resourceProvider = ResourceProvider(activityRule.activity)
    }

    @Test
    fun should_toggle_animation_on_anim_button_click() {
        //given
        val lottieView = activityRule.activity.findViewById<LottieAnimationView>(R.id.lottieView)

        //when
        onView(withId(R.id.helloWorldButton)).perform(click())
        //then
        assertThat(lottieView.isAnimating, `is`(true))

        //when
        onView(withId(R.id.helloWorldButton)).perform(click())
        //then
        assertThat(lottieView.isAnimating, `is`(false))
    }

    @Test
    fun should_show_animation_toast_status_depending_on_current_anim() {
        //given
        val activeAnimation = resourceProvider.strings.getAnimActive()
        val inactiveAnimation = resourceProvider.strings.getAnimInactive()

        //when
        onView(withId(R.id.helloWorldButton)).perform(click())
        //then
        onView(withText(activeAnimation))
            .inRoot(withDecorView(not(`is`(activityRule.activity.window.decorView))))
            .check(matches(isDisplayed()))

        //when
        onView(withId(R.id.helloWorldButton)).perform(click())
        //then
        onView(withText(inactiveAnimation))
            .inRoot(withDecorView(not(`is`(activityRule.activity.window.decorView))))
            .check(matches(isDisplayed()))
    }

    @Test
    fun should_view_model_be_cleared_on_activity_destroy() {

    }
}