package pl.adriandefus.utilsproject.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import pl.adriandefus.utilsproject.ResourceProvider
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideApplicationContext(application: Application): Context {
        return application
    }

    @Provides
    @Singleton
    fun provideResourceProvider(context: Context): ResourceProvider {
        return ResourceProvider(context)
    }
}