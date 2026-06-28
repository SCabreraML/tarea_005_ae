package ec.edu.puce.students.service

import ec.edu.puce.students.dto.StudentRequest
import ec.edu.puce.students.entity.Student
import ec.edu.puce.students.exception.StudentNotFoundException
import ec.edu.puce.students.repository.StudentRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Optional
import kotlin.collections.get

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
        Mockito.`when`(studentRepository.save(ArgumentMatchers.any(Student::class.java))).thenReturn(student)

        // Act
        val response = studentService.create(request)

        // Assert
        Assertions.assertEquals(student.id, response.id)
        Assertions.assertEquals(student.name, response.name)
        Assertions.assertEquals(student.email, response.email)
        Mockito.verify(studentRepository).save(ArgumentMatchers.any(Student::class.java))
    }

    @Test
    fun `create should throw IllegalArgumentException when name is blank`() {
        // Arrange
        val request = StudentRequest(name = "", email = "john@example.com")

        // Act & Assert
        val exception = assertThrows<IllegalArgumentException> {
            studentService.create(request)
        }
        Assertions.assertEquals("Student name cannot be blank", exception.message)
    }

    @Test
    fun `findAll should return list of StudentResponse`() {
        // Arrange
        val student = Student(id = 1L, name = "John Doe", email = "john@example.com")
        Mockito.`when`(studentRepository.findAll()).thenReturn(listOf(student))

        // Act
        val responseList = studentService.findAll()

        // Assert
        Assertions.assertEquals(1, responseList.size)
        Assertions.assertEquals(student.id, responseList[0].id)
        Mockito.verify(studentRepository).findAll()
    }

    @Test
    fun `findById should return StudentResponse when student exists`() {
        // Arrange
        val id = 1L
        val student = Student(id = id, name = "John Doe", email = "john@example.com")
        Mockito.`when`(studentRepository.findById(id)).thenReturn(Optional.of(student))

        // Act
        val response = studentService.findById(id)

        // Assert
        Assertions.assertEquals(id, response.id)
        Mockito.verify(studentRepository).findById(id)
    }

    @Test
    fun `findById should throw StudentNotFoundException when student does not exist`() {
        // Arrange
        val id = 1L
        Mockito.`when`(studentRepository.findById(id)).thenReturn(Optional.empty())

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

        Mockito.`when`(studentRepository.findById(id)).thenReturn(Optional.of(existingStudent))
        Mockito.`when`(studentRepository.save(ArgumentMatchers.any(Student::class.java))).thenReturn(updatedStudent)

        // Act
        val response = studentService.update(id, request)

        // Assert
        Assertions.assertEquals(id, response.id)
        Assertions.assertEquals(request.name, response.name)
        Assertions.assertEquals(request.email, response.email)
        Mockito.verify(studentRepository).findById(id)
        Mockito.verify(studentRepository).save(ArgumentMatchers.any(Student::class.java))
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
        Assertions.assertEquals("Student name cannot be blank", exception.message)
    }

    @Test
    fun `update should throw StudentNotFoundException when student does not exist`() {
        // Arrange
        val id = 1L
        val request = StudentRequest(name = "Jane Doe", email = "jane@example.com")
        Mockito.`when`(studentRepository.findById(id)).thenReturn(Optional.empty())

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
        Mockito.`when`(studentRepository.findById(id)).thenReturn(Optional.of(student))

        // Act
        studentService.delete(id)

        // Assert
        Mockito.verify(studentRepository).findById(id)
        Mockito.verify(studentRepository).delete(student)
    }

    @Test
    fun `delete should throw StudentNotFoundException when student does not exist`() {
        // Arrange
        val id = 1L
        Mockito.`when`(studentRepository.findById(id)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<StudentNotFoundException> {
            studentService.delete(id)
        }
    }
}