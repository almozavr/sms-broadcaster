package ua.sprotyv.smsbroadcaster

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MainState(
    val name: String,
) : Parcelable

sealed class MainEffect
