package cn.zzs.xiaoai.face.di

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import cn.zzs.xiaoai.face.data.XIAORepository
import com.airbnb.mvrx.hilt.MavericksViewModelComponent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okio.Path.Companion.toPath
import java.io.File
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class XIAOModule {

    companion object {
        const val dataStoreFileName = "dice.preferences_pb"
    }

    @Provides
    fun provideXIAORepository(
        dataStore: DataStore<Preferences>
    ): XIAORepository {
        return XIAORepository(dataStore)
    }


    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.createWithPath(produceFile = {
            context.filesDir.resolve(dataStoreFileName).absolutePath.toPath()
        })
    }
}