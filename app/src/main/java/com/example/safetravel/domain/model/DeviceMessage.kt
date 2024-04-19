package com.example.safetravel.domain.model

/**
 * Enum containing different types of messages tags for communicating with the bluetooth device
 */
enum class DeviceMessage(val tag: String) {
    CONNECTED("connected"),
    UUUID("uuid"),
    LOCK_STATE_CHANGED("lock_state_changed");

    companion object {
        fun getByTag(tag: String) = entries.firstOrNull { it.tag in tag }
    }
}
