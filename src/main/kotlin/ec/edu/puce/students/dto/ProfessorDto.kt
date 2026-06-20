package ec.edu.puce.students.dto

data class ProfessorRequest(
    val name: String,
    val email: String
)

data class ProfessorResponse(
    val id: Long,
    val name: String,
    val email: String
)