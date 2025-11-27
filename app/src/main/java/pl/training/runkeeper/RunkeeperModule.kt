package pl.training.runkeeper

import android.content.Context
import android.util.Log
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BASIC
import pl.training.runkeeper.common.AppIdInterceptor
import pl.training.runkeeper.common.store.SharedPreferencesStore
import pl.training.runkeeper.common.store.Store
import java.util.concurrent.Executors
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RunkeeperModule {

    @Singleton
    @Provides
    fun httpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = BASIC
        return OkHttpClient().newBuilder()
            .addInterceptor(AppIdInterceptor())
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun database(@ApplicationContext context: Context): RunkeeperDatabase = Room
        .databaseBuilder(context, RunkeeperDatabase::class.java, "runkeeper")
        .fallbackToDestructiveMigration(false)
        .setQueryCallback(
            { sqlQuery, bindArgs -> Log.d("RoomQuery", "SQL: $sqlQuery BindArgs: $bindArgs") },
            Executors.newSingleThreadExecutor()
        )
        .build()

    @Singleton
    @Provides
    fun store(@ApplicationContext context: Context): Store = SharedPreferencesStore(context)

}

@Qualifier
annotation class Development

@Qualifier
annotation class Production
