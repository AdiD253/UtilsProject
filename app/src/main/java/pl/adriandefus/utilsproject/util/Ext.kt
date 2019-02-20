package pl.adriandefus.utilsproject.util

import android.arch.lifecycle.MutableLiveData

infix fun <T>MutableLiveData<T>.post(value: T) {
    postValue(value)
}