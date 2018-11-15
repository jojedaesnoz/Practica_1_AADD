package com.company.control;

import com.company.base.Pelicula;
import com.company.datos.Modelo;
import com.company.ui.Vista;

import javax.swing.*;
import javax.swing.event.MouseInputListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;

import static com.company.control.Controlador.Origen.MODIFICAR;
import static com.company.control.Controlador.Origen.NUEVO;

public class Controlador implements ActionListener {
    private Modelo modelo;
    private Vista vista;

    public enum Origen {
        NUEVO, MODIFICAR
    }
    Origen origen;

    public Controlador(Modelo modelo, Vista vista) {
        this.modelo = modelo;
        this.vista = vista;
        modoEdicion(false);
        addListeners();
        refrescarLista();
    }

    private void guardarPelicula() {
        // Recoger los datos con su verificacion de que son buenos
        Pelicula pelicula = getPelicula();
        if (pelicula == null)
            return;

        // Guardar
        try {
            modelo.guardarPelicula(pelicula);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refrescarLista() {
        vista.modeloPeliculas.removeAllElements();
        for (Pelicula pelicula: modelo.getPeliculas()) {
            vista.modeloPeliculas.addElement(pelicula);
        }
    }


    private void eliminarPelicula() {

    }


    private void modoEdicion(boolean modo){
        // Activar/desactivar botones
        vista.btGuardar.setEnabled(modo);
        vista.btCancelar.setEnabled(modo);

        // Activar/desactivar campos
        vista.lImagen.setEnabled(modo);
        vista.tfTitulo.setEnabled(modo);
        vista.taSinopsis.setEnabled(modo);
        vista.tfValoracion.setEnabled(modo);
        vista.tfRecaudacion.setEnabled(modo);
    }

    private void configurarModo(String actionCommand) {
        switch (actionCommand) {
            case "Nuevo":
            case "Modificar":
                modoEdicion(true);
                break;
            case "Guardar":
            case "Cancelar":
            case "Eliminar":
                modoEdicion(false);
                break;
            default:
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        configurarModo(e.getActionCommand());
        switch (e.getActionCommand()) {
            case "Extra":
                break;
            case "Modificar":
                cargarPeliculaSeleccionada();
                break;
            case "Guardar":
                guardarPelicula();
                break;
            case "Cancelar":
                vaciarPelicula();
                break;
            case "Eliminar":
                eliminarPelicula();
                modoEdicion(false);
                break;
            default:
                break;
        }
        refrescarLista();
    }


    private void addListeners() {
        // BOTONES
        JButton[] botones = {vista.btNuevo, vista.btModificar, vista.btGuardar,
                vista.btCancelar, vista.btEliminar};
        for (JButton boton: botones) boton.addActionListener(this);

        // IMAGEN


        // LISTA
        vista.listaPeliculas.addMouseListener(new MouseInputListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                cargarPeliculaSeleccionada();
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });
    }

    private void vaciarPelicula() {

    }

    private void cargarPeliculaSeleccionada() {
        Pelicula pelicula = vista.listaPeliculas.getSelectedValue();

        vista.tfTitulo.setText(pelicula.getTitulo());
        vista.tfValoracion.setText(String.valueOf(pelicula.getValoracion()));
        vista.tfRecaudacion.setText(String.valueOf(pelicula.getRecaudacion()));
        vista.taSinopsis.setText(pelicula.getSinopsis());
    }

    private Pelicula getPelicula(){
        // Coger los datos
        String titulo, sinopsis;
        int valoracion;
        float recaudacion;

        // Asegurarse de que los datos son correctos
        if (vista.tfTitulo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "El título es necesario", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        titulo = vista.tfTitulo.getText();
        sinopsis = vista.taSinopsis.getText();
        valoracion = vista.tfValoracion.getText().isEmpty()?
                0 : Integer.parseInt(vista.tfValoracion.getText());
        recaudacion = vista.tfRecaudacion.getText().isEmpty()?
                0 : Float.parseFloat(vista.tfRecaudacion.getText());

        // Construir la pelicula y devolverla
        Pelicula pelicula = new Pelicula();
        pelicula.setTitulo(titulo);
        pelicula.setSinopsis(sinopsis);
        pelicula.setValoracion(valoracion);
        pelicula.setRecaudacion(recaudacion);
        return pelicula;
    }

}
