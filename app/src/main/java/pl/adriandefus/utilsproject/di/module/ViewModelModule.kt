package pl.adriandefus.utilsproject.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import pl.adriandefus.utilsproject.di.annotation.ViewModelKey
import pl.adriandefus.utilsproject.di.viewmodel.UtilsViewModelFactory
import pl.adriandefus.utilsproject.ui.MainViewModel

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: UtilsViewModelFactory): ViewModelProvider.Factory
}