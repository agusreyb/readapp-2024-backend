package ar.edu.unsam.algo3.domain

import ar.edu.unsam.algo3.repositorio.Repositorio

class Administrador {
    fun run(procesos: List<ProcesoDeAdmin>) {
        procesos.forEach { proceso -> proceso.ejecutar() }
    }
}

abstract class ProcesoDeAdmin (private val mailSender: MailSender) {
    private var mail = Mail(
        "notificaciones@readapp.com.ar",
        "admin@readapp.com.ar",
        "Notificacion para administrador",
        "Se realizo el proceso: ${this::class.simpleName}")
    private fun mandarMail(){
        mailSender.mandarMail(mail)
    }
    fun ejecutar(){
        primerEjecucion()
        mandarMail()
    }
    abstract fun primerEjecucion()
}

class BorraUsuariosInactivos(private val repositorioUsuarios: Repositorio<Usuario>, private val repositorioRecomendaciones: Repositorio<Recomendacion>, private val mailSender: MailSender) : ProcesoDeAdmin(mailSender) {

    override fun primerEjecucion() {
        listaInactivos().forEach{usuario -> repositorioUsuarios.delete(usuario)}
    }
    fun listaInactivos() : List<Usuario> {
        return repositorioUsuarios.elementos.filter { usuario -> estaInactivo(usuario) }
    }
    private fun estaInactivo(usuario: Usuario): Boolean {
        return noGeneroRecomendacion(usuario) && noEvaluoRecomendacion(usuario) && noConsideradoAmigo(usuario)
    }
    private fun noGeneroRecomendacion(usuario: Usuario): Boolean {
        return usuario.misRecomendaciones.size == 0
    }
    private fun noEvaluoRecomendacion(usuario: Usuario): Boolean {
        return repositorioRecomendaciones.elementos.all {recomendacion -> recomendacion.encuentraValoracionUsuario(usuario) == -1 }
    }
    private fun noConsideradoAmigo(usuario: Usuario): Boolean {
        return !repositorioUsuarios.elementos.any { otroUsuario -> otroUsuario.amigos.contains(usuario)}
    }
}

class ActualizarLibros(private val repositorioLibros: Repositorio<Libro>, private val serviceLibros: ServiceLibros, private val mailSender: MailSender) : ProcesoDeAdmin(mailSender) {
    override fun primerEjecucion() {
        ActualizacionLibros(repositorioLibros , serviceLibros).actualizarRepositorio()
    }
}

class BorraCentrosExpirados(private val repositorioCentros: Repositorio<CentroDeLectura>, private val mailSender: MailSender) : ProcesoDeAdmin(mailSender) { //private val mailSender: MailSender
    override fun primerEjecucion() {
        listaCentrosInactivos().forEach{centro -> repositorioCentros.delete(centro)}
    }
    fun listaCentrosInactivos(): List<CentroDeLectura> {
        return repositorioCentros.elementos.filterNot{centro -> centro.estaDisponibleEncuentro()}
    }
}

class AgregaAutores(private val repositorioAutores: Repositorio<Autor>, private val listaAutores: List<Autor>, private val mailSender: MailSender) : ProcesoDeAdmin(mailSender) {
    override fun primerEjecucion(){
        listaAutores.forEach { autor -> repositorioAutores.create(autor)}
    }
}
