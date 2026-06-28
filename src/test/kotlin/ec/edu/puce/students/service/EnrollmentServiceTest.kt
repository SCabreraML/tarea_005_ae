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
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.util.Optional

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

        Mockito.`when`(studentRepository.findById(studentId)).thenReturn(Optional.of(student))
        Mockito.`when`(subjectRepository.findById(subjectId)).thenReturn(Optional.of(subject))
        Mockito.`when`(enrollmentRepository.save(ArgumentMatchers.any(Enrollment::class.java))).thenReturn(enrollment)

        // Act
        val response = enrollmentService.create(request)

        // Assert
        Assertions.assertEquals(enrollment.id, response.id)
        Assertions.assertEquals("INSCRITO", response.status)
        Assertions.assertEquals(studentId, response.student.id)
        Assertions.assertEquals(subjectId, response.subject.id)
        Mockito.verify(studentRepository).findById(studentId)
        Mockito.verify(subjectRepository).findById(subjectId)
        Mockito.verify(enrollmentRepository).save(ArgumentMatchers.any(Enrollment::class.java))
    }

    @Test
    fun `create should throw StudentNotFoundException when student does not exist`() {
        // Arrange
        val studentId = 1L
        val request = EnrollmentRequest(studentId = studentId, subjectId = 2L)
        Mockito.`when`(studentRepository.findById(studentId)).thenReturn(Optional.empty())

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

        Mockito.`when`(studentRepository.findById(studentId)).thenReturn(Optional.of(student))
        Mockito.`when`(subjectRepository.findById(subjectId)).thenReturn(Optional.empty())

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
        val enrollment = Enrollment(
            id = 1L,
            student = student,
            subject = subject,
            status = "INSCRITO",
            createdAt = LocalDateTime.now()
        )

        Mockito.`when`(enrollmentRepository.findAll()).thenReturn(listOf(enrollment))

        // Act
        val responseList = enrollmentService.findAll()

        // Assert
        Assertions.assertEquals(1, responseList.size)
        Assertions.assertEquals(enrollment.id, responseList[0].id)
        Mockito.verify(enrollmentRepository).findAll()
    }

    @Test
    fun `findById should return EnrollmentResponse when enrollment exists`() {
        // Arrange
        val id = 1L
        val student = Student(id = 1L, name = "John Doe", email = "john@example.com")
        val professor = Professor(id = 1L, name = "Dr. Smith", email = "smith@example.com")
        val subject = Subject(id = 1L, name = "Math", code = "MATH101", professor = professor)
        val enrollment = Enrollment(
            id = id,
            student = student,
            subject = subject,
            status = "INSCRITO",
            createdAt = LocalDateTime.now()
        )

        Mockito.`when`(enrollmentRepository.findById(id)).thenReturn(Optional.of(enrollment))

        // Act
        val response = enrollmentService.findById(id)

        // Assert
        Assertions.assertEquals(id, response.id)
        Mockito.verify(enrollmentRepository).findById(id)
    }

    @Test
    fun `findById should throw EnrollmentNotFoundException when enrollment does not exist`() {
        // Arrange
        val id = 1L
        Mockito.`when`(enrollmentRepository.findById(id)).thenReturn(Optional.empty())

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
        val enrollment = Enrollment(
            id = id,
            student = student,
            subject = subject,
            status = "INSCRITO",
            createdAt = LocalDateTime.now()
        )
        val updatedEnrollment = Enrollment(
            id = id,
            student = student,
            subject = subject,
            status = "COMPLETADO",
            createdAt = enrollment.createdAt
        )

        Mockito.`when`(enrollmentRepository.findById(id)).thenReturn(Optional.of(enrollment))
        Mockito.`when`(enrollmentRepository.save(ArgumentMatchers.any(Enrollment::class.java))).thenReturn(updatedEnrollment)

        // Act
        val response = enrollmentService.updateStatus(id, request)

        // Assert
        Assertions.assertEquals(id, response.id)
        Assertions.assertEquals("COMPLETADO", response.status)
        Mockito.verify(enrollmentRepository).findById(id)
        Mockito.verify(enrollmentRepository).save(ArgumentMatchers.any(Enrollment::class.java))
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
        Assertions.assertEquals("Status cannot be blank", exception.message)
    }

    @Test
    fun `updateStatus should throw EnrollmentNotFoundException when enrollment does not exist`() {
        // Arrange
        val id = 1L
        val request = StatusUpdateRequest(status = "COMPLETADO")
        Mockito.`when`(enrollmentRepository.findById(id)).thenReturn(Optional.empty())

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
        val enrollment = Enrollment(
            id = id,
            student = student,
            subject = subject,
            status = "INSCRITO",
            createdAt = LocalDateTime.now()
        )

        Mockito.`when`(enrollmentRepository.findById(id)).thenReturn(Optional.of(enrollment))

        // Act
        enrollmentService.delete(id)

        // Assert
        Mockito.verify(enrollmentRepository).findById(id)
        Mockito.verify(enrollmentRepository).delete(enrollment)
    }

    @Test
    fun `delete should throw EnrollmentNotFoundException when enrollment does not exist`() {
        // Arrange
        val id = 1L
        Mockito.`when`(enrollmentRepository.findById(id)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<EnrollmentNotFoundException> {
            enrollmentService.delete(id)
        }
    }
}