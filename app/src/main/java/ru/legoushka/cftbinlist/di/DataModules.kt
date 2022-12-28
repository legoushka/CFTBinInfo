package ru.legoushka.cftbinlist.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import ru.legoushka.cftbinlist.data.source.BinInfoDataSource
import ru.legoushka.cftbinlist.data.source.BinInfoRepository
import ru.legoushka.cftbinlist.data.source.DefaultBinInfoRepository
import ru.legoushka.cftbinlist.data.source.local.BinInfoLocalDataSource
import ru.legoushka.cftbinlist.data.source.remote.BinInfoRemoteDataSource
import javax.inject.Qualifier
import javax.inject.Singleton


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RemoteBinInfoDataSource

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class LocalBinInfoDataSource


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideBinInfoRepository(
        @RemoteBinInfoDataSource remoteDataSource: BinInfoDataSource,
        @LocalBinInfoDataSource localDataSource: BinInfoDataSource,
    ): BinInfoRepository {
        return DefaultBinInfoRepository(remoteDataSource, localDataSource)
    }
}


@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Singleton
    @Provides
    fun provideHttpClient(): HttpClient {
        return HttpClient(Android) {
            install(Logging){
                level = LogLevel.ALL
            }
            install(ContentNegotiation){
                json()
            }
        }
    }

    @Singleton
    @RemoteBinInfoDataSource
    @Provides
    fun provideBinInfoRemoteDataSource(httpClient: HttpClient): BinInfoDataSource = BinInfoRemoteDataSource(httpClient)

    @Singleton
    @LocalBinInfoDataSource
    @Provides
    fun provideBinInfoLocalDataSource(): BinInfoDataSource = BinInfoLocalDataSource()


}
