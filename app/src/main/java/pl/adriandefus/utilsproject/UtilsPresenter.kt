package pl.adriandefus.utilsproject

class UtilsPresenter(
    private val view: MainView,
    private val resourceProvider: ResourceProvider
) {

    fun showHelloWorld() {
        val helloWorld = resourceProvider.strings.getHelloWorld()

        view.showToast(helloWorld)
    }
}