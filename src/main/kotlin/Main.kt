import java.io.File
import java.text.SimpleDateFormat
import java.util.*

data class Album(
    val nombreAlbum: String,
    val totalCanciones: Int,
    val fechaEmision: Date,
    val autor: String,
    val duracionAlbum: Int,
    val canciones: MutableList<Cancion> = mutableListOf()
)

data class Cancion(
    val reproducciones: Float,
    val contenidoExplicito: Boolean,
    val duracionCancion: String,
    val nombreCancion: String,
    val cantidadLikes: Float
)

val dateFormat = SimpleDateFormat("yyyy-MM-dd")
val dataFile = File("datos.txt")

fun main() {
    while (true) {
        println("\n--- Menú ---")
        println("1. Crear Álbum")
        println("2. Leer Álbumes")
        println("3. Actualizar Álbum")
        println("4. Eliminar Álbum")
        println("5. Crear Canción")
        println("6. Leer Canciones")
        println("7. Actualizar Canción")
        println("8. Eliminar Canción")
        println("9. Salir")
        print("Seleccione una opción: ")

        when (readLine()?.toIntOrNull()) {
            1 -> crearAlbum()
            2 -> leerAlbumes()
            3 -> actualizarAlbum()
            4 -> eliminarAlbum()
            5 -> crearCancion()
            6 -> leerCanciones()
            7 -> actualizarCancion()
            8 -> eliminarCancion()
            9 -> return
            else -> println("Opción no válida")
        }
    }
}

fun crearAlbum() {
    print("Nombre del Álbum: ")
    val nombreAlbum = readLine()!!
    print("Total de Canciones: ")
    val totalCanciones = readLine()!!.toInt()
    print("Fecha de Emisión (yyyy-MM-dd): ")
    val fechaEmision = dateFormat.parse(readLine()!!)
    print("Autor: ")
    val autor = readLine()!!
    print("Duración del Álbum (minutos): ")
    val duracionAlbum = readLine()!!.toInt()

    val album = Album(nombreAlbum, totalCanciones, fechaEmision, autor, duracionAlbum)
    dataFile.appendText("ALBUM:${album.nombreAlbum},${album.totalCanciones},${dateFormat.format(album.fechaEmision)},${album.autor},${album.duracionAlbum}\n")
    println("Álbum creado exitosamente")
}

fun leerAlbumes() {
    if (!dataFile.exists()) {
        println("No hay álbumes disponibles")
        return
    }
    println("\n--- Lista de Álbumes ---")
    dataFile.forEachLine {
        if (it.startsWith("ALBUM:")) {
            println(it.removePrefix("ALBUM:"))
        }
    }
}

fun actualizarAlbum() {
    if (!dataFile.exists()) {
        println("No hay álbumes disponibles")
        return
    }

    print("Nombre del Álbum a actualizar: ")
    val nombreAlbum = readLine()!!
    val lines = dataFile.readLines().toMutableList()
    val albumIndex = lines.indexOfFirst { it.startsWith("ALBUM:$nombreAlbum,") }

    if (albumIndex == -1) {
        println("El álbum no existe")
        return
    }

    println("Ingrese los nuevos datos del Álbum:")
    print("Total de Canciones: ")
    val totalCanciones = readLine()!!.toInt()
    print("Fecha de Emisión (yyyy-MM-dd): ")
    val fechaEmision = dateFormat.parse(readLine()!!)
    print("Autor: ")
    val autor = readLine()!!
    print("Duración del Álbum (minutos): ")
    val duracionAlbum = readLine()!!.toInt()

    val album = Album(nombreAlbum, totalCanciones, fechaEmision, autor, duracionAlbum)
    lines[albumIndex] = "ALBUM:${album.nombreAlbum},${album.totalCanciones},${dateFormat.format(album.fechaEmision)},${album.autor},${album.duracionAlbum}"

    dataFile.writeText(lines.joinToString("\n") + "\n")
    println("Álbum actualizado exitosamente")
}

fun eliminarAlbum() {
    if (!dataFile.exists()) {
        println("No hay álbumes disponibles")
        return
    }

    print("Nombre del Álbum a eliminar: ")
    val nombreAlbum = readLine()!!
    val lines = dataFile.readLines().toMutableList()
    val albumIndex = lines.indexOfFirst { it.startsWith("ALBUM:$nombreAlbum,") }

    if (albumIndex == -1) {
        println("El álbum no existe")
        return
    }

    lines.removeAt(albumIndex)
    lines.removeAll { it.startsWith("CANCION:$nombreAlbum,") }

    dataFile.writeText(lines.joinToString("\n") + "\n")
    println("Álbum y sus canciones eliminados exitosamente")
}

fun crearCancion() {
    print("Nombre del Álbum para agregar la canción: ")
    val nombreAlbum = readLine()!!
    if (!dataFile.exists() || !dataFile.readLines().any { it.startsWith("ALBUM:$nombreAlbum,") }) {
        println("El álbum no existe")
        return
    }

    print("Nombre de la Canción: ")
    val nombreCancion = readLine()!!
    print("Reproducciones: ")
    val reproducciones = readLine()!!.toFloat()
    print("Contenido Explícito (true/false): ")
    val contenidoExplicito = readLine()!!.toBoolean()
    print("Duración de la Canción (mm:ss): ")
    val duracionCancion = readLine()!!
    print("Cantidad de Likes: ")
    val cantidadLikes = readLine()!!.toFloat()

    val cancion = Cancion(reproducciones, contenidoExplicito, duracionCancion, nombreCancion, cantidadLikes)
    dataFile.appendText("CANCION:${nombreAlbum},${cancion.reproducciones},${cancion.contenidoExplicito},${cancion.duracionCancion},${cancion.nombreCancion},${cancion.cantidadLikes}\n")
    println("Canción creada exitosamente")
}

fun leerCanciones() {
    if (!dataFile.exists()) {
        println("No hay canciones disponibles")
        return
    }
    println("\n--- Lista de Canciones ---")
    dataFile.forEachLine {
        if (it.startsWith("CANCION:")) {
            println(it.removePrefix("CANCION:"))
        }
    }
}

fun actualizarCancion() {
    if (!dataFile.exists()) {
        println("No hay canciones disponibles")
        return
    }

    print("Nombre del Álbum de la Canción a actualizar: ")
    val nombreAlbum = readLine()!!
    print("Nombre de la Canción a actualizar: ")
    val nombreCancion = readLine()!!
    val lines = dataFile.readLines().toMutableList()
    val cancionIndex = lines.indexOfFirst { it.startsWith("CANCION:$nombreAlbum,") && it.contains(",$nombreCancion,") }

    if (cancionIndex == -1) {
        println("La canción no existe")
        return
    }

    println("Ingrese los nuevos datos de la Canción:")
    print("Reproducciones: ")
    val reproducciones = readLine()!!.toFloat()
    print("Contenido Explícito (true/false): ")
    val contenidoExplicito = readLine()!!.toBoolean()
    print("Duración de la Canción (mm:ss): ")
    val duracionCancion = readLine()!!
    print("Cantidad de Likes: ")
    val cantidadLikes = readLine()!!.toFloat()

    val cancion = Cancion(reproducciones, contenidoExplicito, duracionCancion, nombreCancion, cantidadLikes)
    lines[cancionIndex] = "CANCION:${nombreAlbum},${cancion.reproducciones},${cancion.contenidoExplicito},${cancion.duracionCancion},${cancion.nombreCancion},${cancion.cantidadLikes}"

    dataFile.writeText(lines.joinToString("\n") + "\n")
    println("Canción actualizada exitosamente")
}

fun eliminarCancion() {
    if (!dataFile.exists()) {
        println("No hay canciones disponibles")
        return
    }

    print("Nombre del Álbum de la Canción a eliminar: ")
    val nombreAlbum = readLine()!!
    print("Nombre de la Canción a eliminar: ")
    val nombreCancion = readLine()!!
    val lines = dataFile.readLines().toMutableList()
    val cancionIndex = lines.indexOfFirst { it.startsWith("CANCION:$nombreAlbum,") && it.contains(",$nombreCancion,") }

    if (cancionIndex == -1) {
        println("La canción no existe")
        return
    }

    lines.removeAt(cancionIndex)

    dataFile.writeText(lines.joinToString("\n") + "\n")
    println("Canción eliminada exitosamente")
}
