package com.glc.smartcar.di

import com.glc.smartcar.BuildConfig
import com.glc.smartcar.data.api.ApiService
import com.glc.smartcar.data.repository.AuthRepositoryInterface
import com.glc.smartcar.data.repository.AvaliacaoRepositoryInterface
import com.glc.smartcar.data.repository.FipeRepositoryInterface
import com.glc.smartcar.data.local.TokenManager
import com.glc.smartcar.data.repository.impl.AuthRepository
import com.glc.smartcar.data.repository.impl.AvaliacaoRepository
import com.glc.smartcar.data.repository.impl.FipeRepository
import com.glc.smartcar.ui.auth.AuthViewModel
import com.glc.smartcar.ui.history.HistoryViewModel
import com.glc.smartcar.ui.newevaluation.NewEvaluationViewModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.Retrofit

val networkModule = module {

    single { TokenManager(androidContext()) }

    single {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }

    single {

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val authInterceptor = Interceptor { chain ->
            val tokenManager: TokenManager = get()
            val token = tokenManager.obterToken()

            val request = chain.request().newBuilder()

            if (!token.isNullOrEmpty()) {
                request.addHeader("Authorization", "Bearer $token")
            }
            chain.proceed(request.build())
        }

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
    }

    single {
        val contentType = "application/json".toMediaType()

        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(get())
            .addConverterFactory(get<Json>().asConverterFactory(contentType))
            .build()
    }

    single { get<Retrofit>().create(ApiService::class.java) }

}

val repositoryModule = module {

    single<AuthRepositoryInterface> {
        AuthRepository(
            apiService = get(),
            tokenManager = get()
        )
    }

    single<AvaliacaoRepositoryInterface> {
        AvaliacaoRepository(
            apiService = get()
        )
    }

    single<FipeRepositoryInterface> {
        FipeRepository(
            apiService = get()
        )
    }
}

val viewModelModule = module {

    viewModelOf(::AuthViewModel)
    viewModelOf(::HistoryViewModel)
    viewModelOf(::NewEvaluationViewModel)
}
