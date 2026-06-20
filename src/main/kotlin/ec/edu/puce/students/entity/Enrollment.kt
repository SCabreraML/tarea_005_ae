package ec.edu.puce.students.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "enrollments")
class Enrollment(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var status: String = "INSCRITO",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    var student: Student,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    var subject: Subject
)