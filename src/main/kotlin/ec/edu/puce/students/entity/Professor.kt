package ec.edu.puce.students.entity

import jakarta.persistence.*

@Entity
@Table(name = "professors")
class Professor(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var email: String,

    // Agregamos explícitamente el targetEntity para que Hibernate no tenga dudas del tipo
    @OneToMany(
        mappedBy = "professor",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY,
        targetEntity = Subject::class
    )
    var subjects: List<Subject> = mutableListOf()
)