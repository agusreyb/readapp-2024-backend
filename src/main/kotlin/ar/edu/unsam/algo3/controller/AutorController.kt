package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.dto.AutorResumenDTO
import ar.edu.unsam.algo3.dto.toResumenDTO
import ar.edu.unsam.algo3.service.AutorService
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["http://localhost:4200", "http://localhost:5173"])
@RestController
@RequestMapping("/autores")
class AutorController(val autorService: AutorService) {

    @GetMapping("/{id}")
    fun getAutorById(@PathVariable id: Int) = autorService.getByID(id).toResumenDTO()

    @GetMapping("/busqueda")
    fun buscarAutores(@RequestParam busqueda: String?): List<AutorResumenDTO> {
        val autoresEncontrados = this.autorService.search(busqueda)
        return autoresEncontrados.map { autor -> autor.toResumenDTO() }.toList()
    }

    @PutMapping("/modificar/{id}")
    fun modificarDatosAutor(@RequestBody autorAModificar: AutorResumenDTO) {
        autorService.modificarDatosAutor(autorAModificar)
    }

    @DeleteMapping("/eliminar-autor/{id}")
    fun eliminarAutor(@PathVariable id: Int) {
        autorService.eliminarAutor(id)
    }
}