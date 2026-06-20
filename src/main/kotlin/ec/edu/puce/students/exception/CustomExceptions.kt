package ec.edu.puce.students.exception

class StudentNotFoundException(id: Long) : RuntimeException("Student not found with id: $id")
class ProfessorNotFoundException(id: Long) : RuntimeException("Professor not found with id: $id")
class SubjectNotFoundException(id: Long) : RuntimeException("Subject not found with id: $id")
class EnrollmentNotFoundException(id: Long) : RuntimeException("Enrollment not found with id: $id")