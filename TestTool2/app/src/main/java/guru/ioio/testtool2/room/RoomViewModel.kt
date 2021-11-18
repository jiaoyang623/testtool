package guru.ioio.testtool2.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import guru.ioio.testtool2.db.OneDatabase
import guru.ioio.testtool2.db.model.ImageBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoomViewModel : ViewModel() {
    val imageList = OneDatabase.getDatabase().imageDao().getAll()

    fun addImage(image: ImageBean) {
        viewModelScope.launch(Dispatchers.IO) {
            OneDatabase.getDatabase().imageDao().insert(image)
        }
    }

    fun removeImage(image: ImageBean) {
        viewModelScope.launch(Dispatchers.IO) {
            OneDatabase.getDatabase().imageDao().delete(image)
        }
    }
}