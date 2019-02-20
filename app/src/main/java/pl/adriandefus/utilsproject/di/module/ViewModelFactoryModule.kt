package pl.adriandefus.utilsproject.di.module

import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import pl.adriandefus.utilsproject.di.viewmodel.UtilsViewModelFactory

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: UtilsViewModelFactory): ViewModelProvider.Factory
}