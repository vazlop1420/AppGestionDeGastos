package app.gastos;

import javax.swing.UIManager;

import com.formdev.flatlaf.FlatDarkLaf;

public class Main {

	public static void main(String[]args) {
	//ESTILO OS WINDOWS
	try {
		FlatDarkLaf.setup();
	}catch (Exception e) {
		e.printStackTrace();
	}
		

	//EJECUTA LA INTERFAZ 
	javax.swing.SwingUtilities.invokeLater(new Runnable() {
		@Override
		public void run() {
			new FormularioBasicoSwing();
			}
		});
	
	}
}