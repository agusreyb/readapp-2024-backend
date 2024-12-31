package ar.edu.unsam.algo3

import ar.edu.unsam.algo3.domain.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import io.mockk.verify

class ObserversTest: DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    val juan = Usuario("Juan", "Perez","JPerez", "JPerez@gmail.com", "",1996, 7, 13, Lenguaje.Español, 200.00, Leedor(), Promedio(), "")
    val maria = Usuario("Maria", "Perez", "MPerez", "MPerez@gmail.com","", 1990, 4, 22, Lenguaje.Español, 200.00,Leedor(), Promedio(), "")

    val borges = Autor("Jorge Luis", "Borges", "J.Borges", Lenguaje.Español, 54, 4)
    val walsh = Autor("Maria Elena", "Walsh","M.Walsh", Lenguaje.Español, 54, 4)

    val libro1 = Libro(
        "Libro 1!",
        borges,
        paginas = 100,
        palabras = 4000,
        cant_ediciones = 1,
        ventasSemanales = 15000,
        complejo = false,
        ""
    )
    val libro2 = Libro(
        "Libro 2!",
        walsh,
        paginas = 100,
        palabras = 4000,
        cant_ediciones = 3,
        ventasSemanales = 2000,
        complejo = false,
        ""
    )
    val libro3 = Libro(
        "Libro 3!",
        borges,
        paginas = 200,
        palabras = 30800,
        cant_ediciones = 2,
        ventasSemanales = 15000,
        complejo = false,
        ""
    )
    val recomendacion = juan.agregarRecomendacion(true)


    describe("dada una recomendacion"){
        val registroLibrosAgregados = RegistroLibrosAgregados()
        val valoracionInstantanea = ValoracionInstantanea()
        it("se pueden incorporar acciones") {
            recomendacion.agregarObserver(valoracionInstantanea)
            recomendacion.agregarObserver(registroLibrosAgregados)
            recomendacion.listaObservers.size shouldBe 2
        }
        it("se pueden quitar acciones"){
            recomendacion.agregarObserver(valoracionInstantanea)
            recomendacion.agregarObserver(registroLibrosAgregados)
            recomendacion.quitarObserver(valoracionInstantanea)
            recomendacion.listaObservers.size shouldBe 1
        }
    }


    describe("dada una recomendacion con Notificacion al creador cuando un usuario que no es el agrega un libro"){
        val mockedMailSender = mockk<MailSender>(relaxUnitFun = true)
        val notificacionACreador = NotificacionACreador(mockedMailSender)
        recomendacion.agregarObserver(notificacionACreador)
        juan.leerLibro(libro1)
        juan.leerLibro(libro2)
        juan.leerLibro(libro3)
        maria.leerLibro(libro1)
        maria.leerLibro(libro2)
        maria.leerLibro(libro3)
        juan.agregarAmigo(maria)
        it("Si juan agrega un libro a su propia recomendacion, el no deberia mandarse ningun mail"){
            juan.agregarLibroARecomendacion(libro1, recomendacion)
            verify(exactly = 0) {
                mockedMailSender.mandarMail(any())
            }
        }
        it("Si maria agrega un libro a la recomendacion de juan " +
                "deberia mandarse 1 mail con el contenido"){
            recomendacion.libros.add(libro2)
            recomendacion.libros.add(libro2) //HARDCODEADO
            recomendacion.libros.add(libro3) //HARDCODEADO
            maria.agregarLibroARecomendacion(libro1, recomendacion)
            verify(exactly = 1) {
                mockedMailSender.mandarMail(
                    mail = Mail(
                        remitente = "notificaciones@readapp.com.ar",
                        destinatario = "JPerez@gmail.com",
                        asunto = "Se agregó un Libro",
                        cuerpo = "El usuario 'MPerez' agrego el Libro 'Libro 1!' a la " +
                                "recomendación que tenía estos Títulos: Libro 2!, Libro 3!"
                    )
                )
            }
        }
    }

    describe("Registro de los usuarios que agregaron libros mediante observer") {
        val registroLibrosAgregados = RegistroLibrosAgregados()
        recomendacion.agregarObserver(registroLibrosAgregados)

        it("el usuario agrego 2 veces") {
            juan.agregarAmigo(maria)
            juan.leerLibro(libro2)
            juan.leerLibro(libro1)
            maria.leerLibro(libro1)
            maria.leerLibro(libro2)
            maria.agregarLibroARecomendacion(libro1, recomendacion)
            maria.agregarLibroARecomendacion(libro2, recomendacion)

            registroLibrosAgregados.aportesDeUsuarios.getValue(maria) shouldBe 2
        }
        it ("El usuario creador agrega 2 veces"){

            juan.leerLibro(libro2)
            juan.leerLibro(libro1)
            juan.agregarLibroARecomendacion(libro1, recomendacion)
            juan.agregarLibroARecomendacion(libro2, recomendacion)

            registroLibrosAgregados.aportesDeUsuarios.getValue(juan) shouldBe 2
        }
    }

    describe("dada una recomendacion con limite de libros agregados por usuario"){
        val limiteLibrosAgregadosMaria = LimiteLibrosAgregados(2, maria)
        recomendacion.agregarObserver(limiteLibrosAgregadosMaria)
        juan.agregarAmigo(maria)
        juan.leerLibro(libro1)
        juan.leerLibro(libro2)
        juan.leerLibro(libro3)
        maria.leerLibro(libro1)
        maria.leerLibro(libro2)
        maria.leerLibro(libro3)

        it("se elimina al usuario de la lista de amigos del creador si supera el limite"){
            maria.agregarLibroARecomendacion(libro1, recomendacion)
            maria.agregarLibroARecomendacion(libro2, recomendacion)

            juan.amigos shouldBe listOf()
        }
        it("si el usuario se pasa del limite, como ya no es amigo del creador, tira un error al querer agregar un tercer libro"){
            maria.agregarLibroARecomendacion(libro1, recomendacion)
            maria.agregarLibroARecomendacion(libro2, recomendacion)

            shouldThrow<IllegalArgumentException> { maria.agregarLibroARecomendacion(libro3, recomendacion) }
        }
        it("otro usuario tiene otro limite"){
            val steve = Usuario("Steve", "Johnson", "SteveJohnson", "sjohnson@gmail.com", "",1999, 3, 4, Lenguaje.Inglés, 200.00,Leedor(),Promedio(), "")
            val limiteLibrosAgregadosSteve = LimiteLibrosAgregados(3, steve)
            val libro4 = Libro(
                autor = walsh,
                paginas = 100,
                palabras = 4000,
                cant_ediciones = 3,
                ventasSemanales = 2000,
                complejo = false,
                imagen = ""
            )

            recomendacion.agregarObserver(limiteLibrosAgregadosSteve)
            juan.agregarAmigo(steve)
            juan.leerLibro(libro4)
            steve.leerLibro(libro1)
            steve.leerLibro(libro2)
            steve.leerLibro(libro3)
            steve.leerLibro(libro4)

            steve.agregarLibroARecomendacion(libro1, recomendacion)
            steve.agregarLibroARecomendacion(libro2, recomendacion)
            steve.agregarLibroARecomendacion(libro3, recomendacion)

            shouldThrow<IllegalArgumentException> { steve.agregarLibroARecomendacion(libro4, recomendacion) }
        }
    }

    describe("Valoración instantánea") {
        val valoracionInstantanea = ValoracionInstantanea()
        recomendacion.agregarObserver(valoracionInstantanea)
        juan.agregarAmigo(maria)
        juan.leerLibro(libro1)
        maria.leerLibro(libro1)

        it("Si María quiere agregar un libro sin haber valorado la recomendación, se agrega la valoración predeterminada") {
            maria.agregarLibroARecomendacion(libro1, recomendacion)

            val valoracionDeMaria = recomendacion.valoraciones.find { it.creador == maria }
            valoracionDeMaria?.valor shouldBe 5.0
        }
        it("Si María ya había hecho una valoración, ésta no cambia") {
            maria.valorarRecomendacion(recomendacion, 3.0, "Esta bien")
            maria.agregarLibroARecomendacion(libro1, recomendacion)

            val valoracionDeMaria: Valoracion? = recomendacion.valoraciones.find { it.creador == maria }
            valoracionDeMaria?.valor shouldBe 3.0
        }
    }
})