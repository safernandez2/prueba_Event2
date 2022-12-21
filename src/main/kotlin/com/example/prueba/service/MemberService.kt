package com.example.prueba.service

import com.example.prueba.model.Member
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
class MemberService {
    @Autowired
    lateinit var memberRepository: MemberRepository
    fun save (member:Member):Member{
        return memberRepository.save(member)
    }
    fun list (pageable: Pageable, member: Member):Page<Member>{
        val matcher = ExampleMatcher.matching()
            .withIgnoreNullValues()
            .withMatcher(("fullname"), ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
        return memberRepository.findAll(Example.of(member, matcher), pageable)
    }

    fun listById (id: Long?): Member?{
        return  memberRepository.findById(id)
    }

    fun update(member:Member):Member{
        try {
            memberRepository.findById(member.id)
                ?: throw Exception("ID no existe")
            return  memberRepository.save(member)
        }
        catch (ex:Exception){
            throw ResponseStatusException(HttpStatus.NOT_FOUND,ex.message)
        }

    }

    fun updateName(member:Member): Member {
        try{
            val response = memberRepository.findById(member.id)
                ?: throw Exception("ID no existe")
            response.apply {
                fullname=member.fullname
            }
            return memberRepository.save(response)
        }
        catch (ex:Exception){
            throw ResponseStatusException(HttpStatus.NOT_FOUND,ex.message)
        }
    }


    fun delete (id: Long?):Boolean?{
        memberRepository.findById(id) ?:
        throw  Exception()
        memberRepository.deleteById(id!!)
        return true
    }
}