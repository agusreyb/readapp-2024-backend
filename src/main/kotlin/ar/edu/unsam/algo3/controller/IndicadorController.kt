package ar.edu.unsam.algo3.controller
import ar.edu.unsam.algo3.service.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@CrossOrigin(origins = ["http://localhost:5173"])
@RestController
@RequestMapping("/indicadores")
class IndicadorController(
    private val usuarioService: UsuarioService,
    private val recomendacionService: RecomendacionService,
    private val libroService: LibroService,
    private val centroService: CentroService,
    private val procesosService: ProcesosService
) {

    @GetMapping("/totales")
    fun obtenerIndicadores(): Map<String, Int>  {
        val totalUsuarios = usuarioService.obtenerTotalUsuarios()
        val totalLibros = libroService.obtenerTotalLibros()
        val totalRecomendaciones = recomendacionService.obtenerTotalRecomendaciones()
        val totalCentros = centroService.obtenerTotalCentros()

         val resultado = mapOf(
             "Recomendaciones" to totalRecomendaciones,
             "Libros en sistema" to totalLibros,
             "Usuarios totales" to totalUsuarios,
             "Centros de distribución" to totalCentros
         )
        return resultado
    }

    @DeleteMapping("/usuarios/inactivos")
    fun borrarUsuariosInactivos(): ResponseEntity<Void> {
        procesosService.borrarUsuariosInactivos()
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/centros/inactivos")
    fun borrarCentrosInactivos(): ResponseEntity<Void> {
        procesosService.borrarCentrosInactivos()
        return ResponseEntity.noContent().build()
    }


}