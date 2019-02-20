package pl.adriandefus.utilsproject.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.adriandefus.utilsproject.ui.MainActivity

@Module
abstract class ActivityBindingModule {

    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity
}