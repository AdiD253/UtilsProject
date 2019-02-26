package pl.adriandefus.utilsproject.model

data class LatLng(
    val lat: Double,
    val lng: Double
) {
    override fun toString() = "${this.lat},${this.lng}"
}