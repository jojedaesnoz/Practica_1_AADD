package com.company.ui;

import com.company.base.Pelicula;

import javax.swing.*;
import java.awt.*;

public class Vista extends JFrame {
    private JPanel panelInformacion, panelBotones;
    private JScrollPane panelPeliculas;
    private DefaultListModel<Pelicula> modeloPeliculas;
    private JList<Pelicula> listaPeliculas;
    private JButton btNuevo, btGuardar, btModificar, btCancelar, btEliminar;
    private JTextField tfTitulo, tfValoracion, tfRecaudacion;
    private JTextArea taSinopsis;
    private JLabel lImagen;

    public Vista() {
        inicializarComponentes();
        colocarComponentes();
        crearPanelBotones();
        prepararVentana();
    }

    private void inicializarComponentes() {
        tfTitulo = new JTextField();
        taSinopsis = new JTextArea();
        tfValoracion = new JTextField();
        tfRecaudacion = new JTextField();
        lImagen = new JLabel("IMAGEN");

        btNuevo = new JButton("Nuevo");
        btGuardar = new JButton("Guardar");
        btModificar = new JButton("Modificar");
        btCancelar = new JButton("Cancelar");
        btEliminar = new JButton("Eliminar");
    }

    private void colocarComponentes(){
        JPanel contenedor = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // COLOCAR

        JLabel lTitulo = new JLabel("Título", SwingConstants.CENTER);
        JLabel lSinopsis = new JLabel("Sinopsis", SwingConstants.CENTER);
        JLabel lValoracion = new JLabel("Valoración", SwingConstants.CENTER);
        JLabel lRecaudacion = new JLabel("Recaudación", SwingConstants.CENTER);

        // DAR TAMS Y BORDES
        for (JLabel label: new JLabel[]{lTitulo, lSinopsis, lValoracion, lRecaudacion}){
            label.setPreferredSize(new Dimension(80, 30));
        }
        for (JComponent entrada: new JComponent[]{tfTitulo, tfValoracion, tfRecaudacion}) {
            entrada.setPreferredSize(new Dimension(150, 25));
            entrada.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        }
        taSinopsis.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // IMAGEN
        lImagen.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        contenedor.add(lImagen, gbc);
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;

        // ETIQUETAS
        gbc.gridx = 0;
        gbc.gridy = 1;
        contenedor.add(lTitulo, gbc);

        gbc.gridy++;
        gbc.anchor = GridBagConstraints.NORTH;
        contenedor.add(lSinopsis, gbc);
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridy++;
        contenedor.add(lValoracion, gbc);

        gbc.gridy++;
        contenedor.add(lRecaudacion, gbc);

        // Poner entradas
        gbc.gridx = 1;
        gbc.gridy = 1;
        contenedor.add(tfTitulo, gbc);

        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy++;
        contenedor.add(taSinopsis, gbc);
        gbc.weighty = 0;

        gbc.fill = GridBagConstraints.NONE;
        gbc.gridy++;
        contenedor.add(tfValoracion, gbc);

        gbc.gridy++;
        contenedor.add(tfRecaudacion, gbc);

        // LISTA
        add(contenedor);
        JScrollPane panelPeliculas = crearPanelPeliculas();
        gbc.weightx = 1;
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 5;
        gbc.fill = GridBagConstraints.BOTH;
        contenedor.add(panelPeliculas, gbc);
    }

    private void prepararVentana() {
        setSize(550, 300);
        setMinimumSize(new Dimension(550, 300));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void crearPanelBotones() {

        JPanel botonera = new JPanel();
        JButton[] botones = {btNuevo, btGuardar, btModificar, btCancelar, btEliminar};

        botonera.setLayout(new GridLayout(1, botones.length));
        for (JButton boton: botones)
            botonera.add(boton);

        add(botonera, BorderLayout.SOUTH);
    }

    private JScrollPane crearPanelPeliculas() {
        modeloPeliculas = new DefaultListModel<>();
        listaPeliculas = new JList<>(modeloPeliculas);
        return new JScrollPane(listaPeliculas);
    }
}
