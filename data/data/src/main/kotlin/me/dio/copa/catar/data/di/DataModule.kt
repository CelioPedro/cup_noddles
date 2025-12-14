package me.dio.copa.catar.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.dio.copa.catar.data.repository.MatchesRepositoryImpl
import me.dio.copa.catar.data.repository.TeamsRepositoryImpl
import me.dio.copa.catar.domain.repositories.MatchesRepository
import me.dio.copa.catar.domain.repositories.TeamsRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    abstract fun providesMatchesRepository(impl: MatchesRepositoryImpl): MatchesRepository

    @Binds
    abstract fun providesTeamsRepository(impl: TeamsRepositoryImpl): TeamsRepository
}
