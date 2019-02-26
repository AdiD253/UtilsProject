package pl.adriandefus.utilsproject.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import pl.adriandefus.utilsproject.R
import pl.adriandefus.utilsproject.di.annotation.PlacesBaseUrl
import pl.adriandefus.utilsproject.service.GooglePlacesService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
class NetworkModule {

    @Provides
    @Named("baseUrl")
    fun providePlacesBaseUrl(context: Context): String =
        context.getString(R.string.base_url)

    @Provides
    fun provideOkHttpClient(): OkHttpClient =
            OkHttpClient().newBuilder()
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                .readTimeout(5000, TimeUnit.MILLISECONDS)
                .build()

    @Provides
    fun provideRetrofit(@Named("baseUrl") placesBaseUrl: String, client: OkHttpClient): Retrofit =
            Retrofit.Builder()
                .baseUrl(placesBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()

    @Provides
    fun provideGooglePlacesService(retrofit: Retrofit): GooglePlacesService =
            retrofit.create(GooglePlacesService::class.java)
}