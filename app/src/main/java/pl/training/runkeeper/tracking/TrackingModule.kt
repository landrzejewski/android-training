package pl.training.runkeeper.tracking

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.training.runkeeper.tracking.domain.TrackingService
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class TrackingModule {

    @Singleton
    @Provides
    fun trackingService(): TrackingService = TrackingService()

}