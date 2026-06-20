package ec.edu.puce.students.service

import ec.edu.puce.students.dto.EnrollmentRequest
import ec.edu.puce.students.dto.EnrollmentResponse
import ec.edu.puce.students.dto.StatusUpdateRequest
import ec.edu.puce.students.entity.Enrollment
import ec.edu.puce.students.exception.EnrollmentNotFoundException
import ec.edu.puce.students.exception.StudentNotFoundException
import ec.edu.puce.students.exception.SubjectNotFoundException
import ec.edu.puce.students.mapper.toResponse
import ec.edu.puce.students.repository.EnrollmentRepository
import ec.edu.puce.students.repository.StudentRepository
import ec.edu.puce.students.repository.SubjectRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class EnrollmentService(
    private val enrollmentRepository: EnrollmentRepository,
    private val studentRepository: StudentRepository,
    private val subjectRepository: SubjectRepository
) {

    @Transactional
    fun create(request: EnrollmentRequest): EnrollmentResponse {
        val student = studentRepository.findById(request.studentId).orElseThrow { StudentNotFoundException(request.studentId) }
        val subject = subjectRepository.findById(request.subjectId).orElseThrow { SubjectNotFoundException(request.subjectId) }

        val enrollment = Enrollment(
            student = student,
            subject = subject,
            status = "INSCRITO",
            createdAt = LocalDateTime.now()
        )
        return enrollmentRepository.save(enrollment).toResponse()
    }

    @Transactional(readOnly = true)
    fun findAll(): List<EnrollmentResponse> = enrollmentRepository.findAll().map { it.toResponse() }

    @Transactional(readOnly = true)
    fun findById(id: Long): EnrollmentResponse = enrollmentRepository.findById(id).map { it.toResponse() }.orElseThrow { EnrollmentNotFoundException(id) }

    @Transactional
    fun updateStatus(id: Long, request: StatusUpdateRequest): EnrollmentResponse {
        require(request.status.isNotBlank()) { "Status cannot be blank" }
        val enrollment = enrollmentRepository.findById(id).orElseThrow { EnrollmentNotFoundException(id) }
        enrollment.status = request.status
        return enrollmentRepository.save(enrollment).toResponse()
    }

    @Transactional
    fun delete(id: Long) {
        val enrollment = enrollmentRepository.findById(id).orElseThrow { EnrollmentNotFoundException(id) }
        enrollmentRepository.delete(enrollment)
    }
}