package com.programmazionemobile.myapplication.data

import com.programmazionemobile.myapplication.di.ApplicationScope
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class], version = 3, exportSchema = false)  //entità è Task
//estende RoomDatabase
//rappresenta il db dell'app
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    //viene chiamato quando il DB viene creato per la 1° volta
    class Callback @Inject constructor(
        private val database: Provider<TaskDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().taskDao()

            applicationScope.launch {
                dao.insert(Task("Studia Programmazione Mobile", priority = 1, duration = 10))
                dao.insert(Task("Fai le spese", priority = 2, duration = 10))
                dao.insert(Task("Telefona i genitori", important = true, priority = 1, duration = 10))
                dao.insert(Task("Prepara la cena", completed = true, priority = 3, duration = 10))
                dao.insert(Task("Finisci gli appunti di Sistemi Operativi", priority = 1, duration = 10))
                dao.insert(Task("Fai l'offerta di telefono", completed = true, priority = 3, duration = 10))
            }
        }
    }
}