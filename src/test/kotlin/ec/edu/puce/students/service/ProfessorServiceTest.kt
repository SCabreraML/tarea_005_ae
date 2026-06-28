package ec.edu.puce.students.service

import ec.edu.puce.students.dto.ProfessorRequest
import ec.edu.puce.students.entity.Professor
import ec.edu.puce.students.exception.ProfessorNotFoundException
import ec.edu.puce.students.repository.ProfessorRepository
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
class ProfessorServiceTest {

    @Mock
    private lateinit var professorRepository: ProfessorRepository

    @InjectMocks
    private lateinit var professorService: ProfessorService

    @Test
    fun `create should return ProfessorResponse when request is valid`() {
        // Arrange
        val request = ProfessorRequest(name = "Dr. Smith", email = "smith@example.com")
        val professor = Professor(id = 1L, name = "Dr. Smith", email = "smith@example.com")
        `when`(professorRepository.save(any(Professor::class.java))).thenReturn(professor)

        // Act
        val response = professorService.create(request)

        // Assert
        assertEquals(professor.id, response.id)
        assertEquals(professor.name, response.name)
        assertEquals(professor.email, response.email)
        verify(professorRepository).save(any(Professor::class.java))
    }

    @Test
    fun `create should throw IllegalArgumentException when name is blank`() {
        // Arrange
        val request = ProfessorRequest(name = "", email = "smith@example.com")

        // Act & Assert
        val exception = assertThrows<IllegalArgumentException> {
            professorService.create(request)
        }
        assertEquals("Professor name cannot be blank", exception.message)
    }

    @Test
    fun `findAll should return list of ProfessorResponse`() {
        // Arrange
        val professor = Professor(id = 1L, name = "Dr. Smith", email = "smith@example.com")
        `when`(professorRepository.findAll()).thenReturn(listOf(professor))

        // Act
        val responseList = professorService.findAll()

        // Assert
        assertEquals(1, responseList.size)
        assertEquals(professor.id, responseList[0].id)
        verify(professorRepository).findAll()
    }

    @Test
    fun `findById should return ProfessorResponse when professor exists`() {
        // Arrange
        val id = 1L
        val professor = Professor(id = id, name = "Dr. Smith", email = "smith@example.com")
        `when`(professorRepository.findById(id)).thenReturn(Optional.of(professor))

        // Act
        val response = professorService.findById(id)

        // Assert
        assertEquals(id, response.id)
        verify(professorRepository).findById(id)
    }

    @Test
    fun `findById should throw ProfessorNotFoundException when professor does not exist`() {
        // Arrange
        val id = 1L
        `when`(professorRepository.findById(id)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<ProfessorNotFoundException> {
            professorService.findById(id)
        }
    }

    @Test
    fun `update should return ProfessorResponse when request is valid and professor exists`() {
        // Arrange
        val id = 1L
        val request = ProfessorRequest(name = "Dr. Jane Smith", email = "jane.smith@example.com")
        val existingProfessor = Professor(id = id, name = "Dr. Smith", email = "smith@example.com")
        val updatedProfessor = Professor(id = id, name = "Dr. Jane Smith", email = "jane.smith@example.com")

        `when`(professorRepository.findById(id)).thenReturn(Optional.of(existingProfessor))
        `when`(professorRepository.save(any(Professor::class.java))).thenReturn(updatedProfessor)

        // Act
        val response = professorService.update(id, request)

        // Assert
        assertEquals(id, response.id)
        assertEquals(request.name, response.name)
        assertEquals(request.email, response.email)
        verify(professorRepository).findById(id)
        verify(professorRepository).save(any(Professor::class.java))
    }

    @Test
    fun `update should throw IllegalArgumentException when name is blank`() {
        // Arrange
        val id = 1L
        val request = ProfessorRequest(name = " ", email = "jane@example.com")

        // Act & Assert
        val exception = assertThrows<IllegalArgumentException> {
            professorService.update(id, request)
        }
        assertEquals("Professor name cannot be blank", exception.message)
    }

    @Test
    fun `update should throw ProfessorNotFoundException when professor does not exist`() {
        // Arrange
        val id = 1L
        val request = ProfessorRequest(name = "Dr. Smith", email = "smith@example.com")
        `when`(professorRepository.findById(id)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<ProfessorNotFoundException> {
            professorService.update(id, request)
        }
    }

    @Test
    fun `delete should call delete on repository when professor exists`() {
        // Arrange
        val id = 1L
        val professor = Professor(id = id, name = "Dr. Smith", email = "smith@example.com")
        `when`(professorRepository.findById(id)).thenReturn(Optional.of(professor))

        // Act
        professorService.delete(id)

        // Assert
        verify(professorRepository).findById(id)
        verify(professorRepository).delete(professor)
    }

    @Test
    fun `delete should throw ProfessorNotFoundException when professor does not exist`() {
        // Arrange
        val id = 1L
        `when`(professorRepository.findById(id)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<ProfessorNotFoundException> {
            professorService.delete(id)
        }
    }
}
