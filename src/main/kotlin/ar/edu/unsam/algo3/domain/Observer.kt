package ar.edu.unsam.algo3.domain

interface Observer {
    fun libroAgregado(usuario: Usuario, libro: Libro, recomendacion: Recomendacion){}
}

class NotificacionACreador(private val mailSender: MailSender) : Observer {
    override fun libroAgregado(usuario: Usuario, libro: Libro, recomendacion: Recomendacion) {
        if(usuario != recomendacion.creador){
           mailSender.mandarMail(
               Mail(
               "notificaciones@readapp.com.ar",
               recomendacion.creador.mail,
               "Se agregó un Libro",
               "El usuario '${usuario.username}' agrego el Libro '${libro.titulo}' a la recomendación " +
                       "que tenía estos Títulos: ${(recomendacion.libros.map{ it.titulo }).joinToString()}"
           )
           )
        }
    }
}

class RegistroLibrosAgregados : Observer {
   var aportesDeUsuarios = mutableMapOf<Usuario, Int>()
    override fun libroAgregado(usuario: Usuario, libro: Libro, recomendacion: Recomendacion) {
        aportesDeUsuarios[usuario]=aportesDeUsuarios.getOrDefault(usuario, 0) + 1
    }
}

class LimiteLibrosAgregados(private val limite: Int, private val usuarioConLimite: Usuario) : Observer {
    private var contador: Int = 0
    override fun libroAgregado(usuario: Usuario, libro: Libro, recomendacion: Recomendacion) {
        if(usuario != recomendacion.creador){
            if(usuarioConLimite == usuario){
                contador += 1
                if(contador == limite)
                    recomendacion.creador.amigos.remove(usuario)
            }
        }
    }
}

class ValoracionInstantanea: Observer {
    override fun libroAgregado(usuario: Usuario, libro: Libro, recomendacion: Recomendacion) {
        if ((usuario != recomendacion.creador) and (!recomendacion.valoraciones.any{it.creador == usuario}) ) {
            recomendacion.valorar(usuario,5.0, "Excelente 100% recomendable")
        //usuario.valorarRecomendacion(recomendacion, 5.0, "Excelente 100% recomendable")
        }
    }
}

