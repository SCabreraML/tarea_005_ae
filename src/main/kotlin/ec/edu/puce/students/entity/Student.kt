package ec.edu.puce.students.entity

import jakarta.persistence.*

@Entity
@Table(name = "students")
class Student(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var email: String
)