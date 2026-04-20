package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.nio.file.Files;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;
import java.util.ArrayList;

public class PedidoTest
{
    private Pedido pedido1;
    private Pedido pedidoConProductos;
    private ProductoMenu hamburguesa;
    private ProductoMenu papas;
    private Combo combo;
    private File carpetaFacturas;

    @BeforeEach
    void setUp( ) throws Exception
    {
        pedido1 = new Pedido( "Juan Perez", "Calle 10 # 5-20" );

        hamburguesa = new ProductoMenu( "Hamburguesa Corral", 14000 );
        papas = new ProductoMenu( "Papas Medianas", 5500 );

        ArrayList<ProductoMenu> items = new ArrayList<>( );
        items.add( hamburguesa );
        items.add( papas );
        combo = new Combo( "Combo Corral", 0.10, items );

        pedidoConProductos = new Pedido( "Maria Lopez", "Carrera 7 # 12-34" );
        pedidoConProductos.agregarProducto( hamburguesa );
        pedidoConProductos.agregarProducto( papas );

        carpetaFacturas = new File( "./facturas_prueba/" );
        carpetaFacturas.mkdirs( );
    }

    @AfterEach
    void tearDown( ) throws Exception
    {
        if( carpetaFacturas.exists( ) )
        {
            for( File archivo : carpetaFacturas.listFiles( ) )
            {
                archivo.delete( );
            }
            carpetaFacturas.delete( );
        }
    }

    @Test
    void testGetIdPedidoEsNumeroEntero( )
    {
        assertTrue( pedido1.getIdPedido( ) >= 0 );
    }

    @Test
    void testGetIdPedidosDiferentes( )
    {
        Pedido otroPedido = new Pedido( "Otro Cliente", "Otra Direccion" );
        assertNotEquals( pedido1.getIdPedido( ), otroPedido.getIdPedido( ) );
    }

    @Test
    void testGetNombreCliente( )
    {
        assertEquals( "Juan Perez", pedido1.getNombreCliente( ) );
    }

    @Test
    void testGetPrecioTotalPedidoVacio( )
    {
        assertEquals( 0, pedido1.getPrecioTotalPedido( ) );
    }

    @Test
    void testGetPrecioTotalConProductos( )
    {
        int precioNeto = 14000 + 5500;
        int iva = (int)( precioNeto * 0.19 );
        int total = precioNeto + iva;
        assertEquals( total, pedidoConProductos.getPrecioTotalPedido( ) );
    }

    @Test
    void testAgregarProductoCombo( )
    {
        Pedido pedidoConCombo = new Pedido( "Cliente Combo", "Direccion Combo" );
        pedidoConCombo.agregarProducto( combo );

        int precioNeto = combo.getPrecio( );
        int iva = (int)( precioNeto * 0.19 );
        int total = precioNeto + iva;
        assertEquals( total, pedidoConCombo.getPrecioTotalPedido( ) );
    }

    @Test
    void testGenerarTextoFacturaContieneNombreCliente( )
    {
        String factura = pedido1.generarTextoFactura( );
        assertTrue( factura.contains( "Juan Perez" ) );
    }

    @Test
    void testGenerarTextoFacturaContieneDireccion( )
    {
        String factura = pedido1.generarTextoFactura( );
        assertTrue( factura.contains( "Calle 10 # 5-20" ) );
    }

    @Test
    void testGenerarTextoFacturaContieneProductos( )
    {
        String factura = pedidoConProductos.generarTextoFactura( );
        assertTrue( factura.contains( "Hamburguesa Corral" ) );
        assertTrue( factura.contains( "Papas Medianas" ) );
    }

    @Test
    void testGenerarTextoFacturaContienePrecioNeto( )
    {
        String factura = pedidoConProductos.generarTextoFactura( );
        assertTrue( factura.contains( "Precio Neto" ) );
        assertTrue( factura.contains( "19500" ) );
    }

    @Test
    void testGenerarTextoFacturaContieneIVA( )
    {
        String factura = pedidoConProductos.generarTextoFactura( );
        assertTrue( factura.contains( "IVA" ) );
        assertTrue( factura.contains( "3705" ) );
    }

    @Test
    void testGenerarTextoFacturaContienePrecioTotal( )
    {
        String factura = pedidoConProductos.generarTextoFactura( );
        assertTrue( factura.contains( "Precio Total" ) );
        assertTrue( factura.contains( "23205" ) );
    }

    @Test
    void testGuardarFacturaCreaArchivo( ) throws Exception
    {
        File archivoFactura = new File( carpetaFacturas, "factura_prueba.txt" );
        pedidoConProductos.guardarFactura( archivoFactura );
        assertTrue( archivoFactura.exists( ) );
    }

    @Test
    void testGuardarFacturaContenidoCorrecto( ) throws Exception
    {
        File archivoFactura = new File( carpetaFacturas, "factura_contenido.txt" );
        pedidoConProductos.guardarFactura( archivoFactura );

        String contenidoArchivo = new String( Files.readAllBytes( archivoFactura.toPath( ) ) );
        assertEquals( pedidoConProductos.generarTextoFactura( ), contenidoArchivo );
    }
}
