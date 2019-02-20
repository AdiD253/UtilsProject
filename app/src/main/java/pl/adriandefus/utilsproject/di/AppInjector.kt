package pl.adriandefus.utilsproject.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjection
import pl.adriandefus.utilsproject.UtilsApp

object AppInjector {

    fun init(utilsApp: UtilsApp) {
        DaggerUtilsAppComponent
            .builder()
            .app(utilsApp)
            .build()
            .inject(utilsApp)

        utilsApp.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                handleActivity(activity)
            }

            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityResumed(activity: Activity) {}

            override fun onActivityStarted(activity: Activity) {}

            override fun onActivityDestroyed(activity: Activity) {}

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {}

            override fun onActivityStopped(activity: Activity) {}
        })
    }

    private fun handleActivity(activity: Activity) {
        if (activity is AppCompatActivity) {
            AndroidInjection.inject(activity)
        }
    }
}