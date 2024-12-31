package ar.edu.unsam.algo3

import ar.edu.unsam.algo3.domain.*
import ar.edu.unsam.algo3.repositorio.*
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import io.mockk.every
import io.mockk.verify
import io.mockk.verifyAll
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TestAdministrador : DescribeSpec ({
    isolationMode = IsolationMode.InstancePerTest

    val borges = Autor("Jorge Luis", "Borges","j.Borges", Lenguaje.Español, 54, 4)
    val walsh = Autor("Maria Elena", "Walsh", "M.Walsh", Lenguaje.Español, 49, 4)
    val libro1 = Libro(
        "El Inmortal",
        borges,
        paginas = 100,
        palabras = 4001,
        cant_ediciones = 1,
        ventasSemanales = 15000,
        complejo = false,
        ""
    )
    val libro2 = Libro(
        "La Reina Batata",
        walsh,
        paginas = 100,
        palabras = 4000,
        cant_ediciones = 3,
        ventasSemanales = 2000,
        complejo = false,
        ""
    )

    //MailSender
    val mockedMailSender=mockk<MailSender> (relaxUnitFun = true)

    //Repositorio de autores
    val repositorioAutores = Repositorio(CriterioAutores)

    //Repositorio de usuarios
    val repositorioUsuarios = Repositorio(CriterioUsuarios)
    val juan = Usuario("Juan","Perez","JPerez","JPerez@gmail.com","", 1996,7,13, Lenguaje.Español,200.00,Leedor(), Promedio(), "")
    val pedro = Usuario("Pedro","Gonzales","PGonzales","pedrogonzales@gmail.com","",1996,7,13, Lenguaje.Español,200.00,Leedor(), Promedio(), "")
    val maria = Usuario("Maria", "Perez", "MPerez", "MPerez@gmail.com","", 1990, 4, 22, Lenguaje.Español, 200.00,Leedor(), Promedio(), "")
    repositorioUsuarios.create(juan)
    repositorioUsuarios.create(pedro)
    repositorioUsuarios.create(maria)

    //Repositorio de recomendaciones
    val repositorioRecomendaciones = Repositorio(CriterioRecomendaciones)
    val recomendacionDeMaria = maria.agregarRecomendacion(true)
    maria.leerLibro(libro1)
    maria.leerLibro(libro2)
    recomendacionDeMaria.agregarLibro(maria, libro1)
    recomendacionDeMaria.agregarLibro(maria, libro2)
    repositorioRecomendaciones.create(recomendacionDeMaria)

    //Repositorio Centros De Lectura
    val repositorioCentrodeLectura = Repositorio(CriterioCentrosDeLectura)
    val particular = Particular()
    val biblioteca = Biblioteca()
    val centroDisponible =  CentroDeLectura("Av. 25 de Mayo", 3.00, libro1, biblioteca)
    val centroDisponible2 =  CentroDeLectura("Av. 25 de Mayo", 4.00, libro2, biblioteca)
    val centroVencido =  CentroDeLectura("Av. 25 de Mayo", 6.00, libro2, particular)
    repositorioCentrodeLectura.create(centroDisponible)
    repositorioCentrodeLectura.create(centroVencido)
    repositorioCentrodeLectura.create(centroDisponible2)


    describe ("Administrador"){
        val admin = Administrador()
        // BORRAR USUARIOS INACTIVOS
        describe("Command - Borrar Usuarios Inactivos") {
            it("Si es considerado amigo por otro usuario, ya no es inactivo") {
                maria.agregarAmigo(juan)
                admin.run(
                    listOf(
                        BorraUsuariosInactivos(repositorioUsuarios, repositorioRecomendaciones, mockedMailSender)
                    )
                )
                repositorioUsuarios.elementos shouldBe listOf(juan, maria)
            }
            it("Si valora una recomendacion, ya no es inactivo") {
                pedro.leerLibro(libro1)
                pedro.leerLibro(libro2)
                pedro.valorarRecomendacion(recomendacionDeMaria, 5.0, "buenisima")
                admin.run(
                    listOf(
                        BorraUsuariosInactivos(repositorioUsuarios, repositorioRecomendaciones, mockedMailSender)
                    )
                )
                repositorioUsuarios.elementos shouldBe listOf(pedro, maria)
            }
            it("Si crea una recomendacion, ya no es inactivo") {
                val recomendacionDeJuan = juan.agregarRecomendacion(true)
                repositorioRecomendaciones.create(recomendacionDeJuan)
                admin.run(
                    listOf(
                        BorraUsuariosInactivos(repositorioUsuarios, repositorioRecomendaciones, mockedMailSender)
                    )
                )
                repositorioUsuarios.elementos shouldBe listOf(juan, maria)
            }
            it("El admin recibe una notificacion por mail despues de Borrar los Usuario Inactivos") {
                maria.agregarAmigo(juan)
                admin.run(
                    listOf(
                        BorraUsuariosInactivos(repositorioUsuarios, repositorioRecomendaciones, mockedMailSender)
                    )
                )
                verify(exactly = 1) {
                    mockedMailSender.mandarMail(
                        mail = Mail(
                            remitente = "notificaciones@readapp.com.ar",
                            destinatario = "admin@readapp.com.ar",
                            asunto = "Notificacion para administrador",
                            cuerpo = "Se realizo el proceso: BorraUsuariosInactivos"
                        )
                    )
                }
            }
        }

        // ACTUALIZACION DE LIBRO
        describe("Command - Actualización de Libro"){
            val mockedServiceLibros = mockk<ServiceLibros>(relaxUnitFun = true)
            val pruebaServicio2 = SerializableLibro(2, 12, 15000)
            val listaLibros = mutableListOf(pruebaServicio2)
            every { mockedServiceLibros.getLibros() } answers { Json.encodeToString(listaLibros) }
            val repositorioLibros = Repositorio(CriterioLibros)
            repositorioLibros.create(libro1)
            repositorioLibros.create(libro2)
            it("Deberia actualizarse segundo libro del repositorio"){
                repositorioLibros.getByID(2)?.cant_ediciones shouldBe 3
                repositorioLibros.getByID(2)?.ventasSemanales shouldBe 2000
                admin.run(listOf(
                    ActualizarLibros(repositorioLibros, mockedServiceLibros, mockedMailSender)
                ))
                repositorioLibros.getByID(2)?.cant_ediciones shouldBe 12
                repositorioLibros.getByID(2)?.ventasSemanales shouldBe 15000
            }
            it("El admin recibe una notificacion por mail despues de Actualizar los Libros del repositorio"){
                admin.run(listOf(
                    ActualizarLibros(repositorioLibros, mockedServiceLibros, mockedMailSender)
                ))
                verify(exactly = 1) {
                    mockedMailSender.mandarMail(
                        mail = Mail(
                            remitente = "notificaciones@readapp.com.ar",
                            destinatario = "admin@readapp.com.ar",
                            asunto = "Notificacion para administrador",
                            cuerpo = "Se realizo el proceso: ActualizarLibros"
                        )
                    )
                }
            }
        }

        //BORRAR CENTROS DE LECTURA INACTIVOS
        describe("Command - Borrar Centros de lectura inactivos"){
            //CENTRO PARTICULAR VENCIDO
            particular.cambiarCantidadParticipantes(2.00)
            centroVencido.solicitarReserva(juan)
            centroVencido.solicitarReserva(pedro)

            it("Sin borrar ningun centro"){
                repositorioCentrodeLectura.elementos shouldBe listOf(centroDisponible,centroVencido, centroDisponible2)
            }

            it("Borrar centros inactivos"){
                admin.run(listOf(
                    BorraCentrosExpirados(repositorioCentrodeLectura,mockedMailSender)
                ))
                repositorioCentrodeLectura.elementos shouldBe listOf(centroDisponible, centroDisponible2)
            }
            it("El admin recibe una notificacion por mail despues de Borrar Centro de Lectura Inactivo") {
                admin.run(
                    listOf(
                        BorraCentrosExpirados(repositorioCentrodeLectura,mockedMailSender)
                    )
                )
                verify(exactly = 1) {
                    mockedMailSender.mandarMail(
                        mail = Mail(
                            remitente = "notificaciones@readapp.com.ar",
                            destinatario = "admin@readapp.com.ar",
                            asunto = "Notificacion para administrador",
                            cuerpo = "Se realizo el proceso: BorraCentrosExpirados"
                        )
                    )
                }
            }
        }

        // AGREGAR AUTORES
        describe("Command - Agregar Autores"){
            it ("Puede agregar lista de autores al repositorio"){
                val listaDeAutores = mutableListOf(borges, walsh)
                admin.run(listOf(
                    AgregaAutores(repositorioAutores, listaDeAutores, mockedMailSender)
                ))
                repositorioAutores.elementos shouldBe listOf(borges, walsh)
            }
            it("El admin recibe una notificacion por mail despues de Agregar Autor"){
                val listaDeAutores = mutableListOf(borges, walsh)
                admin.run(
                    listOf(
                        AgregaAutores(repositorioAutores, listaDeAutores, mockedMailSender)
                    )
                )
                verify(exactly = 1) {
                    mockedMailSender.mandarMail(
                        mail = Mail(
                            remitente = "notificaciones@readapp.com.ar",
                            destinatario = "admin@readapp.com.ar",
                            asunto = "Notificacion para administrador",
                            cuerpo = "Se realizo el proceso: AgregaAutores"
                        )
                    )
                }
            }
        }

        describe("Puede hacer varios procesos a la vez"){
            it("Agrega autores y borra usuarios inactivos"){
                val listaDeAutores = mutableListOf(borges, walsh)
                admin.run(listOf(
                    AgregaAutores(repositorioAutores, listaDeAutores, mockedMailSender),
                    BorraUsuariosInactivos(repositorioUsuarios, repositorioRecomendaciones, mockedMailSender)
                ))
                repositorioAutores.elementos shouldBe listOf(borges, walsh)
                repositorioUsuarios.elementos shouldBe listOf(maria)
            }
            it("El admin recibe notificaciones por todos los procesos que realiza"){
                val listaDeAutores = mutableListOf(borges, walsh)
                admin.run(listOf(
                    AgregaAutores(repositorioAutores, listaDeAutores, mockedMailSender),
                    BorraUsuariosInactivos(repositorioUsuarios, repositorioRecomendaciones, mockedMailSender),
                    BorraCentrosExpirados(repositorioCentrodeLectura,mockedMailSender),
                ))
                verifyAll{
                    mockedMailSender.mandarMail(
                        mail = Mail(
                            remitente = "notificaciones@readapp.com.ar",
                            destinatario = "admin@readapp.com.ar",
                            asunto = "Notificacion para administrador",
                            cuerpo = "Se realizo el proceso: AgregaAutores"
                        )
                    )
                    mockedMailSender.mandarMail(
                        mail = Mail(
                            remitente = "notificaciones@readapp.com.ar",
                            destinatario = "admin@readapp.com.ar",
                            asunto = "Notificacion para administrador",
                            cuerpo = "Se realizo el proceso: BorraUsuariosInactivos"
                        )
                    )
                    mockedMailSender.mandarMail(
                        mail = Mail(
                            remitente = "notificaciones@readapp.com.ar",
                            destinatario = "admin@readapp.com.ar",
                            asunto = "Notificacion para administrador",
                            cuerpo = "Se realizo el proceso: BorraCentrosExpirados"
                        )
                    )
                }
            }
        }
    }
})