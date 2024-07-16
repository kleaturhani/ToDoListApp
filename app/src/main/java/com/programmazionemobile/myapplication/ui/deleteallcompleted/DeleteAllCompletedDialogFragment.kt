package com.programmazionemobile.myapplication.ui.deleteallcompleted

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint   //framework per l'iniezione delle dipendenze

@AndroidEntryPoint

//quando l'utente elimina i task completati
class DeleteAllCompletedDialogFragment : DialogFragment() {

    private val viewModel: DeleteAllCompletedViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle("Confermare la cancellazione")
            .setMessage("Vuoi davvero eliminare tutti i task completati?")
            .setNegativeButton("Cancella", null)
            .setPositiveButton("Sì") { _, _ ->
                viewModel.onConfirmClick() //viene chiamato il metodo quando si clicca 'SI'
            }
            .create()
}