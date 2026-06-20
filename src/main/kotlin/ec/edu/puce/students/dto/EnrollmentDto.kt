package ec.edu.puce.students.dto

import java.time.LocalDateTime

data class EnrollmentRequest(
    val studentId: Long,
    val subjectId: Long
)

data class StatusUpdateRequest(
    val status: String
)

data class EnrollmentResponse(
    val id: Long,
    val createdAt: LocalDateTime,
    val status: String,
    val student: StudentResponse,
    val subject: SubjectResponse
)