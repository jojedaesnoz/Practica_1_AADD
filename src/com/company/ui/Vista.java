package com.company.ui;

import com.company.base.Pelicula;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;

public class Vista extends JFrame {
    private JPanel panelInformacion, panelBotones;
    private JScrollPane panelPeliculas;
    private DefaultListModel<Pelicula> modeloPeliculas;
    private JList<Pelicula> listaPeliculas;
    private JButton btNuevo, btGuardar, btModificar, btCancelar, btEliminar;
    private JTextField tfTitulo, tfSinopsis, tfValoracion, tfRecaudacion;
    private JLabel lImagen;

    public Vista() {
        prepararComponentes();
        prepararVentana();
    }

    private void prepararComponentes(){
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        btNuevo = new JButton("Nuevo");
        btGuardar = new JButton("Guardar");
        btModificar = new JButton("Modificar");
        btCancelar = new JButton("Cancelar");
        btEliminar = new JButton("Eliminar");

        tfTitulo = new JTextField();
        tfSinopsis = new JTextField();
        tfValoracion = new JTextField();
        tfRecaudacion = new JTextField();
        lImagen = new JLabel();        // TODO: poner imagen por defecto (pulsa para guardar una imagen)


        // Colocar con GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        add(lImagen, gbc);

        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.NONE;
    }

    private void prepararVentana() {
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JPanel panelEntrada(String nombre, JTextField textField) {
        JPanel panel = new JPanel();
        panel.add(new JLabel(nombre));
        panel.add(textField);
        return panel;
    }

    private JPanel crearPanelInformacion() {
        // GridLayout con hueco para las entradas y la imagen
        JTextField[] entradas = {tfTitulo, tfSinopsis, tfValoracion, tfRecaudacion};
        JPanel contenedor = new JPanel(new GridLayout(entradas.length + 1, 1, 10, 10));

        //
        contenedor.add(lImagen);
        for (JTextField textField: entradas) {
            textField = new JTextField();
            contenedor.add(textField);
        }

        // Grid con hueco para meter contenedor y margenes
        JPanel panelInfo = new JPanel(new GridLayout(1, 1));
        Dimension margen = new Dimension(30, 30);
        panelInfo.add(Box.createRigidArea(margen));
        panelInfo.add(contenedor);
        panelInfo.add(Box.createRigidArea(margen));
        return panelInfo;
    }

    private JPanel crearPanelBotones() {
        JPanel botonera = new JPanel();
        JButton[] botones = {btNuevo, btGuardar, btModificar, btCancelar, btEliminar};

        botonera.setLayout(new GridLayout(1, botones.length));
        for (JButton boton: botones)
            botonera.add(boton);

        return botonera;
    }

    private JScrollPane crearPanelPeliculas() {
        modeloPeliculas = new DefaultListModel<>();
        listaPeliculas = new JList<>(modeloPeliculas);
        return new JScrollPane(listaPeliculas);
    }
}
