package guru.ioio.testtool2.db.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ImageDao {
    @Insert
    fun insert(imageList: List<ImageBean>)

    @Insert
    fun insert(image: ImageBean)

    @Delete
    fun delete(image: ImageBean)

    @Query("SELECT * FROM image")
    fun getAll(): LiveData<List<ImageBean>>
}