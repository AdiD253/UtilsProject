package pl.adriandefus.utilsproject.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import pl.adriandefus.utilsproject.R
import pl.adriandefus.utilsproject.ResourceProvider
import pl.adriandefus.utilsproject.di.annotation.PlacesApiKey
import javax.inject.Named
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

    @Provides
    @Singleton
    @Named("placesApiKey")
    fun providePlacesApiKey(context: Context): String {
        return context.getString(R.string.google_places_key)
    }
}