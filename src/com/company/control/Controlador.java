package com.company.control;

import com.company.base.Pelicula;
import com.company.datos.Modelo;
import com.company.ui.Vista;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static com.company.control.Controlador.Origen.MODIFICAR;
import static com.company.control.Controlador.Origen.NUEVO;
import static com.company.util.Constantes.DEFAULT_IMAGE;
import static javax.swing.JOptionPane.OK_OPTION;

public class Controlador implements ActionListener, MouseListener, DocumentListener {
    private Modelo modelo;
    private Vista vista;
    private int idPeliculaSeleccionada;
    private File imagenSeleccionada;

    public enum Origen {
        NUEVO, MODIFICAR
    }
    private Origen origen;

    public Controlador(Modelo modelo, Vista vista) {
        this.modelo = modelo;
        this.vista = vista;
        modoEdicion(false);
        addListeners();
        colocarImagen(new File(DEFAULT_IMAGE));
        vista.btDeshacer.setEnabled(false);
        refrescarLista(modelo.getPeliculas());
    }
    //Todo: poner que guarde las fotos y las cargue desde disco

    private void guardarPelicula() {
        // Recoger los datos con su verificacion de que son buenos
        Pelicula pelicula = cogerDatosPelicula();
        if (pelicula == null)
            return;

        if (origen == MODIFICAR)
            pelicula.setId(idPeliculaSeleccionada);

        // Guardar
        try {
            modelo.guardarPelicula(pelicula);
            origen = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void guardarPelicula(String destino) {
        // Recoger los datos con su verificacion de que son buenos
        Pelicula pelicula = cogerDatosPelicula();
        if (pelicula == null)
            return;

        if (origen == MODIFICAR)
            pelicula.setId(idPeliculaSeleccionada);

        // Guardar
        try {
            modelo.guardarPelicula(pelicula, destino);
            origen = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refrescarLista(List<Pelicula> lista) {
        vista.modeloPeliculas.removeAllElements();
        for (Pelicula pelicula: lista) {
            vista.modeloPeliculas.addElement(pelicula);
        }
    }

    private void eliminarPelicula() {
        try {
            Pelicula peliculaSeleccionada = vista.listaPeliculas.getSelectedValue();
            modelo.eliminarPelicula(peliculaSeleccionada);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void modoEdicion(boolean modo){
        // Activar/desactivar botones
        vista.btGuardar.setEnabled(modo);
        vista.btCancelar.setEnabled(modo);

        // Activar/desactivar campos
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
            case "Guardar Como":
            case "Cancelar":
            case "Eliminar":
            case "Eliminar Todo":
                modoEdicion(false);
                break;
            default:
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Configura el modo edicion segun que boton ha sido pulsado
        configurarModo(e.getActionCommand());

        // Realiza la accion correspondiente a cada boton
        switch (e.getActionCommand()) {
            case "Nuevo":
                origen = NUEVO;
                vaciarCajas();
                break;
            case "Modificar":
                cargarPelicula(vista.listaPeliculas.getSelectedValue());
                origen = MODIFICAR;
                break;
            case "Guardar":
                guardarPelicula();
                break;
            case "Guardar Como":
                JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (jfc.showOpenDialog(null) == JFileChooser.CANCEL_OPTION)
                    return;
                String rutaSeleccionada = jfc.getSelectedFile().getPath();
                guardarPelicula(rutaSeleccionada + File.separator + "peliculas.dat");
            case "Cancelar":
                vaciarCajas();
                break;
            case "Eliminar":
                eliminarPelicula();
                vista.btDeshacer.setEnabled(true);
                break;
            case "Deshacer":
                deshacerBorrado();
                vista.btDeshacer.setEnabled(false);
                break;
            case "Eliminar Todo":
                String warning = "¿Está seguro de que desea borrarlo todo?";
                if(JOptionPane.showConfirmDialog(null, warning) == OK_OPTION){
                    modelo.borrarTodo();
                    if (origen != NUEVO) {
                        vaciarCajas();
                    }
                }
                break;
            default:
                break;
        }
        refrescarLista(modelo.getPeliculas());
    }

    private void deshacerBorrado() {
        try {
            Pelicula borrada = modelo.getUltimaBorrada();
            modelo.guardarPelicula(borrada);
            cargarPelicula(borrada);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void addListeners() {
        // BOTONES
        JButton[] botones = {vista.btNuevo, vista.btModificar, vista.btGuardar, vista.btGuardarComo,
                vista.btCancelar, vista.btEliminar, vista.btDeshacer, vista.btEliminarTodo};
        for (JButton boton: botones) {
            boton.addActionListener(this);
        }

        // IMAGEN
        vista.lImagen.addMouseListener(this);

        // TEXTO EN BUSQUEDA
        vista.tfBusqueda.getDocument().addDocumentListener(this);

        // CLICK EN LISTA
        vista.listaPeliculas.addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getComponent().equals(vista.listaPeliculas)) {

            // CLICK EN LA LISTA
            cargarPelicula(vista.listaPeliculas.getSelectedValue());
            idPeliculaSeleccionada = vista.listaPeliculas.getSelectedValue().getId();

        } else if (e.getComponent().equals(vista.lImagen)) {
            // CLICK EN LA IMAGEN
            JFileChooser jfc = new JFileChooser();
            if (jfc.showOpenDialog(null) == JFileChooser.CANCEL_OPTION)
                return;

            imagenSeleccionada = jfc.getSelectedFile();
            colocarImagen(imagenSeleccionada);
        }
    }

    private void vaciarCajas() {
        JTextField[] cajas = {vista.tfTitulo,  vista.tfValoracion, vista.tfRecaudacion};
        for (JTextField textField: cajas) {
            textField.setText("");
        }
        vista.taSinopsis.setText("");
        colocarImagen(new File(DEFAULT_IMAGE));
    }

    private void cargarPelicula(Pelicula pelicula) {
        idPeliculaSeleccionada = pelicula.getId();

        vista.tfTitulo.setText(pelicula.getTitulo());
        vista.tfValoracion.setText(String.valueOf(pelicula.getValoracion()));
        vista.tfRecaudacion.setText(String.valueOf(pelicula.getRecaudacion()));
        vista.taSinopsis.setText(pelicula.getSinopsis());

        // Colocar la imagen si tiene, y si no, la de por defecto
        if (pelicula.getImagen().exists()) {
            colocarImagen(pelicula.getImagen());
        }
        else {
            colocarImagen(new File(DEFAULT_IMAGE));
        }

        System.out.println(pelicula.getImagen().getPath());
    }

    private void colocarImagen(File imagen) {
        ImageIcon imageIcon = new ImageIcon(imagen.getPath());
        Image image = imageIcon.getImage().getScaledInstance(240, -1, Image.SCALE_DEFAULT);
        vista.lImagen.setIcon(new ImageIcon(image));
        vista.pack();
    }

    private Pelicula cogerDatosPelicula(){

        String titulo, sinopsis;
        int valoracion;
        float recaudacion;
        File imagen;

        // Verificar datos, con valores por defecto en los casos apropiados
        if (vista.tfTitulo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "El título es necesario", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        titulo = vista.tfTitulo.getText();
        sinopsis = vista.taSinopsis.getText();
        valoracion = vista.tfValoracion.getText().isEmpty() ?
                0 : Integer.parseInt(vista.tfValoracion.getText());
        recaudacion = vista.tfRecaudacion.getText().isEmpty() ?
                0 : Float.parseFloat(vista.tfRecaudacion.getText());
        imagen = imagenSeleccionada != null? imagenSeleccionada : new File(DEFAULT_IMAGE);

        // Construir la pelicula y devolverla
        Pelicula pelicula = new Pelicula();
        pelicula.setTitulo(titulo);
        pelicula.setSinopsis(sinopsis);
        pelicula.setValoracion(valoracion);
        pelicula.setRecaudacion(recaudacion);
        pelicula.setImagen(imagen);
        return pelicula;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        refrescarLista(modelo.getPeliculas(vista.tfBusqueda.getText()));
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        refrescarLista(modelo.getPeliculas(vista.tfBusqueda.getText()));
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
    }


    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
