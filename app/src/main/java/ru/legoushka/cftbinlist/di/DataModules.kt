package ru.legoushka.cftbinlist.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import ru.legoushka.cftbinlist.data.source.BinInfoDataSource
import ru.legoushka.cftbinlist.data.source.BinInfoRepository
import ru.legoushka.cftbinlist.data.source.DefaultBinInfoRepository
import ru.legoushka.cftbinlist.data.source.SearchHistorySource
import ru.legoushka.cftbinlist.data.source.local.SearchHistoryDatabase
import ru.legoushka.cftbinlist.data.source.local.SearchHistoryLocalSource
import ru.legoushka.cftbinlist.data.source.remote.BinInfoRemoteDataSource
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideBinInfoRepository(
        remoteDataSource: BinInfoDataSource,
        searchHistorySource: SearchHistorySource,
    ): BinInfoRepository {
        return DefaultBinInfoRepository(remoteDataSource, searchHistorySource)
    }
}


@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Singleton
    @Provides
    fun provideHttpClient(): HttpClient {
        return HttpClient(Android) {
            install(Logging) {
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                json()
            }
        }
    }

    @Singleton
    @Provides
    fun provideBinInfoRemoteDataSource(httpClient: HttpClient): BinInfoDataSource =
        BinInfoRemoteDataSource(httpClient)

    @Singleton
    @Provides
    fun provideSearchHistorySource(dataBase: SearchHistoryDatabase): SearchHistorySource =
        SearchHistoryLocalSource(dataBase.searchHistoryDao())
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): SearchHistoryDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            SearchHistoryDatabase::class.java,
            "History.db"
        ).build()
    }
}
