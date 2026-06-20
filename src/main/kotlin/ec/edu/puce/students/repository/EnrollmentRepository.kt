package ec.edu.puce.students.repository

import ec.edu.puce.students.entity.Enrollment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EnrollmentRepository : JpaRepository<Enrollment, Long>