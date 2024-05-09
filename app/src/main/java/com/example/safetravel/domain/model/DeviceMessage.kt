package com.example.safetravel.domain.model

/**
 * Enum containing different types of messages tags for communicating with the bluetooth device
 */
enum class DeviceMessage(val tag: String) {
    CONNECTED("connected"),
    UUID("uuid"),
    UNLOCK("unlock");

    companion object {
        fun getByTag(tag: String) = entries.firstOrNull { it.tag in tag }
    }
}
