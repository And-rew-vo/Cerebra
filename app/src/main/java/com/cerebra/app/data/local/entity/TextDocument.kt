package com.cerebra.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "text_documents",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [androidx.room.Index(value = ["userId"])]
)
data class TextDocument(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val title: String,
    val content: String,
    val progress: Int, // 0 to 100
    val lastTrainedAt: Long // Timestamp
)
