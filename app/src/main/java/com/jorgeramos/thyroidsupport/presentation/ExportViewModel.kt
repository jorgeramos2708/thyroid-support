package com.jorgeramos.thyroidsupport.presentation

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgeramos.thyroidsupport.domain.repository.ExportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ExportViewModel @Inject constructor(
    private val exportRepository: ExportRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _exportedFile = MutableLiveData<File?>(null)
    val exportedFile: LiveData<File?> = _exportedFile

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    fun exportSymptomsCsv(patientId: Long) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val file = exportRepository.exportSymptomsToCsv(patientId)
                _exportedFile.value = file
            } catch (e: Exception) {
                _error.value = e.message
            }
            _loading.value = false
        }
    }

    fun exportLabsCsv(patientId: Long) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val file = exportRepository.exportLabsToCsv(patientId)
                _exportedFile.value = file
            } catch (e: Exception) {
                _error.value = e.message
            }
            _loading.value = false
        }
    }

    fun exportMedicationsCsv(patientId: Long) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val file = exportRepository.exportMedicationsToCsv(patientId)
                _exportedFile.value = file
            } catch (e: Exception) {
                _error.value = e.message
            }
            _loading.value = false
        }
    }

    fun exportSupplementsCsv(patientId: Long) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val file = exportRepository.exportSupplementsToCsv(patientId)
                _exportedFile.value = file
            } catch (e: Exception) {
                _error.value = e.message
            }
            _loading.value = false
        }
    }

    fun exportAllZip(patientId: Long) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val file = exportRepository.exportAllDataToZip(patientId)
                _exportedFile.value = file
            } catch (e: Exception) {
                _error.value = e.message
            }
            _loading.value = false
        }
    }

    fun shareFile(file: File) {
        val authority = "${context.packageName}.fileprovider"
        val contentUri = FileProvider.getUriForFile(context, authority, file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "*/*"
            putExtra(Intent.EXTRA_STREAM, contentUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Compartir exportación"))
    }
}
