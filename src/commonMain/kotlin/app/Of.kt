package app.ddd.app

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuid4

/**
 * Command target
 */
data class Of(
    /**
     * User ID that commands
     */
    val user: Uuid,

    /**
     * Command target aggregate root ID
     */
    val id: Uuid = uuid4(),

    /**
     * Target aggregate must be new
     */
    val new: Boolean = false
) {
    val mayExists = !new
}
