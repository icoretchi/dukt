package app.ddd.app

import com.benasher44.uuid.Uuid

data class EventMessage(
    val batch: Int,
    val event: Any,
    val eventName: String,
    val sequence: Int,
    val source: Uuid,
    val sourceName: String,
    val user: Uuid
) {
    val sent = kotlinx.datetime.Clock.System.now()
}