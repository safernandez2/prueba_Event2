package com.example.prueba.repository

import com.example.prueba.model.Conference
import com.example.prueba.model.Register
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RegisterRepository  : JpaRepository<Register, Long?> {
    fun findById(id: Long?): Register?
}