package ec.edu.puce.students.dto

data class SubjectRequest(
    val name: String,
    val code: String,
    val professorId: Long
)

data class SubjectResponse(
    val id: Long,
    val name: String,
    val code: String,
    val professor: ProfessorResponse
)