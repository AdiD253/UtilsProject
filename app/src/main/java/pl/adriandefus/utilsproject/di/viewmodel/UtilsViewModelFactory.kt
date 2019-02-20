package pl.adriandefus.utilsproject.di.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

@Suppress("UNCHECKED_CAST")
class UtilsViewModelFactory @Inject constructor(
    private var creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator: Provider<out ViewModel>? =
            creators[modelClass]
                ?: getCreator(modelClass)
                ?: throw IllegalArgumentException("unknown class $modelClass")

        try {
            return creator!!.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    private fun<T> getCreator(modelClass: Class<T>): Provider<out ViewModel>? {
        for ((key, value) in creators) {
            if (modelClass.isAssignableFrom(key)) {
                return value
            }
        }
        return null
    }
}