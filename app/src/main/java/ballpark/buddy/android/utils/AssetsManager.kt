package ballpark.buddy.android.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import com.leo.searchablespinner.utils.data.NitaqatDropDownData
import java.io.InputStream

interface AssetsManager {
    suspend fun readJsonFile(fileName: String): Result<NitaqatDropDownData>
}

class DefaultJsonAssets : AssetsManager {

    override suspend fun readJsonFile(fileName: String): Result<NitaqatDropDownData> =
        withContext(Dispatchers.IO) {
            try {
                val classLoader = javaClass.classLoader
                val inputStream: InputStream? = classLoader?.getResourceAsStream(fileName)

                if (inputStream != null) {
                    val jsonString = inputStream.bufferedReader().use { it.readText() }
                    val cookie : NitaqatDropDownData = JSONObject(jsonString) as NitaqatDropDownData
                    Result.success(cookie)
                } else {
                    Result.failure(Exception("File not found: $fileName"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}