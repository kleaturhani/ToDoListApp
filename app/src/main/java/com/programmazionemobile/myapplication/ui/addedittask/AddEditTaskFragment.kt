package com.programmazionemobile.myapplication.ui.addedittask

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.programmazionemobile.myapplication.util.exhaustive
import com.google.android.material.snackbar.Snackbar
import com.programmazionemobile.myapplication.R
import com.programmazionemobile.myapplication.databinding.FragmentAddEditTaskBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import androidx.core.widget.doAfterTextChanged
import java.text.SimpleDateFormat
import java.text.ParseException

import com.programmazionemobile.myapplication.data.Task;
import kotlinx.android.synthetic.main.fragment_add_edit_task.seekBarDuration
import java.util.Locale


@AndroidEntryPoint

//aggiunge o modifica un task
class AddEditTaskFragment : Fragment(R.layout.fragment_add_edit_task) {

    private val viewModel: AddEditTaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddEditTaskBinding.bind(view)  //collega le viste del layout XML


      //riempe le viste con i dati esistenti dal ViewModel
        binding.apply {
            editTextTaskName.setText(viewModel.taskName)
            checkBoxImportant.isChecked = viewModel.taskImportance
            checkBoxImportant.jumpDrawablesToCurrentState()
            textViewDateCreated.isVisible = viewModel.task != null
            textViewDateCreated.text = "Created: ${viewModel.task?.createdDateFormatted}"
            seekBarDuration.progress = viewModel.taskDuration   //aggiorna la durata dell'attività quando l'utente cambia il valore dela SeekBar
            textViewDuration.text = "Durata: ${seekBarDuration.progress} minuti"
            prioritySpinner.setSelection(viewModel.taskPriority - 1)  // aggiorna le priorità dell'attività quando l'utente seleziona un valore nel Spinner

            seekBarDuration.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    // Aggiorna il valore della durata nel ViewModel
                    viewModel.taskDuration = progress
                    // Aggiorna il testo della textViewDuration
                    textViewDuration.text = "Durata: $progress min"
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    // Azione all'inizio del tracciamento (opzionale)
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    // Azione alla fine del tracciamento (opzionale)
                }
            })


            prioritySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Ottieni la priorità selezionata
                    val selectedPriority: String = prioritySpinner.selectedItem.toString()

                    // Converte il livello di priorità in un valore numerico
                    val priorityValue = when (selectedPriority) {
                        "Alta" -> 1
                        "Media" -> 2
                        "Bassa" -> 3
                        else -> 0 // Gestione di un valore predefinito o errore
                    }

                    // Aggiorna il view model con la priorità selezionata
                    viewModel.updatePriority(priorityValue)

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Azione quando nulla è selezionato (opzionale)
                }

                private fun updatePriority(adapterView: AdapterView<*>?, priority: Int) {
                    // Non è necessario implementare nulla qui, ma deve essere presente
                }

                private fun priorityToString(priority: Int): String {
                    return when (priority) {
                        1 -> "Alta"
                        2 -> "Media"
                        3 -> "Bassa"
                        else -> "" // Tratta il caso di default o errore
                    }
                }
            }


            editTextTaskName.addTextChangedListener {
                viewModel.taskName = it.toString()
            }

            checkBoxImportant.setOnCheckedChangeListener { _, isChecked ->
                viewModel.taskImportance = isChecked
            }

            fabSaveTask.setOnClickListener {
                viewModel.onSaveClick()
            }
        }





        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditTaskEvent.collect { event ->
                when (event) {
                    is AddEditTaskViewModel.AddEditTaskEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                    is AddEditTaskViewModel.AddEditTaskEvent.NavigateBackWithResult -> {
                        binding.editTextTaskName.clearFocus()
                        setFragmentResult(
                            "add_edit_request",
                            bundleOf("add_edit_result" to event.result)
                        )
                        findNavController().popBackStack()
                    }
                }.exhaustive
            }
        }



    }
}