package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.dto.*

import ar.edu.unsam.algo3.service.UsuarioService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["http://localhost:4200", "http://localhost:5173"])
@RestController
@RequestMapping("/usuarios")
class UsuarioController(val usuarioService: UsuarioService) {

    @GetMapping("/{id}")
    fun getUsuarioById(@PathVariable id: Int) = usuarioService.getByID(id).resumenDatosDTO()

    @GetMapping("/busqueda")
    fun buscarUsuarios(@RequestParam busqueda: String?): List<DatosAmigosDTO> {
        val todosUsuariosCard = this.usuarioService.search(busqueda)
        return todosUsuariosCard.map { user -> user.resumenAmigosDTO() }.toList()
    }

    @PostMapping("/login")
    fun login(@RequestBody usuarioLogin: UsuarioLoginDTO): ResponseEntity<UsuarioIDDTO> {
        val idDTO = usuarioService.validarUsuarioLogin(usuarioLogin)
        return  ResponseEntity.ok(idDTO)  // Devuelve la respuesta con el ID del usuario
    }

    @PutMapping("/actualizar/{id}")
    fun actualizarDatosUsuario(@RequestBody usuarioNuevosDatosDTO: UsuarioActualizarDTO) {
        usuarioService.actualizarDatosUsuario(usuarioNuevosDatosDTO)
    }

    @GetMapping("/amigos/{id}")
    fun getAmigos(@PathVariable id: Int): List<DatosAmigosDTO> {
        val usuarioLogueado = usuarioService.getByID(id)
        val amigos = usuarioLogueado.amigos
        return amigos.map { amigo -> amigo.resumenAmigosDTO() }.toList()
    }

    @PatchMapping("/{userId}/agregar-amigo")
    fun agregarAmigo(@PathVariable userId: Int, @RequestBody nuevosAmigos: List<DatosAmigosDTO>) : ResponseEntity<Any> {
        usuarioService.agregarAmigos(nuevosAmigos,userId )
       return ResponseEntity.ok("Amigos agregados exitosamente.")
    }

    @PatchMapping("{userId}/eliminar-amigo")
    fun eliminarAmigo(@PathVariable userId: Int, @RequestBody amigoAEliminar: Int) {
        usuarioService.eliminarAmigos(amigoAEliminar, userId)
    }

    @GetMapping("/recomendaciones-a-valorar/{userId}")
    fun getRecomendacionesAValorar(@PathVariable userId: Int): List<ResumenRecomendacionCompletaDTO> {
        return usuarioService.getRecomendacionesAValorar(userId)
    }

    @PatchMapping("{userId}/agregar-recomendacion-a-valorar/{recomendacionId}")
    fun agregarRecomendacionAValorar(@PathVariable userId: Int, @PathVariable recomendacionId: Int) {
        usuarioService.agregarRecomendacionAValorar(userId, recomendacionId)
    }

    @DeleteMapping("{userId}/eliminar-recomendacion-a-valorar/{recomendacionId}")
    fun eliminarRecomendacionAValorar(@PathVariable userId: Int, @PathVariable recomendacionId: Int) {
        usuarioService.eliminarRecomendacionAValorar(userId, recomendacionId)
    }

    @GetMapping("/{userId}/recomendacion-en-a-valorar/{recomendacionId}")
    fun estaEnRecomendacionesAValorar(@PathVariable userId: Int, @PathVariable recomendacionId: Int): Boolean {
        return usuarioService.estaEnRecomendacionesAValorar(userId, recomendacionId)
    }

    /* START PERFIL LIBROS A LEER Y LEÍDOS */
    @GetMapping("/{userId}/libros-a-leer")
    fun getLibrosALeer(@PathVariable userId: Int): List<resumenCardLibroDTO> {
        return usuarioService.getLibrosALeer(userId)
    }

    @GetMapping("/{userId}/libros-leidos")
    fun getLibrosLeidosMenosTodos(@PathVariable userId: Int): List<resumenCardLibroDTO> {
        return usuarioService.getLibrosLeidosMenosTodos(userId)
    }

    @PatchMapping("/{userId}/agregar-leidos")
    fun agregarLibrosLeidos(@PathVariable userId: Int, @RequestBody librosIds: List<Int>): List<resumenCardLibroDTO> {
        return usuarioService.agregarLibrosLeidos(userId, librosIds)
    }

    @PatchMapping("/{userId}/eliminar-leidos")
    fun eliminarLibrosLeidos(@PathVariable userId: Int, @RequestBody librosIds: List<Int>): List<resumenCardLibroDTO> {
        return usuarioService.eliminarLibrosLeidos(userId, librosIds)
    }

    @PatchMapping("/{userId}/agregar-a-leer")
    fun agregarLibrosALeer(@PathVariable userId: Int, @RequestBody librosIds: List<Int>): List<resumenCardLibroDTO> {
        return usuarioService.agregarLibrosALeer(userId, librosIds)
    }

    @PatchMapping("/{userId}/eliminar-a-leer")
    fun eliminarLibrosALeer(@PathVariable userId: Int, @RequestBody librosIds: List<Int>): List<resumenCardLibroDTO> {
        return usuarioService.eliminarLibrosALeer(userId, librosIds)
    }
    /* END PERFIL LIBROS A LEER Y LEÍDOS */

}




