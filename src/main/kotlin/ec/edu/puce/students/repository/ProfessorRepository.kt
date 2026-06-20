package ec.edu.puce.students.repository

import ec.edu.puce.students.entity.Professor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProfessorRepository : JpaRepository<Professor, Long>