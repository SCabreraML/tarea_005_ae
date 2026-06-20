package ec.edu.puce.students.mapper

import ec.edu.puce.students.dto.*
import ec.edu.puce.students.entity.*
import java.time.LocalDateTime

// Mappers de Student
fun StudentRequest.toEntity(): Student = Student(name = this.name, email = this.email)
fun Student.toResponse(): StudentResponse = StudentResponse(id = this.id!!, name = this.name, email = this.email)

// Mappers de Professor
fun ProfessorRequest.toEntity(): Professor = Professor(name = this.name, email = this.email)
fun Professor.toResponse(): ProfessorResponse = ProfessorResponse(id = this.id!!, name = this.name, email = this.email)

// Mappers de Subject
fun SubjectRequest.toEntity(professor: Professor): Subject = Subject(name = this.name, code = this.code, professor = professor)
fun Subject.toResponse(): SubjectResponse = SubjectResponse(id = this.id!!, name = this.name, code = this.code, professor = this.professor.toResponse())

// Mappers de Enrollment
fun Enrollment.toResponse(): EnrollmentResponse = EnrollmentResponse(
    id = this.id!!,
    createdAt = this.createdAt,
    status = this.status,
    student = this.student.toResponse(),
    subject = this.subject.toResponse()
)