package app.gastos;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class FormularioBasicoSwing extends JFrame{
	
	private GestorGastos gestor = new GestorGastos();
	
	
	//TABLA DE DATOS
	private DefaultTableModel modeloTabla;
	private JTable tablaGastos;
	
	  
	public FormularioBasicoSwing() {
		super("Registro de Datos");
		setLayout(new FlowLayout());
		
		
		//CARGAR LOS DATOS GUARDADOS
		gestor.cargarArchivo();
		
		
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
		
		
		//JTABLE
		String[]columnas = {"Monto", "Categoria", "Descripcion"};
		modeloTabla = new DefaultTableModel(columnas,0);
		tablaGastos = new JTable ( modeloTabla);
		
		//SCROLL TABLA
		JScrollPane scrollTabla = new JScrollPane(tablaGastos);
		scrollTabla.setPreferredSize(new Dimension(300,300));
		
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
					
					
					
					Gasto nuevoGasto = new Gasto(montoRedondeado,categoria,descripcion);
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
			add(labelMonto);
			add(txtMonto);
			add(labelCategoria);
			add(comboCategoria);
			add(labelDescrip);
			add(txtDescrip);
			add(botonEnviar);
			add(labelTotal);
			add(scrollTabla);
			add(botonEliminar);
			
			

			setSize(340,500);
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
					Object []fila = {montoFormateado,g.getCategoria(),g.getDescripcion()};
					modeloTabla.addRow(fila);
					
				}
			
	
			
		
	}
	  
}
