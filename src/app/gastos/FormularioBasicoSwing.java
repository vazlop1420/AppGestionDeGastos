package app.gastos;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;




public class FormularioBasicoSwing extends JFrame{
	
	private GestorGastos gestor = new GestorGastos();
	
	
	//TABLA DE DATOS
	private DefaultTableModel modeloTabla;
	private JTable tablaGastos;
	
	  
	public FormularioBasicoSwing() {
		super("App Gestor de Gastos");
		
		
		
		//CARGAR LOS DATOS GUARDADOS
		gestor.cargarArchivo();
		
		//ESQUELETO DE LA VENTANA
		setLayout(new BorderLayout());
		
		//COMPONENTES MONTO
		JLabel labelMonto = new JLabel("Monto ($");
		JTextField txtMonto = new JTextField(8);
		
		//COMPONENTES CATEGORIA
		JLabel labelCategoria = new JLabel("Categoria:");
		String[] opcionesCategorias = {"Comida","Transporte","Ocio","Universidad"};
		JComboBox<String> comboCategoria = new JComboBox<>(opcionesCategorias);
		
		//COMPONENTES DESCRIPCION
		JLabel labelDescrip = new JLabel("Descripcion");
		JTextField txtDescrip = new JTextField(15);
		
		//BOTONES 
		JButton botonEnviar = new JButton("Guardar Gasto");
		
		JButton botonEliminar = new JButton("Eliminar Gasto");
		
		//ETIQUETA
		JLabel labelTotal = new JLabel();
		labelTotal.setText(String.format("Total Gastado : $ %.2f", gestor.TotalGasto()));
		
		
		
		//PARTE DE ARRIBA 
		
		JPanel panelFormulario = new JPanel(new GridLayout(4,2,8,8));
		panelFormulario.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(	10, 10, 10, 10)));
		
		panelFormulario.add(labelMonto);
        panelFormulario.add(txtMonto);
        panelFormulario.add(labelCategoria);
        panelFormulario.add(comboCategoria);
        panelFormulario.add(labelDescrip);
        panelFormulario.add(txtDescrip);
        panelFormulario.add(new JLabel("")); // Espacio vacío para alinear el botón
        panelFormulario.add(botonEnviar);

        add(panelFormulario, BorderLayout.NORTH);
		
		//CENTRO DEL PANEL
		//JTABLE
        String[] columnas = {"Monto", "Categoría", "Descripción", "Fecha"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Buena práctica UX: evitar edición directa
            }
        };

        tablaGastos = new JTable(modeloTabla);
        tablaGastos.setRowHeight(25);
        tablaGastos.setFont(new Font("SansSerif", Font.PLAIN, 12));

        // Alineación a la derecha para la columna del Monto
        DefaultTableCellRenderer alineacionDerecha = new DefaultTableCellRenderer();
        alineacionDerecha.setHorizontalAlignment(SwingConstants.RIGHT);
        tablaGastos.getColumnModel().getColumn(0).setCellRenderer(alineacionDerecha);

        JScrollPane scrollTabla = new JScrollPane(tablaGastos);
        scrollTabla.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(scrollTabla, BorderLayout.CENTER);

        // -------------------------------------------------------------
        // PANEL INFERIOR (SOUTH): Acciones de la tabla y Total
        // -------------------------------------------------------------
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelInferior.add(botonEliminar, BorderLayout.WEST);
        panelInferior.add(labelTotal, BorderLayout.EAST);

        add(panelInferior, BorderLayout.SOUTH);

        actualizarTabla();
		
		//CLICK "Borrar Gasto"
		botonEliminar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int filaSeleccionada = tablaGastos.getSelectedRow();
					gestor.eliminarGasto(filaSeleccionada);
					actualizarTabla();
					
					labelTotal.setText("Total Gastado:" + gestor.TotalGasto());
					
				} catch ( RuntimeException er) {
					JOptionPane.showMessageDialog(null, "Selecciona una fila valida,", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		//CLICK "Guardar Gasto"
		botonEnviar.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					double monto = Double.parseDouble(txtMonto.getText());
					String categoria = (String) comboCategoria.getSelectedItem();
					String descripcion = txtDescrip.getText();
					
					double montoRedondeado = Math.round((monto * 100.0)/100);
					
					
					
					Gasto nuevoGasto = new Gasto(montoRedondeado,categoria,descripcion, LocalDate.now());
					gestor.agregarGasto(nuevoGasto);
					actualizarTabla();
					
					
					labelTotal.setText("Total Gastado:" + gestor.TotalGasto());
					
					txtMonto.setText("");
					txtDescrip.setText("");
					
					
					}catch (NumberFormatException ex){
					JOptionPane.showMessageDialog(null, "Ingresa un valor valido en el monto", "Error", JOptionPane.ERROR_MESSAGE);
					
				}catch (IllegalArgumentException ex ) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
			
			
			

			setSize(400,500);
			setLocationRelativeTo(null);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setVisible(true);
			
			((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
			tablaGastos.setRowHeight(25);
			tablaGastos.setFont(new Font("SanSerif", Font.PLAIN, 12));
			
	}
			
			private void actualizarTabla() {
				modeloTabla.setRowCount(0);
				
				for(Gasto g : gestor.getListaGastos()) {
					
					
					String montoFormateado = String.format("$%.2f", g.getMonto());
					Object []fila = {montoFormateado,g.getCategoria(),g.getDescripcion(), g.getDate()};
					modeloTabla.addRow(fila);
					
				}
			
	
			
		
	}
	  
}
