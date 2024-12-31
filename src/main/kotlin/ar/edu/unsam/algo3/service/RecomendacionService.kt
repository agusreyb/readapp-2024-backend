package ar.edu.unsam.algo3.service

import ar.edu.unsam.algo3.domain.Libro
import ar.edu.unsam.algo3.domain.Recomendacion
import ar.edu.unsam.algo3.domain.Usuario
import ar.edu.unsam.algo3.dto.RecomendacionUpdateDTO
import ar.edu.unsam.algo3.dto.ResumenRecomendacionCompletaDTO
import ar.edu.unsam.algo3.dto.ValoracionNueva
import ar.edu.unsam.algo3.dto.resumenRecomendacionCompletaDTO
import ar.edu.unsam.algo3.repositorio.LibrosRepositorio
import ar.edu.unsam.algo3.repositorio.RecomendacionesRepositorio
import ar.edu.unsam.algo3.repositorio.UsuariosRepositorio
import org.springframework.stereotype.Service


@Service
class RecomendacionService (val recomendacionesRepositorio: RecomendacionesRepositorio, val usuariosRepositorio: UsuariosRepositorio, val librosRepositorio: LibrosRepositorio) {

    fun getByID(id: Int): Recomendacion = recomendacionesRepositorio.getByID(id)

    fun search(busqueda: String?, usuarioID: Int, filtro: String?): List<Recomendacion> {
        var recomendaciones = recomendacionesRepositorio.search(busqueda)
        val usuario = getUsuarioByID(usuarioID)
        recomendaciones = if(filtro != null) {
            recomendaciones.filter{recomendacion -> usuario.recomendacionEsApta(recomendacion) }
        } else {
            recomendaciones.filter { recomendacion -> recomendacion.usuarioPuedeEditar(usuario) }
        }
        return recomendaciones
    }

    fun getUsuarioByID(idUsuario: Int): Usuario = usuariosRepositorio.getByID(idUsuario)

    fun modificarRepoEditarValorar(recomendacion: Recomendacion, usuario: Usuario): ResumenRecomendacionCompletaDTO {
        val recoAModificar = recomendacion.resumenRecomendacionCompletaDTO()
        recoAModificar.puedeEditar  = recomendacion.usuarioPuedeEditar(usuario)
        recoAModificar.puedeValorar = recomendacion.usuarioPuedeValorar(usuario)
        return recoAModificar
    }

    fun editarRecomendacionService(recomendacionUpdateDTORecibida: RecomendacionUpdateDTO, usuarioId : Int) {
        val recomendacionRealEncontrada = recomendacionesRepositorio.getByID(recomendacionUpdateDTORecibida.id.toInt())
        recomendacionRealEncontrada.validacionPermisoEdicion(getUsuarioByID(usuarioId))
        recomendacionRealEncontrada.titulo = recomendacionUpdateDTORecibida.titulo
        recomendacionRealEncontrada.esPublica = recomendacionUpdateDTORecibida.esPublica
            val listaLibrosRecibida = recomendacionUpdateDTORecibida.lista_libros.map{ libro -> librosRepositorio.getByID(libro.toInt()) }
        recomendacionRealEncontrada.libros = listaLibrosRecibida.toMutableSet()
        recomendacionRealEncontrada.descripcion = recomendacionUpdateDTORecibida.descripcion
    }

    fun eliminarRecomendacion(recomendacion: Recomendacion) = recomendacionesRepositorio.delete(recomendacion)

    fun puedeValorarUsuario(recomendacionID : Int, usuarioID : Int): Boolean {
        val recomendacion = recomendacionesRepositorio.getByID(recomendacionID)
        return recomendacion.usuarioPuedeValorar(getUsuarioByID(usuarioID))
    }

    fun agregarValoracionGetRecoyUser(recomendacionId : Int, valoracionNueva: ValoracionNueva) {
        val recomendacionAValorar = recomendacionesRepositorio.getByID(recomendacionId)
        val usuario = getUsuarioByID(valoracionNueva.creadorId)
        recomendacionAValorar.valorar(usuario,valoracionNueva.valor,valoracionNueva.comentario)
    }

    fun crearRecomendacion(recomendacionACrear : RecomendacionUpdateDTO) {
        val listaLibrosACrear : List<Libro> = recomendacionACrear.lista_libros.map{ libro ->  librosRepositorio.getByID(libro.toInt())}
        val usuarioEncontrado = getUsuarioByID(recomendacionACrear.creadorId.toInt())
        recomendacionesRepositorio.create(Recomendacion(
            recomendacionACrear.titulo,
            usuarioEncontrado,
            recomendacionACrear.descripcion,
            recomendacionACrear.esPublica
            ).apply { libros.addAll(listaLibrosACrear) }
        )
    }

    fun obtenerTotalRecomendaciones(): Int {
        return recomendacionesRepositorio.contar()
    }
}