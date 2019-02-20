package pl.adriandefus.utilsproject

import android.app.Activity
import android.app.Application
import com.xfinity.resourceprovider.RpApplication
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import pl.adriandefus.utilsproject.di.AppInjector
import javax.inject.Inject

@RpApplication
class UtilsApp : Application(), HasActivityInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector() = activityInjector

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
    }
}