package com.programmazionemobile.myapplication.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

//classe di dati che rappresenta un task all'interno dell'app
@Entity(tableName = "task_table")  //è una entità del DB Room
@Parcelize
data class Task(
    val name: String,
    val important: Boolean = false,
    val completed: Boolean = false,
    val priority: Int,
    val duration: Int,
    val created: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true) val id: Long = 0
) : Parcelable {
    //restituisce la data di creazione del task in formato di data e ora
    val createdDateFormatted: String
        get() = DateFormat.getDateTimeInstance().format(created)
}