package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.domain.Lenguaje
import ar.edu.unsam.algo3.dto.*
import ar.edu.unsam.algo3.service.LibroService
import ar.edu.unsam.algo3.service.UsuarioService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@CrossOrigin(origins = ["http://localhost:4200", "http://localhost:5173"])
@RestController
@RequestMapping("/libros")
class LibroController (val libroService: LibroService, val usuarioService: UsuarioService) {

    @GetMapping("/{id}")
    fun getLibroById(@PathVariable id: Int) = libroService.getByID(id).resumenDTO()

    @GetMapping("/completo/{id}")
    fun getLibroCompletoById(@PathVariable id: Int) = libroService.getByID(id).resumenCompletoDTO()

    @GetMapping("/completo/autorcompleto/{id}")
    fun getLibroConAutorCompletoById(@PathVariable id: Int) = libroService.getByID(id).resumenConAutorCompletoDTO()

    @GetMapping("/busqueda")
    fun buscarLibros(@RequestParam busqueda: String?) : List<LibroResumenCompletoDTO> {
        val libros = this.libroService.search(busqueda)
        return libros.map { libro -> libro.resumenCompletoDTO() }.toList()
    }


    @GetMapping("/busqueda/autorcompleto")
    fun buscarLibrosConAutor(@RequestParam busqueda: String?) : List<LibroConAutorCompletoDTO> {
        val libros = this.libroService.search(busqueda)
        return libros.map { libro -> libro.resumenConAutorCompletoDTO() }.toList()
    }


    @GetMapping("/todos")
    fun obtenerTodosLosLibros(): List<resumenCardLibroDTO> {
        return libroService.obtenerTodosLosLibros()
    }


    @GetMapping("/idiomas/disponibles")
    fun obtenerIdiomasDisponibles(): List<String> {
        return Lenguaje.entries.map { it.name }
    }

    @PostMapping("/nuevo")
    fun nuevoLibro(@RequestBody libroACrear: nuevoLibroDTO) : ResponseEntity<Void> {
        libroService.nuevoLibro(libroACrear)
        return ResponseEntity.noContent().build()
    }

    @PutMapping("/actualizar/{id}")
    fun actualizarDatosUsuario(@RequestBody libroUpdate: LibroConAutorCompletoDTO) : ResponseEntity<Void>  {
        libroService.actualizarLibro(libroUpdate)
        return ResponseEntity.noContent().build()
    }





}