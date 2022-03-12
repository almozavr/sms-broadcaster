package ua.sprotyv.smsbroadcaster

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ua.sprotyv.smsbroadcaster.shared.entity.Status

@Parcelize
data class MainState(
    val token: String,
    val fetchStatus: Status,
    val smsBody: String,
    val phoneNumbers: List<String>,
    val sendStatus: Status,
    val sendNumbers: Int,
) : Parcelable

sealed class MainEffect
