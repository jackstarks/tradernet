package com.jackstarks.tradernet.ui.fragments

import android.util.Log
import androidx.lifecycle.*
import com.google.gson.Gson
import com.jackstarks.tradernet.SocketUpdate
import com.jackstarks.tradernet.WebServicesProvider
import com.jackstarks.tradernet.model.Ticker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import org.json.JSONArray

class MyViewModelFactory(val interactor: MainInteractor) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        modelClass.getConstructor(MainInteractor::class.java)
            .newInstance(interactor)
}

class SplashViewModel constructor(private val interactor: MainInteractor) :
    ViewModel() {
    private val _dataString = MutableLiveData<String>()
    val dataString: LiveData<String>
        get() = _dataString


    private var _tickers = MutableLiveData<MutableSet<Ticker>>()
    val tickers: LiveData<MutableSet<Ticker>>
        get() = _tickers

    init {
        _tickers.value = HashSet()

        subscribeToSocketEvents()
    }

    @ExperimentalCoroutinesApi
    fun subscribeToSocketEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            try {

                interactor.startSocket().consumeEach {
                    if (it.exception == null) {
                        val resArray = JSONArray("${it.text}")
                        Log.d("Rest", "Collecting : ${it.text}")

                        if (resArray[0].toString().equals("q", true)) {
                            val ticker = Gson().fromJson(resArray[1].toString(), Ticker::class.java)
                            _dataString.postValue(ticker.toString())
                            _tickers.value?.add(ticker)
                            _tickers.postValue(_tickers.value)
                        }
                    } else {
                        onSocketError(it.exception)
                    }
                }

            } catch (ex: java.lang.Exception) {
                onSocketError(ex)
            }
        }
    }

    private fun onSocketError(ex: Throwable) {
        println("Error occurred : ${ex.message}")
    }

    override fun onCleared() {
        interactor.stopSocket()
        super.onCleared()
    }

}

class MainInteractor constructor(private val repository: MainRepository) {

    @ExperimentalCoroutinesApi
    fun stopSocket() {
        repository.closeSocket()
    }

    @ExperimentalCoroutinesApi
    fun startSocket(): Channel<SocketUpdate> = repository.startSocket()


}


class MainRepository constructor(private val webServicesProvider: WebServicesProvider) {

    @ExperimentalCoroutinesApi
    fun startSocket(): Channel<SocketUpdate> =
        webServicesProvider.startSocket()

    @ExperimentalCoroutinesApi
    fun closeSocket() {
        webServicesProvider.stopSocket()
    }

}







