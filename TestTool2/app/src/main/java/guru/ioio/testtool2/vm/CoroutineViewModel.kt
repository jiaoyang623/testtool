package guru.ioio.testtool2.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CoroutineViewModel:ViewModel() {
    val message: MutableLiveData<String> by lazy { MutableLiveData() }

    fun onBasicClick(){

    }
}