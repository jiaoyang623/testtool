package guru.ioio.testtool2.db.model

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "image",
    primaryKeys = ["title"],
    indices = [Index("title")]
)
data class ImageBean(
    val title: String,
    val image: String,
)
