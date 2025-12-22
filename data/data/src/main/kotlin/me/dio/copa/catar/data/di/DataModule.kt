package me.dio.copa.catar.data.di

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.dio.copa.catar.data.repository.MatchesRepositoryImpl
import me.dio.copa.catar.data.repository.TeamsRepositoryImpl
import me.dio.copa.catar.domain.repositories.MatchesRepository
import me.dio.copa.catar.domain.repositories.TeamsRepository
import me.dio.copa.catar.domain.source.MatchesDataSource
import me.dio.copa.catar.local.fake.FakeMatchesRepository
import javax.inject.Singleton

private const val USE_FAKE_REPOSITORIES = true

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun providesMatchesRepository(
        local: MatchesDataSource.Local,
        remote: MatchesDataSource.Remote,
        @ApplicationContext context: Context,
        moshi: Moshi
    ): MatchesRepository {
        if (USE_FAKE_REPOSITORIES) {
            return FakeMatchesRepository(context, moshi)
        }
        return MatchesRepositoryImpl(local, remote)
    }

    @Provides
    @Singleton
    fun providesTeamsRepository(impl: TeamsRepositoryImpl): TeamsRepository = impl

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
}
