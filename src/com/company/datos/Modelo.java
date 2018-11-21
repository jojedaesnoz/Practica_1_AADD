package com.company.datos;

import com.company.base.Pelicula;
import com.company.util.Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.company.util.Constantes.RUTA_IMAGENES;
import static com.company.util.Constantes.RUTA_PELICULAS;

public class Modelo {
    private ArrayList<Pelicula> peliculas;
    private int ultimoID;

    public Modelo() throws IOException, ClassNotFoundException {
        if (new File(RUTA_PELICULAS).exists()) {
            // Si existe el fichero, lo carga del disco
            cargarDeDisco();
            ultimoID = peliculas.get(peliculas.size() - 1).getId();
        } else {
            // Si no existe, crea un modelo de cero
            peliculas = new ArrayList<>();
            ultimoID = 0;
        }
    }

    public void guardarPelicula(Pelicula peliculaNueva) throws IOException {
        // Comprobar si es nueva o estamos modificando otra existente
        int indice = peliculas.indexOf(peliculaNueva);
        Pelicula pelicula = buscarPeliculaPorID(peliculaNueva.getId());

        // Guardar la imagen en disco
        File imagen = peliculaNueva.getImagen();
        String rutaImagen =  RUTA_IMAGENES + File.separator +  imagen.getName();

        // Si la carpeta imagenes no contiene ya esta imagen, la guarda
        if (!new File(rutaImagen).exists()){
            Util.copiarFichero(imagen.getPath(), rutaImagen);
            peliculaNueva.setImagen(new File(rutaImagen));
        }

        if (pelicula == null) {
            // Si no está contenida en el el modelo, la guarda
            pelicula = peliculaNueva;
            ultimoID++;
            pelicula.setId(ultimoID);
            peliculas.add(pelicula);
        } else {
            // Si ya está contenida, la modifica
            pelicula.setTitulo(peliculaNueva.getTitulo());
            pelicula.setSinopsis(peliculaNueva.getSinopsis());
            pelicula.setValoracion(peliculaNueva.getValoracion());
            pelicula.setRecaudacion(peliculaNueva.getRecaudacion());
        }

        // Guarda la imagen
        Util.copiarFichero(imagen.getPath(), rutaImagen);
        pelicula.setImagen(new File(rutaImagen));

        guardarADisco();
    }

    private Pelicula buscarPeliculaPorID(int id) {
        for (Pelicula pelicula: peliculas) {
            if (pelicula.getId() == id) {
                return pelicula;
            }
        }
        return null;
    }

    private void cargarDeDisco() throws IOException, ClassNotFoundException {
        peliculas = Util.deserialize(RUTA_PELICULAS);
    }

    private void guardarADisco() throws IOException {
        Util.serialize(RUTA_PELICULAS, peliculas);
    }

    public List<Pelicula> getPeliculas() {
        return peliculas;
    }

//    public void eliminarPelicula(Pelicula pelicula) throws IOException {
//        if(peliculas.remove(pelicula))
//            guardarADisco();
//    }

    public void eliminarPelicula(Pelicula peliculaABorrar) throws IOException {
        // Comprobar que existe antes de intentar borrar
        Pelicula pelicula = buscarPeliculaPorID(peliculaABorrar.getId());
        if (pelicula == null)
            return;

        peliculas.remove(pelicula);
        guardarADisco();
    }

}
