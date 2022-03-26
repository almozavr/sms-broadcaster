package rip.zarmy.smsbroadcaster

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import rip.zarmy.smsbroadcaster.shared.entity.Status

@Parcelize
data class MainState(
    val token: String,
    val fetchStatus: Status,
    val smsBody: String,
    val phoneNumbers: List<String>,
    val sendStatus: Status,
    val sendNumbers: Int,
) : Parcelable

sealed class MainEffect {
    data class FetchError(val authFail: Boolean) : MainEffect()
}
