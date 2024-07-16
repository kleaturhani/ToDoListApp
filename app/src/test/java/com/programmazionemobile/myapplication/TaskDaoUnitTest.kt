package com.programmazionemobile.myapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.programmazionemobile.myapplication.data.TaskDao
import com.programmazionemobile.myapplication.data.TaskDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule


@ExperimentalCoroutinesApi
class TaskDaoUnitTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var taskDao: TaskDao
    private lateinit var database: TaskDatabase
    private lateinit var mockTaskDao: TaskDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TaskDatabase::class.java
        ).build()
        taskDao = database.taskDao()


    }

    @After
    fun tearDown() {
        database.close()
    }
}