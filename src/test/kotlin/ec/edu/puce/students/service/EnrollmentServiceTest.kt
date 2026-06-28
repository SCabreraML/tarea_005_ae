package ec.edu.puce.students.service

import ec.edu.puce.students.dto.EnrollmentRequest
import ec.edu.puce.students.dto.StatusUpdateRequest
import ec.edu.puce.students.entity.Enrollment
import ec.edu.puce.students.entity.Professor
import ec.edu.puce.students.entity.Student
import ec.edu.puce.students.entity.Subject
import ec.edu.puce.students.exception.EnrollmentNotFoundException
import ec.edu.puce.students.exception.StudentNotFoundException
import ec.edu.puce.students.exception.SubjectNotFoundException
import ec.edu.puce.students.repository.EnrollmentRepository
import ec.edu.puce.students.repository.StudentRepository
import ec.edu.puce.students.repository.SubjectRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class EnrollmentServiceTest {

    @Mock
    private lateinit var enrollmentRepository: EnrollmentRepository

    @Mock
    private lateinit var studentRepository: StudentRepository

    @Mock
    private lateinit var subjectRepository: SubjectRepository

    @InjectMocks
    private lateinit var enrollmentService: EnrollmentService

    @Test
    fun `create should return EnrollmentResponse when student and subject exist`() {
        // Arrange
        val studentId = 1L
        val subjectId = 2L
        val request = EnrollmentRequest(studentId = studentId, subjectId = subjectId)

        val student = Student(id = studentId, name = "John Doe", email = "john@example.com")
        val professor = Professor(id = 1L, name = "Dr. Smith", email = "smith@example.com")
        val subject = Subject(id = subjectId, name = "Math", code = "MATH101", professor = professor)

        val enrollment = Enrollment(
            id = 1L,
            student = student,
            subject = subject,
            status = "INSCRITO",
            createdAt = LocalDateTime.now()
        )

        `when`(studentRepository.findById(studentId)).thenReturn(Optional.of(student))
        `when`(subjectRepository.findById(subjectId)).thenReturn(Optional.of(subject))
        `when`(enrollmentRepository.save(any(Enrollment::class.java))).thenReturn(enrollment)

        // Act
        val response = enrollmentService.create(request)

        // Assert
        assertEquals(enrollment.id, response.id)
        assertEquals("INSCRITO", response.status)
        assertEquals(studentId, response.student.id)
        assertEquals(subjectId, response.subject.id)
        verify(studentRepository).findById(studentId)
        verify(subjectRepository).findById(subjectId)
        verify(enrollmentRepository).save(any(Enrollment::class.java))
    }

    @Test
    fun `create should throw StudentNotFoundException when student does not exist`() {
        // Arrange
        val studentId = 1L
        val request = EnrollmentRequest(studentId = studentId, subjectId = 2L)
        `when`(studentRepository.findById(studentId)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<StudentNotFoundException> {
            enrollmentService.create(request)
        }
    }

    @Test
    fun `create should throw SubjectNotFoundException when subject does not exist`() {
        // Arrange
        val studentId = 1L
        val subjectId = 2L
        val request = EnrollmentRequest(studentId = studentId, subjectId = subjectId)
        val student = Student(id = studentId, name = "John Doe", email = "john@example.com")

        `when`(studentRepository.findById(studentId)).thenReturn(Optional.of(student))
        `when`(subjectRepository.findById(subjectId)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<SubjectNotFoundException> {
            enrollmentService.create(request)
        }
    }

    @Test
    fun `findAll should return list of EnrollmentResponse`() {
        // Arrange
        val student = Student(id = 1L, name = "John Doe", email = "john@example.com")
        val professor = Professor(id = 1L, name = "Dr. Smith", email = "smith@example.com")
        val subject = Subject(id = 1L, name = "Math", code = "MATH101", professor = professor)
        val enrollment = Enrollment(id = 1L, student = student, subject = subject, status = "INSCRITO", createdAt = LocalDateTime.now())

        `when`(enrollmentRepository.findAll()).thenReturn(listOf(enrollment))

        // Act
        val responseList = enrollmentService.findAll()

        // Assert
        assertEquals(1, responseList.size)
        assertEquals(enrollment.id, responseList[0].id)
        verify(enrollmentRepository).findAll()
    }

    @Test
    fun `findById should return EnrollmentResponse when enrollment exists`() {
        // Arrange
        val id = 1L
        val student = Student(id = 1L, name = "John Doe", email = "john@example.com")
        val professor = Professor(id = 1L, name = "Dr. Smith", email = "smith@example.com")
        val subject = Subject(id = 1L, name = "Math", code = "MATH101", professor = professor)
        val enrollment = Enrollment(id = id, student = student, subject = subject, status = "INSCRITO", createdAt = LocalDateTime.now())

        `when`(enrollmentRepository.findById(id)).thenReturn(Optional.of(enrollment))

        // Act
        val response = enrollmentService.findById(id)

        // Assert
        assertEquals(id, response.id)
        verify(enrollmentRepository).findById(id)
    }

    @Test
    fun `findById should throw EnrollmentNotFoundException when enrollment does not exist`() {
        // Arrange
        val id = 1L
        `when`(enrollmentRepository.findById(id)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<EnrollmentNotFoundException> {
            enrollmentService.findById(id)
        }
    }

    @Test
    fun `updateStatus should return EnrollmentResponse when valid`() {
        // Arrange
        val id = 1L
        val request = StatusUpdateRequest(status = "COMPLETADO")
        val student = Student(id = 1L, name = "John Doe", email = "john@example.com")
        val professor = Professor(id = 1L, name = "Dr. Smith", email = "smith@example.com")
        val subject = Subject(id = 1L, name = "Math", code = "MATH101", professor = professor)
        val enrollment = Enrollment(id = id, student = student, subject = subject, status = "INSCRITO", createdAt = LocalDateTime.now())
        val updatedEnrollment = Enrollment(id = id, student = student, subject = subject, status = "COMPLETADO", createdAt = enrollment.createdAt)

        `when`(enrollmentRepository.findById(id)).thenReturn(Optional.of(enrollment))
        `when`(enrollmentRepository.save(any(Enrollment::class.java))).thenReturn(updatedEnrollment)

        // Act
        val response = enrollmentService.updateStatus(id, request)

        // Assert
        assertEquals(id, response.id)
        assertEquals("COMPLETADO", response.status)
        verify(enrollmentRepository).findById(id)
        verify(enrollmentRepository).save(any(Enrollment::class.java))
    }

    @Test
    fun `updateStatus should throw IllegalArgumentException when status is blank`() {
        // Arrange
        val id = 1L
        val request = StatusUpdateRequest(status = "")

        // Act & Assert
        val exception = assertThrows<IllegalArgumentException> {
            enrollmentService.updateStatus(id, request)
        }
        assertEquals("Status cannot be blank", exception.message)
    }

    @Test
    fun `updateStatus should throw EnrollmentNotFoundException when enrollment does not exist`() {
        // Arrange
        val id = 1L
        val request = StatusUpdateRequest(status = "COMPLETADO")
        `when`(enrollmentRepository.findById(id)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<EnrollmentNotFoundException> {
            enrollmentService.updateStatus(id, request)
        }
    }

    @Test
    fun `delete should call delete on repository when enrollment exists`() {
        // Arrange
        val id = 1L
        val student = Student(id = 1L, name = "John Doe", email = "john@example.com")
        val professor = Professor(id = 1L, name = "Dr. Smith", email = "smith@example.com")
        val subject = Subject(id = 1L, name = "Math", code = "MATH101", professor = professor)
        val enrollment = Enrollment(id = id, student = student, subject = subject, status = "INSCRITO", createdAt = LocalDateTime.now())

        `when`(enrollmentRepository.findById(id)).thenReturn(Optional.of(enrollment))

        // Act
        enrollmentService.delete(id)

        // Assert
        verify(enrollmentRepository).findById(id)
        verify(enrollmentRepository).delete(enrollment)
    }

    @Test
    fun `delete should throw EnrollmentNotFoundException when enrollment does not exist`() {
        // Arrange
        val id = 1L
        `when`(enrollmentRepository.findById(id)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<EnrollmentNotFoundException> {
            enrollmentService.delete(id)
        }
    }
}
