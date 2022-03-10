package ua.sprotyv.smsbroadcaster

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MainState(
    val fetchStatus: Status,
    val smsBody: String,
    val phoneNumbers: List<String>,
    val sendStatus: Status,
    val sendNumbers: Int,
) : Parcelable {
    enum class Status {
        IDLE, PROGRESS, COMPLETE
    }
}

sealed class MainEffect
