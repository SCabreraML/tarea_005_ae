package ec.edu.puce.students.controller

import ec.edu.puce.students.dto.ProfessorRequest
import ec.edu.puce.students.dto.ProfessorResponse
import ec.edu.puce.students.service.ProfessorService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/professors")
class ProfessorController(private val professorService: ProfessorService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: ProfessorRequest): ProfessorResponse = professorService.create(request)

    @GetMapping
    fun findAll(): List<ProfessorResponse> = professorService.findAll()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ProfessorResponse = professorService.findById(id)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody request: ProfessorRequest): ProfessorResponse = professorService.update(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) = professorService.delete(id)
}