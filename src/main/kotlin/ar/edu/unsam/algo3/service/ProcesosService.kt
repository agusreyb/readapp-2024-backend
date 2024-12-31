package ar.edu.unsam.algo3.service

import ar.edu.unsam.algo2.readapp.BusinessException
import ar.edu.unsam.algo3.domain.*
import ar.edu.unsam.algo3.dto.CentroDeLecturaDTO
import ar.edu.unsam.algo3.dto.DatosAmigosDTO
import ar.edu.unsam.algo3.repositorio.RecomendacionesRepositorio
import ar.edu.unsam.algo3.repositorio.Repositorio
import ar.edu.unsam.algo3.repositorio.UsuariosRepositorio
import org.springframework.stereotype.Service

@Service
class ProcesosService (
    private val mailSender: MailSender,
    private val usuariosRepositorio: UsuariosRepositorio,
    private val recomendacionesRepositorio: RecomendacionesRepositorio,
    private val centrosRepositorio: Repositorio<CentroDeLectura>
){
    fun borrarUsuariosInactivos(){
        val procesoBorrarUsuarios = BorraUsuariosInactivos(usuariosRepositorio, recomendacionesRepositorio, mailSender)
        val usuariosInactivosEliminados = procesoBorrarUsuarios.listaInactivos()

        if (usuariosInactivosEliminados.isEmpty()) {
         throw BusinessException("No hay usuarios inactivos para eliminar")
        } else{
            val administracion = Administrador()
            administracion.run(listOf(procesoBorrarUsuarios))
        }
    }

    fun borrarCentrosInactivos() {
        val procesoBorrarCentros = BorraCentrosExpirados(centrosRepositorio, mailSender)
        val centrosExpiradosEliminados = procesoBorrarCentros.listaCentrosInactivos()

        if (centrosExpiradosEliminados.isEmpty()) {
            throw BusinessException("No hay centros inactivos para eliminar")
        } else {
            val administracion = Administrador()
            administracion.run(listOf(procesoBorrarCentros))
        }
    }
}