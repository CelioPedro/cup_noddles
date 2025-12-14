package me.dio.copa.catar.di

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.dio.copa.catar.data.di.DataModule
import me.dio.copa.catar.local.di.LocalBindsModule
import me.dio.copa.catar.local.di.LocalProvidesModule
import me.dio.copa.catar.remote.di.NetworkModule
import me.dio.copa.catar.remote.di.RemoteBindsModule

@Module(
    includes = [
        DataModule::class,
        LocalBindsModule::class,
        LocalProvidesModule::class,
        RemoteBindsModule::class,
        NetworkModule::class,
    ]
)
@InstallIn(SingletonComponent::class)
abstract class ApplicationModule {
    @Binds
    abstract fun bindContext(application: Application): Context
}
