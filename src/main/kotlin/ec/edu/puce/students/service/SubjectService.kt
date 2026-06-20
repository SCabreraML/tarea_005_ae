package ec.edu.puce.students.service

import ec.edu.puce.students.dto.SubjectRequest
import ec.edu.puce.students.dto.SubjectResponse
import ec.edu.puce.students.exception.ProfessorNotFoundException
import ec.edu.puce.students.exception.SubjectNotFoundException
import ec.edu.puce.students.mapper.toEntity
import ec.edu.puce.students.mapper.toResponse
import ec.edu.puce.students.repository.ProfessorRepository
import ec.edu.puce.students.repository.SubjectRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SubjectService(
    private val subjectRepository: SubjectRepository,
    private val professorRepository: ProfessorRepository
) {

    @Transactional
    fun create(request: SubjectRequest): SubjectResponse {
        require(request.name.isNotBlank()) { "Subject name cannot be blank" }
        require(request.code.isNotBlank()) { "Subject code cannot be blank" }
        val professor = professorRepository.findById(request.professorId).orElseThrow { ProfessorNotFoundException(request.professorId) }
        return subjectRepository.save(request.toEntity(professor)).toResponse()
    }

    @Transactional(readOnly = true)
    fun findAll(): List<SubjectResponse> = subjectRepository.findAll().map { it.toResponse() }

    @Transactional(readOnly = true)
    fun findById(id: Long): SubjectResponse = subjectRepository.findById(id).map { it.toResponse() }.orElseThrow { SubjectNotFoundException(id) }

    @Transactional
    fun update(id: Long, request: SubjectRequest): SubjectResponse {
        require(request.name.isNotBlank()) { "Subject name cannot be blank" }
        require(request.code.isNotBlank()) { "Subject code cannot be blank" }
        val subject = subjectRepository.findById(id).orElseThrow { SubjectNotFoundException(id) }
        val professor = professorRepository.findById(request.professorId).orElseThrow { ProfessorNotFoundException(request.professorId) }

        subject.name = request.name
        subject.code = request.code
        subject.professor = professor
        return subjectRepository.save(subject).toResponse()
    }

    @Transactional
    fun delete(id: Long) {
        val subject = subjectRepository.findById(id).orElseThrow { SubjectNotFoundException(id) }
        subjectRepository.delete(subject)
    }
}