package com.ceiba.tiendatecnologica.dominio.unitaria;


import com.ceiba.tiendatecnologica.dominio.GarantiaExtendida;
import com.ceiba.tiendatecnologica.dominio.Producto;
import com.ceiba.tiendatecnologica.dominio.servicio.garantia.ServicioObtenerGarantia;
import com.ceiba.tiendatecnologica.dominio.servicio.vendedor.ServicioVendedor;
import com.ceiba.tiendatecnologica.dominio.repositorio.RepositorioGarantiaExtendida;
import com.ceiba.tiendatecnologica.dominio.repositorio.RepositorioProducto;
import com.ceiba.tiendatecnologica.testdatabuilder.GarantiaTestDataBuilder;
import com.ceiba.tiendatecnologica.testdatabuilder.ProductoTestDataBuilder;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioVendedorTest {

	@Test
	public void productoYaTieneGarantiaTest() {
		
		// arrange
		ProductoTestDataBuilder productoTestDataBuilder = new ProductoTestDataBuilder();
		
		Producto producto = productoTestDataBuilder.build();
		
		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
		
		when(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo())).thenReturn(producto);
		
		ServicioVendedor servicioVendedor = new ServicioVendedor(repositorioProducto, repositorioGarantia );
		
		// act 
		boolean existeProducto = servicioVendedor.tieneGarantia(producto.getCodigo());
		
		//assert
		assertTrue(existeProducto);
	}
	
	@Test
	public void productoNoTieneGarantiaTest() {
		
		// arrange
		ProductoTestDataBuilder productoestDataBuilder = new ProductoTestDataBuilder();
		
		Producto producto = productoestDataBuilder.build();
		
		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
		
		when(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo())).thenReturn(null);
		
		ServicioVendedor servicioVendedor = new ServicioVendedor(repositorioProducto, repositorioGarantia);
		
		// act 
		boolean existeProducto =  servicioVendedor.tieneGarantia(producto.getCodigo());
		
		//assert
		assertFalse(existeProducto);
	}

	@Test
	public void contarVocalesTest(){
		// arrange
		ProductoTestDataBuilder productoestDataBuilder = new ProductoTestDataBuilder();
		Producto producto = productoestDataBuilder.build();

		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
		ServicioVendedor servicioVendedor = new ServicioVendedor(repositorioProducto, repositorioGarantia);

		// act
		int contador = servicioVendedor.contarVocales(producto.getCodigo());

		//assert
		assertNotNull(contador);
	}

	@Test
	public void obtenerFechaFinGarantiaTest(){
		// arrange
		ProductoTestDataBuilder productoestDataBuilder = new ProductoTestDataBuilder();
		Producto producto = productoestDataBuilder.build();

		GarantiaTestDataBuilder garantiaTestDataBuilder = new GarantiaTestDataBuilder();
		GarantiaExtendida garantia  = garantiaTestDataBuilder.build();

		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
		ServicioVendedor servicioVendedor = new ServicioVendedor(repositorioProducto, repositorioGarantia);

		// act
		Date fechaFinGarantia = servicioVendedor.obtenerFechaFinGarantia(producto.getPrecio(), garantia.getFechaSolicitudGarantia());

		//assert
		assertNotNull(fechaFinGarantia);
	}
}
