package com.jorgeramos.thyroidsupport.domain.repository

import com.jorgeramos.thyroidsupport.domain.Lab

interface LabRepository {

    suspend fun insert(lab: Lab): Long
    suspend fun getLabsForPatient(patientId: Long): List<Lab>
    suspend fun delete(id: Long)
}
