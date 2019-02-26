package pl.adriandefus.utilsproject.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import pl.adriandefus.utilsproject.UtilsApp
import pl.adriandefus.utilsproject.di.module.*
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    ActivityBindingModule::class,
    NetworkModule::class,
    ViewModelModule::class,
    RepositoryModule::class
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