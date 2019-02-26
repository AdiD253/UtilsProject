package pl.adriandefus.utilsproject.runner

import android.app.Application
import android.content.Context
import android.support.test.runner.AndroidJUnitRunner
import pl.adriandefus.utilsproject.app.TestApplication

class CustomTestRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return super.newApplication(cl, TestApplication::class.java.name, context)
    }
}