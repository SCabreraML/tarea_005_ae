package ec.edu.puce.students.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.LocalDateTime

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(
        StudentNotFoundException::class,
        ProfessorNotFoundException::class,
        SubjectNotFoundException::class,
        EnrollmentNotFoundException::class
    )
    fun handleNotFound(ex: RuntimeException): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            message = ex.message ?: "Resource not found",
            timestamp = LocalDateTime.now()
        )
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(ex: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            message = ex.message ?: "Invalid request parameters",
            timestamp = LocalDateTime.now()
        )
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }
}

data class ErrorResponse(
    val status: Int,
    val message: String,
    val timestamp: LocalDateTime
)