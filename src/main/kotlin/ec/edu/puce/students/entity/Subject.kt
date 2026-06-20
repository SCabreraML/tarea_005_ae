package ec.edu.puce.students.entity

import jakarta.persistence.*

@Entity
@Table(name = "subjects")
class Subject(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false, unique = true)
    var code: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id", nullable = false)
    var professor: Professor
)