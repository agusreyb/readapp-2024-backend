package ar.edu.unsam.algo3.domain
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonSubTypes
/* SE SERIALIZA CORRECTAMENTE LA INTERFAZ PARA PODER PASARLO COMO JSON */

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type" // Esto agregará un campo "type" en el JSON para identificar la clase
)
@JsonSubTypes(
    JsonSubTypes.Type(value = Promedio::class, name = "Promedio"),
    JsonSubTypes.Type(value = Ansioso::class, name = "Ansioso"),
    JsonSubTypes.Type(value = Fanatico::class, name = "Fanatico"),
    JsonSubTypes.Type(value = Recurrente::class, name = "Recurrente")
)


interface FormaDeLeer{
    fun tiempoDeLectura (libro : Libro, usuario: Usuario): Double
}

class Promedio: FormaDeLeer {
    override fun tiempoDeLectura (libro : Libro, usuario: Usuario): Double = usuario.tiempoDeLecturaPromedio(libro)
}

class Ansioso: FormaDeLeer {
    override fun tiempoDeLectura(libro: Libro, usuario: Usuario): Double {
        return if (libro.esBestSeller()) usuario.tiempoDeLecturaPromedio(libro) * 0.5 else usuario.tiempoDeLecturaPromedio(libro) * 0.8
    }
}

class Fanatico: FormaDeLeer {
    override fun tiempoDeLectura(libro: Libro, usuario: Usuario): Double {
        return if (!validacionFanatico(libro, usuario)) usuario.tiempoDeLecturaPromedio(libro)
        else tiempoLongitud(libro, usuario)
    }

    private fun validacionFanatico(libro : Libro, usuario : Usuario) : Boolean {
        return (!usuario.librosLeidos.contains(libro) && usuario.autoresPreferidos.contains(libro.autor))
    }

    private fun tiempoLongitud(libro : Libro, usuario: Usuario) : Double {
        return if (!libro.esLargo()) (usuario.tiempoDeLecturaPromedio(libro) + (libro.paginas*2))
        else usuario.tiempoDeLecturaPromedio(libro) + (libro.paginasLargo*2) + (libro.paginas - libro.paginasLargo)
    }
}

class Recurrente: FormaDeLeer {
    private fun valorRecurrente(libro: Libro, usuario: Usuario): Double {
        return if (usuario.cantVecesLeido.getValue(libro.id) <= 5) {
            calculoPorcentaje(libro, usuario)
        } else {
            usuario.tiempoDeLecturaPromedio(libro) * 0.95
        }
    }

    private fun calculoPorcentaje(libro: Libro, usuario: Usuario): Double {
        return ((usuario.tiempoDeLecturaPromedio(libro)) - ((usuario.cantVecesLeido.getValue(libro.id) / 100) * usuario.tiempoDeLecturaPromedio(libro
        )))
    }

    override fun tiempoDeLectura(libro: Libro, usuario: Usuario): Double {
        return if (usuario.librosLeidos.contains(libro)) {
            valorRecurrente(libro, usuario)
        } else {
            usuario.tiempoDeLecturaPromedio(libro)
        }
    }
}
