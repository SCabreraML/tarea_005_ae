package ec.edu.puce.students.service

import ec.edu.puce.students.dto.StudentRequest
import ec.edu.puce.students.dto.StudentResponse
import ec.edu.puce.students.exception.StudentNotFoundException
import ec.edu.puce.students.mapper.toEntity
import ec.edu.puce.students.mapper.toResponse
import ec.edu.puce.students.repository.StudentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StudentService(private val studentRepository: StudentRepository) {

    @Transactional
    fun create(request: StudentRequest): StudentResponse {
        require(request.name.isNotBlank()) { "Student name cannot be blank" }
        return studentRepository.save(request.toEntity()).toResponse()
    }

    @Transactional(readOnly = true)
    fun findAll(): List<StudentResponse> = studentRepository.findAll().map { it.toResponse() }

    @Transactional(readOnly = true)
    fun findById(id: Long): StudentResponse = studentRepository.findById(id).map { it.toResponse() }.orElseThrow { StudentNotFoundException(id) }

    @Transactional
    fun update(id: Long, request: StudentRequest): StudentResponse {
        require(request.name.isNotBlank()) { "Student name cannot be blank" }
        val student = studentRepository.findById(id).orElseThrow { StudentNotFoundException(id) }
        student.name = request.name
        student.email = request.email
        return studentRepository.save(student).toResponse()
    }

    @Transactional
    fun delete(id: Long) {
        val student = studentRepository.findById(id).orElseThrow { StudentNotFoundException(id) }
        studentRepository.delete(student)
    }
}