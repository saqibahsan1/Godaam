package ballpark.buddy.network.servicetype

import ballpark.buddy.network.BuildConfig

sealed class NetworkServiceType(val baseURL: String) {
    data object WeAreCoveredBaseUrl : NetworkServiceType(BuildConfig.APP_BASE_URL)
    data object ThirdPartyBaserUrl : NetworkServiceType("https://vico.callassist.net/api/breakdown-job/")
}
