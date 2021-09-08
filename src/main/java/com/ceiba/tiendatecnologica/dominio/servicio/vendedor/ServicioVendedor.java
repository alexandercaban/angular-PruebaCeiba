package com.ceiba.tiendatecnologica.dominio.servicio.vendedor;

import com.ceiba.tiendatecnologica.dominio.GarantiaExtendida;
import com.ceiba.tiendatecnologica.dominio.Producto;
import com.ceiba.tiendatecnologica.dominio.repositorio.RepositorioGarantiaExtendida;
import com.ceiba.tiendatecnologica.dominio.repositorio.RepositorioProducto;
import com.ceiba.tiendatecnologica.dominio.servicio.garantia.ServicioObtenerGarantia;

import java.util.Calendar;
import java.util.Date;


public class ServicioVendedor {

	public static final String EL_PRODUCTO_TIENE_GARANTIA = "El producto ya cuenta con una garantía extendida";
	public static final String EL_PRODUCTO_NO_CUENTA_CON_GARANTIA_EXTENDIDA = "Este producto no cuenta con una garantía extendida";

	private RepositorioProducto repositorioProducto;
	private RepositorioGarantiaExtendida repositorioGarantia;

	public ServicioVendedor(RepositorioProducto repositorioProducto, RepositorioGarantiaExtendida repositorioGarantia) {
		this.repositorioProducto = repositorioProducto;
		this.repositorioGarantia = repositorioGarantia;
	}

	public void generarGarantia(String codigo, String nombreCliente) {
		try {
			//Se valida que el producto no cuente con Garantia extendida
			if(tieneGarantia(codigo)){
				throw new Exception(EL_PRODUCTO_TIENE_GARANTIA);
			}
			//Se valida que si el codigo del Producto contiene 3 vocales, se ejecute un error controlado
			if(contarVocales(codigo) == 3){
				throw new Exception(EL_PRODUCTO_NO_CUENTA_CON_GARANTIA_EXTENDIDA);
			}

			Producto producto = repositorioProducto.obtenerPorCodigo(codigo);
			GarantiaExtendida garantia = new GarantiaExtendida(producto);

			// Se calcula la Fecha Fin de la Garantía y el Precio de la Garantia extendida, de acuerdo a las condiciones establecidas en la Regla de Negocio 5
			double precioGarantiaExtendida = producto.getPrecio() > 500000 ? producto.getPrecio()*20/100 : producto.getPrecio()*10/100;
			Date fechaFinGarantia = obtenerFechaFinGarantia(producto.getPrecio(), garantia.getFechaSolicitudGarantia());

			// Se almacena la información en la base de datos
			GarantiaExtendida garantiaExtendida = new GarantiaExtendida(garantia.getProducto(), garantia.getFechaSolicitudGarantia(), fechaFinGarantia, precioGarantiaExtendida, nombreCliente);
			this.repositorioGarantia.agregar(garantiaExtendida);
		} catch (Exception e) {
			System.out.printf("Error "+ e);
		}
	}

	public boolean tieneGarantia(String codigo) {
		boolean tieneGarantia = false;
			Producto producto = this.repositorioGarantia.obtenerProductoConGarantiaPorCodigo(codigo);
			if (producto!= null) {
					tieneGarantia = true;
			}
		return tieneGarantia;
	}

	public int contarVocales(String codigo) {
		String vocales="aeiou";
		int contador = 0;
		for(int i=0;i<codigo.length();i++) {
			for(int j=0;j<vocales.length();j++) {
				if(codigo.charAt(i)==vocales.charAt(j)) {
					contador++;
				}
			}
		}
		return contador;
	}

	public Date obtenerFechaFinGarantia(double precioProducto, Date fechaSolicitudGarantia) {
		Date fechaFin;

		Calendar fechaFinGarantia = Calendar.getInstance();
		fechaFinGarantia.setTime(fechaSolicitudGarantia);
		if (precioProducto > 500000) {
				int sumarDiasLunes = 0;
				Calendar fechaSolicitud = Calendar.getInstance();
				fechaSolicitud.setTime(fechaSolicitudGarantia);
			    fechaFinGarantia.add(Calendar.DAY_OF_YEAR, 199);
				while (fechaFinGarantia.after(fechaSolicitud)) {
					if (fechaSolicitud.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY){
						sumarDiasLunes++;
					}
					    fechaSolicitud.add(Calendar.DAY_OF_YEAR, 1);
				}

				fechaFinGarantia.add(Calendar.DAY_OF_YEAR, sumarDiasLunes);

				if (fechaFinGarantia.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
					fechaFinGarantia.add(Calendar.DAY_OF_YEAR, 1);
				}
				fechaFin = fechaFinGarantia.getTime();

		} else {
				fechaFinGarantia.add(Calendar.DAY_OF_YEAR, 99);
				fechaFin = fechaFinGarantia.getTime();
		}
		return fechaFin;
	}

}
