package ec.edu.puce.students.service

import ec.edu.puce.students.dto.SubjectRequest
import ec.edu.puce.students.entity.Professor
import ec.edu.puce.students.entity.Subject
import ec.edu.puce.students.exception.ProfessorNotFoundException
import ec.edu.puce.students.exception.SubjectNotFoundException
import ec.edu.puce.students.repository.ProfessorRepository
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
import java.util.Optional
import kotlin.code
import kotlin.collections.get

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

        Mockito.`when`(professorRepository.findById(professorId)).thenReturn(Optional.of(professor))
        Mockito.`when`(subjectRepository.save(ArgumentMatchers.any(Subject::class.java))).thenReturn(subject)

        // Act
        val response = subjectService.create(request)

        // Assert
        Assertions.assertEquals(subject.id, response.id)
        Assertions.assertEquals(subject.name, response.name)
        Assertions.assertEquals(subject.code, response.code)
        Assertions.assertEquals(professor.id, response.professor.id)
        Mockito.verify(professorRepository).findById(professorId)
        Mockito.verify(subjectRepository).save(ArgumentMatchers.any(Subject::class.java))
    }

    @Test
    fun `create should throw IllegalArgumentException when name is blank`() {
        // Arrange
        val request = SubjectRequest(name = "", code = "MATH101", professorId = 1L)

        // Act & Assert
        val exception = assertThrows<IllegalArgumentException> {
            subjectService.create(request)
        }
        Assertions.assertEquals("Subject name cannot be blank", exception.message)
    }

    @Test
    fun `create should throw IllegalArgumentException when code is blank`() {
        // Arrange
        val request = SubjectRequest(name = "Math", code = " ", professorId = 1L)

        // Act & Assert
        val exception = assertThrows<IllegalArgumentException> {
            subjectService.create(request)
        }
        Assertions.assertEquals("Subject code cannot be blank", exception.message)
    }

    @Test
    fun `create should throw ProfessorNotFoundException when professor does not exist`() {
        // Arrange
        val professorId = 1L
        val request = SubjectRequest(name = "Math", code = "MATH101", professorId = professorId)
        Mockito.`when`(professorRepository.findById(professorId)).thenReturn(Optional.empty())

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
        Mockito.`when`(subjectRepository.findAll()).thenReturn(listOf(subject))

        // Act
        val responseList = subjectService.findAll()

        // Assert
        Assertions.assertEquals(1, responseList.size)
        Assertions.assertEquals(subject.id, responseList[0].id)
        Mockito.verify(subjectRepository).findAll()
    }

    @Test
    fun `findById should return SubjectResponse when subject exists`() {
        // Arrange
        val id = 1L
        val professor = Professor(id = 1L, name = "Dr. Smith", email = "smith@example.com")
        val subject = Subject(id = id, name = "Math", code = "MATH101", professor = professor)
        Mockito.`when`(subjectRepository.findById(id)).thenReturn(Optional.of(subject))

        // Act
        val response = subjectService.findById(id)

        // Assert
        Assertions.assertEquals(id, response.id)
        Mockito.verify(subjectRepository).findById(id)
    }

    @Test
    fun `findById should throw SubjectNotFoundException when subject does not exist`() {
        // Arrange
        val id = 1L
        Mockito.`when`(subjectRepository.findById(id)).thenReturn(Optional.empty())

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

        Mockito.`when`(subjectRepository.findById(id)).thenReturn(Optional.of(existingSubject))
        Mockito.`when`(professorRepository.findById(professorId)).thenReturn(Optional.of(newProfessor))
        Mockito.`when`(subjectRepository.save(ArgumentMatchers.any(Subject::class.java))).thenReturn(updatedSubject)

        // Act
        val response = subjectService.update(id, request)

        // Assert
        Assertions.assertEquals(id, response.id)
        Assertions.assertEquals(request.name, response.name)
        Assertions.assertEquals(request.code, response.code)
        Assertions.assertEquals(professorId, response.professor.id)
        Mockito.verify(subjectRepository).findById(id)
        Mockito.verify(professorRepository).findById(professorId)
        Mockito.verify(subjectRepository).save(ArgumentMatchers.any(Subject::class.java))
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
        Assertions.assertEquals("Subject name cannot be blank", exception.message)
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
        Assertions.assertEquals("Subject code cannot be blank", exception.message)
    }

    @Test
    fun `update should throw SubjectNotFoundException when subject does not exist`() {
        // Arrange
        val id = 1L
        val request = SubjectRequest(name = "Physics", code = "PHYS101", professorId = 2L)
        Mockito.`when`(subjectRepository.findById(id)).thenReturn(Optional.empty())

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

        Mockito.`when`(subjectRepository.findById(id)).thenReturn(Optional.of(existingSubject))
        Mockito.`when`(professorRepository.findById(professorId)).thenReturn(Optional.empty())

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
        Mockito.`when`(subjectRepository.findById(id)).thenReturn(Optional.of(subject))

        // Act
        subjectService.delete(id)

        // Assert
        Mockito.verify(subjectRepository).findById(id)
        Mockito.verify(subjectRepository).delete(subject)
    }

    @Test
    fun `delete should throw SubjectNotFoundException when subject does not exist`() {
        // Arrange
        val id = 1L
        Mockito.`when`(subjectRepository.findById(id)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<SubjectNotFoundException> {
            subjectService.delete(id)
        }
    }
}