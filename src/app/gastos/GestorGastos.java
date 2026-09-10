package app.gastos;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class GestorGastos {

	List<Gasto> listaGastos = new ArrayList<>();
	List<String> categorias = new ArrayList<>();
	private final String ARCHIVO = "gastos.txt";
	
	
	public void agregarGasto(Gasto nuevoGasto) {
		if( nuevoGasto!= null) {
			this.listaGastos.add(nuevoGasto);
			guardarEnArchivo();
		}
	}
	//METODO PARA GUARDAR LOS DATOS EN UN ARCHIVO AL DARLE GUARDAR GASTO 
	public void guardarEnArchivo() {
		try( BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO))) {
			for(Gasto g : listaGastos) {
				writer.write(g.getMonto() + ", " + g.getCategoria() + ", " + g.getDescripcion());
				writer.newLine();
			}
		}catch(IOException e ) {
			System.out.println("Error al guardar el archivo" + e.getMessage());
		}
	}
	
	public void cargarArchivo() {
		File file = new File(ARCHIVO);
		if(!file.exists()) return;
		
		try( BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String linea;
			
			
			
			while (( linea = reader.readLine()) != null) {
				String [] datos = linea.split(",");
				if(datos.length == 3) {
					double monto = Double.parseDouble(datos[0]);
					String categoria = datos[1];
					String descripcion = datos[2];
					
					listaGastos.add(new Gasto(monto,categoria,descripcion));
				}
			}
		}catch (IOException e ) {
			System.out.println("Error al cargar archivo: " + e.getMessage());
		}
		
	}
	
	public void eliminarGasto( int indice ) {
		listaGastos.remove(indice);
		
		guardarEnArchivo();
		
	}
		
	public double TotalGasto() {
		double gasto = 0;
		for(Gasto d : listaGastos) {
			gasto += d.getMonto();
		}
		return gasto;
		
	}
	
	
	
	public void filtrarCategoria(List<String> categoria) {
		Collections.sort(categorias);
	for(String c : categorias) {
		System.out.println(c);
	}
	}
	public List<Gasto> getListaGastos(){
		return listaGastos;
	}
}


