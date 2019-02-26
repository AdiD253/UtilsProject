package pl.adriandefus.utilsproject.model

data class PlaceQueryStatus(
    val responseStatus: ResponseStatus,
    var results: List<SearchServiceResult>? = null,
    var error: Throwable? = null
)

enum class ResponseStatus {
    SUCCESS, ERROR, IN_PROGRESS
}