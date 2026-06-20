package ec.edu.puce.students.controller

import ec.edu.puce.students.dto.SubjectRequest
import ec.edu.puce.students.dto.SubjectResponse
import ec.edu.puce.students.service.SubjectService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/subjects")
class SubjectController(private val subjectService: SubjectService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: SubjectRequest): SubjectResponse = subjectService.create(request)

    @GetMapping
    fun findAll(): List<SubjectResponse> = subjectService.findAll()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): SubjectResponse = subjectService.findById(id)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody request: SubjectRequest): SubjectResponse = subjectService.update(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) = subjectService.delete(id)
}