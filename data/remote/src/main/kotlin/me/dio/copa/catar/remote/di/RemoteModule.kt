package me.dio.copa.catar.remote.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.dio.copa.catar.domain.source.MatchesDataSource
import me.dio.copa.catar.domain.source.TeamsDataSource
import me.dio.copa.catar.remote.source.MatchDataSourceRemote

@Module
@InstallIn(SingletonComponent::class)
interface RemoteModule {
    @Binds
    fun providesMatchesDataSource(impl: MatchDataSourceRemote): MatchesDataSource.Remote

    @Binds
    fun providesTeamsDataSource(impl: MatchDataSourceRemote): TeamsDataSource.Remote
}