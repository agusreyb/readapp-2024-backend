package ar.edu.unsam.algo3

import ar.edu.unsam.algo3.domain.*
import ar.edu.unsam.algo3.repositorio.CriterioCentrosDeLectura
import ar.edu.unsam.algo3.repositorio.Repositorio
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class CentrosTest : DescribeSpec({
    isolationMode = IsolationMode.InstancePerTest

    val juan = Usuario(
        "Juan",
        "Perez",
        "JPerez",
        "JPerez@gmail.com",
        "",
        1996,
        7,
        13,
        Lenguaje.Español,
        200.00,
        Leedor(),
        Promedio(),
        ""
    )

    val maria = Usuario(
        "Maria",
        "Perez",
        "MPerez",
        "MPerez@gmail.com",
        "",
        1990,
        4,
        22,
        Lenguaje.Español,
        200.00,
        Leedor(),
        Promedio(),
        ""
    )

    val steve = Usuario(
        "Steve",
        "Johnson",
        "SteveJohnson",
        "sjohnson@gmail.com",
        "",
        1999,
        3,
        4,
        Lenguaje.Inglés,
        200.00,
        Leedor(),
        Promedio(),
        ""
    )

    val usuario = Usuario(
        "Juan",
        "Perez",
        "JPerez",
        "JPerez@gmail.com",
        "",
        1996,
        7,
        13,
        Lenguaje.Español,
        200.00,
        Leedor(),
        Promedio(),
        ""
    )

    val juanjose = Usuario(
        "Juan Jose",
        "Perez",
        "doublejj",
        "jjjjj@gmail.com",
        "",
        2004,
        3,
        5,
        Lenguaje.Inglés,
        200.00,
        Leedor(),
        Promedio(),
        ""
    )

    //AUTORES
    val borges = Autor("Jorge Luis", "Borges","j.Borges", Lenguaje.Español, 54, 4)
    val walsh = Autor("Maria Elena", "Walsh", "M.Walsh", Lenguaje.Español, 49, 4)
    val shakespeare = Autor("William", "Shakespeare","W.Shakespeare", Lenguaje.Inglés , 52 , 0)

    //LIBROS
    val libroComplejo = Libro(
        autor = walsh,
        paginas = 300,
        palabras = 1400,
        cant_ediciones = 1,
        ventasSemanales = 9000,
        complejo = true,
        imagen = ""
    )
    val libroNoDesafiante = Libro(
        autor = borges,
        paginas = 200,
        palabras = 120000,
        cant_ediciones = 3,
        ventasSemanales = 12000,
        complejo = false,
        imagen = ""
    )
    val libroIdiomas = Libro(
        autor = shakespeare,
        paginas = 100,
        palabras = 4000,
        cant_ediciones = 5,
        ventasSemanales = 15000,
        complejo = false,
        imagen = ""
    )

    //CENTROS DE LECTURA
    val particular = Particular()
    val editorial = Editorial()
    val biblioteca = Biblioteca()

    val centroParticular =  CentroDeLectura("Av. 25 de Mayo", 3.00, libroIdiomas, particular )
    val centroEditorialBestSeller =  CentroDeLectura("Av. 25 de Mayo", 4.00, libroNoDesafiante, editorial )
    val centroEditorialNoBestSeller =  CentroDeLectura("Av. 25 de Mayo", 4.00, libroComplejo, editorial )
    val centroBiblioteca =  CentroDeLectura("Av. 25 de Mayo", 6.00, libroComplejo, biblioteca )

    val repositorioCentros = Repositorio(CriterioCentrosDeLectura)

    // SUPONEMOS QUE EL COSTO BASE DE LA RESERVA ES 1000 //

    describe ("Centro Particular"){
        it("Disponibilidad del encuentro true"){
            centroParticular.estaDisponibleEncuentro() shouldBe true
        }
        it("Disponibilidad del encuentro false"){
            particular.cambiarCantidadParticipantes(2.00)
            centroParticular.solicitarReserva(maria)
            centroParticular.solicitarReserva(steve)

            shouldThrow<IllegalArgumentException> { centroParticular.solicitarReserva(juan) }
            centroParticular.estaDisponibleEncuentro() shouldBe false
        }

        describe(("Costo reserva con promocion hasta el 20% del cupo y 20 personas de capacidad maxima")){
            it ("Cupo menor del 20%"){
                centroParticular.costoReserva() shouldBe 1000
            }
            it ("Cupo mayor al 20%"){ //TAMBIEN SE TESTEA LA LINEA DEL METODO "SOLICITAR RESERVA"

                particular.cambiarCantidadParticipantes(10.00)

                centroParticular.solicitarReserva(steve)
                centroParticular.solicitarReserva(maria)
                centroParticular.solicitarReserva(juan)
                centroParticular.solicitarReserva(usuario)
                centroParticular.solicitarReserva(juanjose)

                centroParticular.costoReserva() shouldBe 1500
            }
        }
    }

    describe ("Centro Editorial"){
        describe(("Con presencia del autor")){
            //PRESENCIA DEL AUTOR TRUE

            centroEditorialNoBestSeller.solicitarReserva(steve)
            centroEditorialNoBestSeller.solicitarReserva(maria)
            centroEditorialBestSeller.solicitarReserva(juan)
            centroEditorialBestSeller.solicitarReserva(usuario)

            it("Se puede cambiar el costo limite de reserva"){
                editorial.cambiarCostoLimite(10000.00)
                editorial.costoLimiteReserva shouldBe 10000.00
            }
            it ("No es best seller") {
                centroEditorialNoBestSeller.costoReserva() shouldBe 2000
            }
            it ("Cupo Participantes No BestSeller"){
                //SUPONEMOS QUE EL VALOR A ALCANZAR ES 20000
                centroEditorialNoBestSeller.cupoParticipantes() shouldBe 10
            }
            it ("Es Best Seller"){
                centroEditorialBestSeller.costoReserva() shouldBe 3000
            }
            it ("Cupo Participantes Si BestSeller"){
                //SUPONEMOS QUE EL VALOR A ALCANZAR ES 20000
                centroEditorialBestSeller.cupoParticipantes() shouldBe 7
            }
        }

        describe ("El autor no se encuentra presente en el centro (WALSH)"){

            editorial.modificarPresenciaAutor()

            centroEditorialNoBestSeller.solicitarReserva(juan)
            centroEditorialNoBestSeller.solicitarReserva(steve)
            centroEditorialNoBestSeller.solicitarReserva(maria)

            it ("Costo reserva"){
                centroEditorialNoBestSeller.costoReserva() shouldBe 1800
            }
            it ("Cupo Participantes sin autor"){
                //SUPONEMOS QUE EL VALOR A ALCANZAR ES 20000, SE REDONDEA CON .roundToInt()
                centroEditorialNoBestSeller.cupoParticipantes() shouldBe 11
            }
        }
    }

    describe("Centro Biblioteca"){

        centroBiblioteca.solicitarReserva(steve)
        centroBiblioteca.solicitarReserva(maria)
        centroBiblioteca.solicitarReserva(juan)
        centroBiblioteca.solicitarReserva(usuario)
        centroBiblioteca.solicitarReserva(juanjose)


        biblioteca.agregarGastoFijo(20000.00)
        biblioteca.agregarGastoFijo(10000.00)
        biblioteca.agregarGastoFijo(5000.00)
        biblioteca.agregarGastoFijo(2000.00)  // SUMA = 37000

        it("Se puede la dimension de la sala"){
            biblioteca.cambiarDimensionSala(80.00)
            biblioteca.dimensionSala shouldBe 80.00
        }
        it ("Cantidad maxima de participantes"){
            //SUPONEMOS QUE LA SALA TIENE 60m2
            centroBiblioteca.cupoParticipantes() shouldBe 60
        }
        it ("Costo encuentro menor a 5 dias"){
            centroBiblioteca.agregarFecha(2024, 7, 13)
            centroBiblioteca.agregarFecha(2024, 10, 6)
            centroBiblioteca.agregarFecha(2024, 11, 30)
            centroBiblioteca.agregarFecha(2024, 8, 23)

            centroBiblioteca.costoReserva() shouldBe 11360

        }
        it ("Costo encuentro mayor a 5 dias"){
            centroBiblioteca.agregarFecha(2024, 7, 13)
            centroBiblioteca.agregarFecha(2024, 10, 6)
            centroBiblioteca.agregarFecha(2024, 11, 30)
            centroBiblioteca.agregarFecha(2024, 8, 23)
            centroBiblioteca.agregarFecha(2024, 6,22)
            centroBiblioteca.agregarFecha(2024, 8, 10)

            centroBiblioteca.costoReserva() shouldBe 12100

        }
    }

    describe("Buscar centros de lectura"){
        it ("Obtengo lista del repositorio"){

            repositorioCentros.create(centroParticular)
            repositorioCentros.create(centroEditorialBestSeller)

            repositorioCentros.listarCentrosLectura(repositorioCentros) shouldBe listOf(centroParticular, centroEditorialBestSeller)
        }
    }

})
