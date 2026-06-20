package ec.edu.puce.students.service

import ec.edu.puce.students.dto.ProfessorRequest
import ec.edu.puce.students.dto.ProfessorResponse
import ec.edu.puce.students.exception.ProfessorNotFoundException
import ec.edu.puce.students.mapper.toEntity
import ec.edu.puce.students.mapper.toResponse
import ec.edu.puce.students.repository.ProfessorRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProfessorService(private val professorRepository: ProfessorRepository) {

    @Transactional
    fun create(request: ProfessorRequest): ProfessorResponse {
        require(request.name.isNotBlank()) { "Professor name cannot be blank" }
        return professorRepository.save(request.toEntity()).toResponse()
    }

    @Transactional(readOnly = true)
    fun findAll(): List<ProfessorResponse> = professorRepository.findAll().map { it.toResponse() }

    @Transactional(readOnly = true)
    fun findById(id: Long): ProfessorResponse = professorRepository.findById(id).map { it.toResponse() }.orElseThrow { ProfessorNotFoundException(id) }

    @Transactional
    fun update(id: Long, request: ProfessorRequest): ProfessorResponse {
        require(request.name.isNotBlank()) { "Professor name cannot be blank" }
        val professor = professorRepository.findById(id).orElseThrow { ProfessorNotFoundException(id) }
        professor.name = request.name
        professor.email = request.email
        return professorRepository.save(professor).toResponse()
    }

    @Transactional
    fun delete(id: Long) {
        val professor = professorRepository.findById(id).orElseThrow { ProfessorNotFoundException(id) }
        professorRepository.delete(professor)
    }
}