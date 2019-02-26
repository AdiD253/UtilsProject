package pl.adriandefus.utilsproject.di.module

import dagger.Binds
import dagger.Module
import pl.adriandefus.utilsproject.repository.GooglePlacesRepository
import pl.adriandefus.utilsproject.repository.GooglePlacesRepositoryImpl

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindGooglePlacesRepository(googlePlacesRepositoryImpl: GooglePlacesRepositoryImpl): GooglePlacesRepository
}