package me.dio.copa.catar.data.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.dio.copa.catar.data.repository.MatchesRepositoryImpl
import me.dio.copa.catar.data.repository.TeamsRepositoryImpl
import me.dio.copa.catar.domain.repositories.MatchesRepository
import me.dio.copa.catar.domain.repositories.TeamsRepository
import me.dio.copa.catar.local.AppDatabase
import me.dio.copa.catar.remote.CopaApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    abstract fun providesMatchesRepository(impl: MatchesRepositoryImpl): MatchesRepository

    @Binds
    abstract fun providesTeamsRepository(impl: TeamsRepositoryImpl): TeamsRepository
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
        .baseUrl("https://gist.githubusercontent.com/")
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    @Provides
    @Singleton
    fun provideCopaApi(retrofit: Retrofit): CopaApi = retrofit.create(CopaApi::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "copa-catar.db"
    ).build()

    @Provides
    fun provideMatchDao(appDatabase: AppDatabase) = appDatabase.matchDao()

    @Provides
    fun provideTeamDao(appDatabase: AppDatabase) = appDatabase.teamDao()
}
