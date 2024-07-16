package com.programmazionemobile.myapplication.ui

import com.programmazionemobile.myapplication.R
import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint   //lo utilizzo per iniettare dipendenze con Hilt

//estende AppCompatActivity per utilizzare la barra di azione
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController   //NavController serve per gestire la navigazione tra fragments

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //si inizializza l'interfaccia utente
        setContentView(R.layout.activity_main)

        //per trovare navHostFragment utilizzo findFragmentById
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()  //inizializzo navController con il fragment trovato

        setupActionBarWithNavController(navController)  //configuro la barra di azione
    }

    //con onSupportNavigateUp gestisco il pulsante "Indietro" nella barra di azione
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()  //si ritorna nel fragment precedente
    }
}

//costanti per aggiungere e modificare un task
const val ADD_TASK_RESULT_OK = Activity.RESULT_FIRST_USER
const val EDIT_TASK_RESULT_OK = Activity.RESULT_FIRST_USER + 1