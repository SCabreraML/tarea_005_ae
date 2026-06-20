package ec.edu.puce.students.controller

import ec.edu.puce.students.dto.EnrollmentRequest
import ec.edu.puce.students.dto.EnrollmentResponse
import ec.edu.puce.students.dto.StatusUpdateRequest
import ec.edu.puce.students.service.EnrollmentService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/enrollments")
class EnrollmentController(private val enrollmentService: EnrollmentService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: EnrollmentRequest): EnrollmentResponse = enrollmentService.create(request)

    @GetMapping
    fun findAll(): List<EnrollmentResponse> = enrollmentService.findAll()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): EnrollmentResponse = enrollmentService.findById(id)

    @PutMapping("/{id}")
    fun updateStatus(@PathVariable id: Long, @RequestBody request: StatusUpdateRequest): EnrollmentResponse = enrollmentService.updateStatus(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) = enrollmentService.delete(id)
}