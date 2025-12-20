package me.dio.copa.catar.local.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.dio.copa.catar.domain.source.BettingDataSource
import me.dio.copa.catar.domain.source.MatchesDataSource
import me.dio.copa.catar.local.source.BettingDataSourceLocal
import me.dio.copa.catar.local.source.MatchDataSourceLocal
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "settings")

@Module
@InstallIn(SingletonComponent::class)
interface LocalBindsModule {
    @Binds
    fun providesMatchDataSourceLocal(impl: MatchDataSourceLocal): MatchesDataSource.Local

    @Binds
    fun providesBettingDataSourceLocal(impl: BettingDataSourceLocal): BettingDataSource.Local
}

@Module
@InstallIn(SingletonComponent::class)
object LocalProvidesModule {
    @Provides
    @Singleton
    fun providesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }
}
