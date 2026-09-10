package app.gastos;


	public class Gasto {
	private double monto;
	private String categoria;
	private String descripcion;
	
	


	public String getCategoria() {
		return categoria;
	}



	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}



	public String getDescripcion() {
		return descripcion;
	}



	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public double getMonto() {
		return monto;
	}



	public void setMonto(double m) {
		if(m > 0 ) {
			this.monto = m;
		}
		else {
			throw new RuntimeException("El valor tiene que ser mayor a 0");
		}
	}



	public Gasto(double monto, String categoria, String descripcion) {
		this.monto= monto;
		this.categoria = categoria;
		this.descripcion = descripcion;
		
	}


	}


