package ar.edu.unsam.algo3.controller

import ar.edu.unsam.algo3.domain.Recomendacion
import ar.edu.unsam.algo3.dto.RecomendacionUpdateDTO
import ar.edu.unsam.algo3.dto.ResumenRecomendacionCompletaDTO
import ar.edu.unsam.algo3.dto.ValoracionNueva
import ar.edu.unsam.algo3.dto.resumenRecomendacionCompletaDTO
import ar.edu.unsam.algo3.service.RecomendacionService
import org.springframework.web.bind.annotation.*

@CrossOrigin(origins = ["http://localhost:4200", "http://localhost:5173"])
@RestController
@RequestMapping("/recomendaciones")
class RecomendacionController(val recomendacionService: RecomendacionService) {

    @GetMapping("/{idReco}")
    fun getRecomendacionById( @PathVariable idReco: Int, @RequestParam idUser: Int? = null): ResumenRecomendacionCompletaDTO {
        return if (idUser == null) {
            recomendacionService.getByID(idReco).resumenRecomendacionCompletaDTO()
        } else {
            val recomendacionEncontrada = recomendacionService.getByID(idReco)
            val usuario = recomendacionService.getUsuarioByID(idUser)
            recomendacionService.modificarRepoEditarValorar(recomendacionEncontrada, usuario)
        }
    }

    @GetMapping("/busqueda/{usuarioID}")
    fun buscarRecomendaciones(@PathVariable usuarioID: Int, @RequestParam busqueda: String?) : List<ResumenRecomendacionCompletaDTO> {
        val recomendaciones = recomendacionService.search(busqueda, usuarioID, "todas")
        return listaToDTO(recomendaciones, usuarioID)
    }

    fun listaToDTO(recomendaciones: List<Recomendacion>, usuarioID: Int): List<ResumenRecomendacionCompletaDTO> {
        val usuario = recomendacionService.getUsuarioByID(usuarioID)
        return recomendaciones.map { recomendacion -> recomendacionService.modificarRepoEditarValorar(recomendacion, usuario)}.toList()
    }

    @GetMapping("/permiso/editar/usuario/{usuarioID}")
    fun recomendacionesEditables(@PathVariable usuarioID: Int, @RequestParam busqueda : String?): List<ResumenRecomendacionCompletaDTO> {
        val recomendaciones = recomendacionService.search(busqueda, usuarioID, null)
        return listaToDTO(recomendaciones, usuarioID)
    }

    @PatchMapping("/editar/por/{usuarioID}")
    fun editarRecomendacion(@PathVariable usuarioID : Int, @RequestBody recomendacionUpdateDTO: RecomendacionUpdateDTO) {
        recomendacionService.editarRecomendacionService(recomendacionUpdateDTO, usuarioID)
    }

    @GetMapping("{recomendacionID}/puede/valorar/usuario/{usuarioID}")
    fun puedeValorarUsuario(@PathVariable recomendacionID: Int, @PathVariable usuarioID: Int) : Boolean {
        return recomendacionService.puedeValorarUsuario(recomendacionID, usuarioID)
    }

    @DeleteMapping("/eliminar-recomendacion/{recomendacionId}")
    fun eliminarRecomendacion(@PathVariable recomendacionId: Int) {
        val recomendacionAEliminar = recomendacionService.getByID(recomendacionId)
        recomendacionService.eliminarRecomendacion(recomendacionAEliminar)
    }

    @PostMapping("/{recomendacionId}/agregar/valoracion")
    fun agregarValoracion(@PathVariable recomendacionId: Int, @RequestBody valoracionNueva: ValoracionNueva) {
        recomendacionService.agregarValoracionGetRecoyUser(recomendacionId, valoracionNueva)
    }

    @PostMapping("/crear")
    fun crearRecomendacion(@RequestBody recomendacionACrear: RecomendacionUpdateDTO) {
        recomendacionService.crearRecomendacion(recomendacionACrear)
    }
}