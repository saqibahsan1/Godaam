package ballpark.buddy.android.utils

import java.util.Locale
import java.util.UUID

object Constants {
    const val COUNT_DOWN_TIMER_INTERVAL = 1000L
    const val SCREEN_TRANSITIONS_DELAY = 2000L
    const val OTP_COUNT_DOWN_TIMER = COUNT_DOWN_TIMER_INTERVAL.times(10)
    const val TEXT_PLAIN = "text/plain"

    //Table names
    const val LEAGUE_NAME_TABLE = "league_name_table"
    const val JOB_DUTY_TABLE = "job_duty_table"
    const val GAME_TABLE_STAGE = "game_table_stage"
    const val USER_TABLE_STAGE = "user_table_stage"
    fun getCurrentUnixTimestamp(): Long {
        return System.currentTimeMillis() / 1000
    }

    fun generateRandomId() =  UUID.randomUUID().toString().uppercase(Locale.ROOT)
}
object BuildType {
    const val DEBUG = "debug"
    const val RELEASE = "release"
}

object ArgsConstants {
    const val ARGS_DATA = "argsData"
}