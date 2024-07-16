package com.programmazionemobile.myapplication

import android.content.ContextWrapper
import com.programmazionemobile.myapplication.data.FilterPreferences
import com.programmazionemobile.myapplication.data.PreferencesManager
import com.programmazionemobile.myapplication.data.SortOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TestContextProvider {
    fun getContext(): android.content.Context {
        return android.content.ContextWrapper(androidx.test.core.app.ApplicationProvider.getApplicationContext())
    }
}

@ExperimentalCoroutinesApi
class PreferencesManagerUnitTest {

    private lateinit var preferencesManager: PreferencesManager

    @Before
    fun setup() {
        // Inizializza la classe PreferencesManager con un contesto di test
        val context = TestContextProvider().getContext()
        preferencesManager = PreferencesManager(context)
    }

    @Test
    fun testDefaultPreferences() = runBlockingTest {
        // Verifica che le preferenze predefinite siano impostate correttamente
        val defaultPreferences = FilterPreferences(SortOrder.BY_DATE, false)
        assertEquals(defaultPreferences, preferencesManager.preferencesFlow.first())
    }

    @Test
    fun testUpdateSortOrder() = runBlockingTest {
        // Verifica che la preferenza di ordinamento venga aggiornata correttamente
        preferencesManager.updateSortOrder(SortOrder.BY_NAME)
        val updatedPreferences = FilterPreferences(SortOrder.BY_NAME, false)
        assertEquals(updatedPreferences, preferencesManager.preferencesFlow.first())
    }

    @Test
    fun testUpdateHideCompleted() = runBlockingTest {
        // Verifica che la preferenza di nascondere i task completati venga aggiornata correttamente
        preferencesManager.updateHideCompleted(true)
        val updatedPreferences = FilterPreferences(SortOrder.BY_DATE, true)
        assertEquals(updatedPreferences, preferencesManager.preferencesFlow.first())
    }
}