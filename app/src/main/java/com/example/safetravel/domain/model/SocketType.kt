package com.example.safetravel.domain.model

import java.util.UUID

enum class SocketType(val uuid: UUID) {
    SECURE(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")),
    INSECURE(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))
}
