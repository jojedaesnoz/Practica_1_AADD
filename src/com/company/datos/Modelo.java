package com.company.datos;

import com.company.base.Pelicula;
import com.company.util.Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Modelo {
    private final String RUTA_PELICULAS = "src" + File.separator + "peliculas.dat";
    private ArrayList<Pelicula> peliculas;
    private int ultimoID;

    public Modelo() throws IOException, ClassNotFoundException {
        if (new File(RUTA_PELICULAS).exists()) {
            cargarDeDisco();
            ultimoID = peliculas.get(peliculas.size() - 1).getId();
        } else {
            peliculas = new ArrayList<>();
            ultimoID = 0;
        }
    }

    public void guardarPelicula(Pelicula peliculaNueva) throws IOException {
        // Comprobar si es nueva o estamos modificando otra existente
        int indice = peliculas.indexOf(peliculaNueva);
        Pelicula pelicula = buscarPeliculaPorID(peliculaNueva.getId());

        if (pelicula == null) {
            ultimoID++;
            peliculaNueva.setId(ultimoID);
            peliculas.add(peliculaNueva);
        } else {
            pelicula.setTitulo(pelicula.getTitulo());
            pelicula.setSinopsis(pelicula.getSinopsis());
            pelicula.setValoracion(pelicula.getValoracion());
            pelicula.setRecaudacion(pelicula.getRecaudacion());
        }


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

    public void eliminarPelicula(Pelicula pelicula) throws IOException {
        peliculas.remove(pelicula);
        guardarADisco();
    }

}
