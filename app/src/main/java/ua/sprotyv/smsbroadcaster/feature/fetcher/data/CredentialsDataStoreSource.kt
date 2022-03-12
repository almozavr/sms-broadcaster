package ua.sprotyv.smsbroadcaster.feature.fetcher.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import ua.sprotyv.smsbroadcaster.feature.fetcher.domain.CredentialsSource
import ua.sprotyv.smsbroadcaster.feature.fetcher.domain.entity.Credentials

class CredentialsDataStoreSource(private val context: Context) : CredentialsSource {

    private val Context.store: DataStore<Preferences> by preferencesDataStore("credentials_data_store")
    private val tokenKey = stringPreferencesKey("token")

    override suspend fun get(): Credentials? {
        return context.store.data.first()[tokenKey]?.let { Credentials(it) }
    }

    override suspend fun put(credentials: Credentials) {
        context.store.edit { settings ->
            settings[tokenKey] = credentials.token
        }
    }
}
