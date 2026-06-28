package ec.edu.puce.students.service

import ec.edu.puce.students.dto.SubjectRequest
import ec.edu.puce.students.entity.Professor
import ec.edu.puce.students.entity.Subject
import ec.edu.puce.students.exception.ProfessorNotFoundException
import ec.edu.puce.students.exception.SubjectNotFoundException
import ec.edu.puce.students.repository.ProfessorRepository
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
import java.util.*

@ExtendWith(MockitoExtension::class)
class SubjectServiceTest {

    @Mock
    private lateinit var subjectRepository: SubjectRepository

    @Mock
    private lateinit var professorRepository: ProfessorRepository

    @InjectMocks
    private lateinit var subjectService: SubjectService

    @Test
    fun `create should return SubjectResponse when request is valid`() {
        // Arrange
        val professorId = 1L
        val request = SubjectRequest(name = "Math", code = "MATH101", professorId = professorId)
        val professor = Professor(id = professorId, name = "Dr. Smith", email = "smith@example.com")
        val subject = Subject(id = 1L, name = "Math", code = "MATH101", professor = professor)

        `when`(professorRepository.findById(professorId)).thenReturn(Optional.of(professor))
        `when`(subjectRepository.save(any(Subject::class.java))).thenReturn(subject)

        // Act
        val response = subjectService.create(request)

        // Assert
        assertEquals(subject.id, response.id)
        assertEquals(subject.name, response.name)
        assertEquals(subject.code, response.code)
        assertEquals(professor.id, response.professor.id)
        verify(professorRepository).findById(professorId)
        verify(subjectRepository).save(any(Subject::class.java))
    }

    @Test
    fun `create should throw IllegalArgumentException when name is blank`() {
        // Arrange
        val request = SubjectRequest(name = "", code = "MATH101", professorId = 1L)

        // Act & Assert
        val exception = assertThrows<IllegalArgumentException> {
            subjectService.create(request)
        }
        assertEquals("Subject name cannot be blank", exception.message)
    }

    @Test
    fun `create should throw IllegalArgumentException when code is blank`() {
        // Arrange
        val request = SubjectRequest(name = "Math", code = " ", professorId = 1L)

        // Act & Assert
        val exception = assertThrows<IllegalArgumentException> {
            subjectService.create(request)
        }
        assertEquals("Subject code cannot be blank", exception.message)
    }

    @Test
    fun `create should throw ProfessorNotFoundException when professor does not exist`() {
        // Arrange
        val professorId = 1L
        val request = SubjectRequest(name = "Math", code = "MATH101", professorId = professorId)
        `when`(professorRepository.findById(professorId)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<ProfessorNotFoundException> {
            subjectService.create(request)
        }
    }

    @Test
    fun `findAll should return list of SubjectResponse`() {
        // Arrange
        val professor = Professor(id = 1L, name = "Dr. Smith", email = "smith@example.com")
        val subject = Subject(id = 1L, name = "Math", code = "MATH101", professor = professor)
        `when`(subjectRepository.findAll()).thenReturn(listOf(subject))

        // Act
        val responseList = subjectService.findAll()

        // Assert
        assertEquals(1, responseList.size)
        assertEquals(subject.id, responseList[0].id)
        verify(subjectRepository).findAll()
    }

    @Test
    fun `findById should return SubjectResponse when subject exists`() {
        // Arrange
        val id = 1L
        val professor = Professor(id = 1L, name = "Dr. Smith", email = "smith@example.com")
        val subject = Subject(id = id, name = "Math", code = "MATH101", professor = professor)
        `when`(subjectRepository.findById(id)).thenReturn(Optional.of(subject))

        // Act
        val response = subjectService.findById(id)

        // Assert
        assertEquals(id, response.id)
        verify(subjectRepository).findById(id)
    }

    @Test
    fun `findById should throw SubjectNotFoundException when subject does not exist`() {
        // Arrange
        val id = 1L
        `when`(subjectRepository.findById(id)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<SubjectNotFoundException> {
            subjectService.findById(id)
        }
    }

    @Test
    fun `update should return SubjectResponse when request is valid and everything exists`() {
        // Arrange
        val id = 1L
        val professorId = 2L
        val request = SubjectRequest(name = "Physics", code = "PHYS101", professorId = professorId)

        val oldProfessor = Professor(id = 1L, name = "Dr. Smith", email = "smith@example.com")
        val newProfessor = Professor(id = professorId, name = "Dr. Jones", email = "jones@example.com")
        val existingSubject = Subject(id = id, name = "Math", code = "MATH101", professor = oldProfessor)
        val updatedSubject = Subject(id = id, name = "Physics", code = "PHYS101", professor = newProfessor)

        `when`(subjectRepository.findById(id)).thenReturn(Optional.of(existingSubject))
        `when`(professorRepository.findById(professorId)).thenReturn(Optional.of(newProfessor))
        `when`(subjectRepository.save(any(Subject::class.java))).thenReturn(updatedSubject)

        // Act
        val response = subjectService.update(id, request)

        // Assert
        assertEquals(id, response.id)
        assertEquals(request.name, response.name)
        assertEquals(request.code, response.code)
        assertEquals(professorId, response.professor.id)
        verify(subjectRepository).findById(id)
        verify(professorRepository).findById(professorId)
        verify(subjectRepository).save(any(Subject::class.java))
    }

    @Test
    fun `update should throw IllegalArgumentException when name is blank`() {
        // Arrange
        val id = 1L
        val request = SubjectRequest(name = "", code = "PHYS101", professorId = 2L)

        // Act & Assert
        val exception = assertThrows<IllegalArgumentException> {
            subjectService.update(id, request)
        }
        assertEquals("Subject name cannot be blank", exception.message)
    }

    @Test
    fun `update should throw IllegalArgumentException when code is blank`() {
        // Arrange
        val id = 1L
        val request = SubjectRequest(name = "Physics", code = " ", professorId = 2L)

        // Act & Assert
        val exception = assertThrows<IllegalArgumentException> {
            subjectService.update(id, request)
        }
        assertEquals("Subject code cannot be blank", exception.message)
    }

    @Test
    fun `update should throw SubjectNotFoundException when subject does not exist`() {
        // Arrange
        val id = 1L
        val request = SubjectRequest(name = "Physics", code = "PHYS101", professorId = 2L)
        `when`(subjectRepository.findById(id)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<SubjectNotFoundException> {
            subjectService.update(id, request)
        }
    }

    @Test
    fun `update should throw ProfessorNotFoundException when professor does not exist`() {
        // Arrange
        val id = 1L
        val professorId = 2L
        val request = SubjectRequest(name = "Physics", code = "PHYS101", professorId = professorId)
        val professor = Professor(id = 1L, name = "Dr. Smith", email = "smith@example.com")
        val existingSubject = Subject(id = id, name = "Math", code = "MATH101", professor = professor)

        `when`(subjectRepository.findById(id)).thenReturn(Optional.of(existingSubject))
        `when`(professorRepository.findById(professorId)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<ProfessorNotFoundException> {
            subjectService.update(id, request)
        }
    }

    @Test
    fun `delete should call delete on repository when subject exists`() {
        // Arrange
        val id = 1L
        val professor = Professor(id = 1L, name = "Dr. Smith", email = "smith@example.com")
        val subject = Subject(id = id, name = "Math", code = "MATH101", professor = professor)
        `when`(subjectRepository.findById(id)).thenReturn(Optional.of(subject))

        // Act
        subjectService.delete(id)

        // Assert
        verify(subjectRepository).findById(id)
        verify(subjectRepository).delete(subject)
    }

    @Test
    fun `delete should throw SubjectNotFoundException when subject does not exist`() {
        // Arrange
        val id = 1L
        `when`(subjectRepository.findById(id)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<SubjectNotFoundException> {
            subjectService.delete(id)
        }
    }
}
