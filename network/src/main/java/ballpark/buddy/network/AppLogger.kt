package ballpark.buddy.network

import timber.log.Timber

object AppLogger {

    private const val MSG_LENGTH_LIMIT = 3000

    val isDebugEnabled: Boolean
        get() = true

    fun networkLog(message: String?) {
        if ((message?.length ?: 0) > MSG_LENGTH_LIMIT) {
            handleLongLogMessage(message ?: EMPTY_STRING)
            return
        }
        d(message = message)
    }

    fun d(
        throwable: Throwable? = null,
        exception: Exception? = null,
        message: String? = null,
    ) {
        Timber.d(throwable)
        Timber.d(exception)
        Timber.d(message)
    }

    fun e(
        throwable: Throwable? = null,
        exception: Exception? = null,
        message: String? = null,
    ) {
        Timber.e(throwable)
        Timber.e(exception)
        Timber.e(message)
    }


    private fun handleLongLogMessage(msg: String) {
        // Split up and log each substring separately...
        var message = msg
        while (message.length > MSG_LENGTH_LIMIT) {
            try {
                networkLog(message.substring(0, MSG_LENGTH_LIMIT))
                message = message.substring(MSG_LENGTH_LIMIT)
            } catch (e: OutOfMemoryError) {
                return
            }
        }
        // Log the last remaining substring < MSG_LENGTH_LIMIT
        if (message.isNotEmpty()) {
            networkLog(message)
        }
    }
}
