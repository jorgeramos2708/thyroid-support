package com.jorgeramos.thyroidsupport.domain.repository

import com.jorgeramos.thyroidsupport.domain.Supplement

interface SupplementRepository {

    suspend fun insert(supplement: Supplement): Long
    suspend fun getSupplementsForPatient(patientId: Long): List<Supplement>
    suspend fun delete(id: Long)
}
