package ec.edu.puce.students.service

import ec.edu.puce.students.dto.StudentRequest
import ec.edu.puce.students.entity.Student
import ec.edu.puce.students.exception.StudentNotFoundException
import ec.edu.puce.students.repository.StudentRepository
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
import java.util.*

@ExtendWith(MockitoExtension::class)
class StudentServiceTest {

    @Mock
    private lateinit var studentRepository: StudentRepository

    @InjectMocks
    private lateinit var studentService: StudentService

    @Test
    fun `create should return StudentResponse when request is valid`() {
        // Arrange
        val request = StudentRequest(name = "John Doe", email = "john@example.com")
        val student = Student(id = 1L, name = "John Doe", email = "john@example.com")
        `when`(studentRepository.save(any(Student::class.java))).thenReturn(student)

        // Act
        val response = studentService.create(request)

        // Assert
        assertEquals(student.id, response.id)
        assertEquals(student.name, response.name)
        assertEquals(student.email, response.email)
        verify(studentRepository).save(any(Student::class.java))
    }

    @Test
    fun `create should throw IllegalArgumentException when name is blank`() {
        // Arrange
        val request = StudentRequest(name = "", email = "john@example.com")

        // Act & Assert
        val exception = assertThrows<IllegalArgumentException> {
            studentService.create(request)
        }
        assertEquals("Student name cannot be blank", exception.message)
    }

    @Test
    fun `findAll should return list of StudentResponse`() {
        // Arrange
        val student = Student(id = 1L, name = "John Doe", email = "john@example.com")
        `when`(studentRepository.findAll()).thenReturn(listOf(student))

        // Act
        val responseList = studentService.findAll()

        // Assert
        assertEquals(1, responseList.size)
        assertEquals(student.id, responseList[0].id)
        verify(studentRepository).findAll()
    }

    @Test
    fun `findById should return StudentResponse when student exists`() {
        // Arrange
        val id = 1L
        val student = Student(id = id, name = "John Doe", email = "john@example.com")
        `when`(studentRepository.findById(id)).thenReturn(Optional.of(student))

        // Act
        val response = studentService.findById(id)

        // Assert
        assertEquals(id, response.id)
        verify(studentRepository).findById(id)
    }

    @Test
    fun `findById should throw StudentNotFoundException when student does not exist`() {
        // Arrange
        val id = 1L
        `when`(studentRepository.findById(id)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<StudentNotFoundException> {
            studentService.findById(id)
        }
    }

    @Test
    fun `update should return StudentResponse when request is valid and student exists`() {
        // Arrange
        val id = 1L
        val request = StudentRequest(name = "Jane Doe", email = "jane@example.com")
        val existingStudent = Student(id = id, name = "John Doe", email = "john@example.com")
        val updatedStudent = Student(id = id, name = "Jane Doe", email = "jane@example.com")

        `when`(studentRepository.findById(id)).thenReturn(Optional.of(existingStudent))
        `when`(studentRepository.save(any(Student::class.java))).thenReturn(updatedStudent)

        // Act
        val response = studentService.update(id, request)

        // Assert
        assertEquals(id, response.id)
        assertEquals(request.name, response.name)
        assertEquals(request.email, response.email)
        verify(studentRepository).findById(id)
        verify(studentRepository).save(any(Student::class.java))
    }

    @Test
    fun `update should throw IllegalArgumentException when name is blank`() {
        // Arrange
        val id = 1L
        val request = StudentRequest(name = " ", email = "jane@example.com")

        // Act & Assert
        val exception = assertThrows<IllegalArgumentException> {
            studentService.update(id, request)
        }
        assertEquals("Student name cannot be blank", exception.message)
    }

    @Test
    fun `update should throw StudentNotFoundException when student does not exist`() {
        // Arrange
        val id = 1L
        val request = StudentRequest(name = "Jane Doe", email = "jane@example.com")
        `when`(studentRepository.findById(id)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<StudentNotFoundException> {
            studentService.update(id, request)
        }
    }

    @Test
    fun `delete should call delete on repository when student exists`() {
        // Arrange
        val id = 1L
        val student = Student(id = id, name = "John Doe", email = "john@example.com")
        `when`(studentRepository.findById(id)).thenReturn(Optional.of(student))

        // Act
        studentService.delete(id)

        // Assert
        verify(studentRepository).findById(id)
        verify(studentRepository).delete(student)
    }

    @Test
    fun `delete should throw StudentNotFoundException when student does not exist`() {
        // Arrange
        val id = 1L
        `when`(studentRepository.findById(id)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<StudentNotFoundException> {
            studentService.delete(id)
        }
    }
}
