package com.example.prueba.service

import com.example.prueba.model.Conference
import com.example.prueba.model.Register
import com.example.prueba.repository.ConferenceRepository
import com.example.prueba.repository.RegisterRepository
import com.example.prueba.repository.MemberRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException


@Service
class RegisterService {
    @Autowired
    lateinit var registerRepository: RegisterRepository
    @Autowired
    lateinit var memberRepository: MemberRepository
    @Autowired
    lateinit var conferenceRepository: ConferenceRepository

    /*fun save (register:Register):Register{
        try {
            val response = registerRepository.save(register)
            val responseConference: Conference = conferenceRepository.findById(response.conferenceId)
            responseConference.apply {
                totalAttendees = totalAttendees?.plus(register.assisted!!)
            }
            conferenceRepository.save(responseConference)
            return response
        }
        catch (ex:Exception){
            throw ResponseStatusException(HttpStatus.NOT_FOUND, ex.message)
        }
    }
*/




    fun list (pageable: Pageable, register: Register):Page<Register>{
        val matcher = ExampleMatcher.matching()
            .withIgnoreNullValues()
            .withMatcher(("assisted"), ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
        return registerRepository.findAll(Example.of(register, matcher), pageable)
    }

    fun listById (id: Long?): Register?{
        return  registerRepository.findById(id)
    }

    fun update(register:Register):Register{
        try {
            registerRepository.findById(register.id)
                ?: throw Exception("ID no existe")
            return  registerRepository.save(register)
        }
        catch (ex:Exception){
            throw ResponseStatusException(HttpStatus.NOT_FOUND,ex.message)
        }

    }

    fun updateAssisted(register:Register): Register {
        try{
            val response = registerRepository.findById(register.id)
                ?: throw Exception("ID no existe")
            response.apply {
                assisted=register.assisted
            }
            return registerRepository.save(response)
        }
        catch (ex:Exception){
            throw ResponseStatusException(HttpStatus.NOT_FOUND,ex.message)
        }
    }
    fun delete (id: Long?):Boolean?{
        registerRepository.findById(id) ?:
        throw  Exception()
        registerRepository.deleteById(id!!)
        return true
    }
}