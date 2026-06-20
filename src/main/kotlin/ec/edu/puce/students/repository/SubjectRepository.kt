package ec.edu.puce.students.repository

import ec.edu.puce.students.entity.Subject
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SubjectRepository : JpaRepository<Subject, Long>