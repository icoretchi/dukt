package app.ddd.app

import com.benasher44.uuid.Uuid

class DomainException(val id: Uuid, cause: Throwable?) : Exception(cause)