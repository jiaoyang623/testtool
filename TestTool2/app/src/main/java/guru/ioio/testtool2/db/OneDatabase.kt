package guru.ioio.testtool2.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import guru.ioio.testtool2.BaseApp
import guru.ioio.testtool2.db.model.ImageBean
import guru.ioio.testtool2.db.model.ImageDao

@Database(
    entities = [ImageBean::class],
    version = 1,
    exportSchema = false
)
abstract class OneDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: OneDatabase? = null

        fun getDatabase(): OneDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    BaseApp.getInstance().applicationContext,
                    OneDatabase::class.java, "one_db"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}