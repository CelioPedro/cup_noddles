package me.dio.copa.catar.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.dio.copa.catar.data.MatchesRepositoryImpl
import me.dio.copa.catar.data.repository.TeamsRepositoryImpl
import me.dio.copa.catar.domain.repositories.MatchesRepository
import me.dio.copa.catar.domain.repositories.TeamsRepository

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindsMatchesRepository(impl: MatchesRepositoryImpl): MatchesRepository

    @Binds
    fun bindsTeamsRepository(impl: TeamsRepositoryImpl): TeamsRepository
}
