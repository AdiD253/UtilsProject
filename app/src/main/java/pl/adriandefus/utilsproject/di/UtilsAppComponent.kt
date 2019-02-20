package pl.adriandefus.utilsproject.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import pl.adriandefus.utilsproject.UtilsApp
import pl.adriandefus.utilsproject.di.module.AppModule
import pl.adriandefus.utilsproject.di.module.ViewModelFactoryModule
import pl.adriandefus.utilsproject.di.viewmodel.ViewModelModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    ViewModelFactoryModule::class,
    ViewModelModule::class
])
interface UtilsAppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun app(app: Application): Builder

        fun build(): UtilsAppComponent
    }

    fun inject(app: UtilsApp)
}