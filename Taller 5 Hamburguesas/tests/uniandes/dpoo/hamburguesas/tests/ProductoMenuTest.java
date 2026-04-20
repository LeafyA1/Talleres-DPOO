package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ProductoMenuTest
{
    private ProductoMenu producto1;
    private ProductoMenu producto2;

    @BeforeEach
    void setUp( ) throws Exception
    {
        producto1 = new ProductoMenu( "Hamburguesa Corral", 14000 );
        producto2 = new ProductoMenu( "Papas Medianas", 5500 );
    }

    @AfterEach
    void tearDown( ) throws Exception
    {
    }

    @Test
    void testGetNombre( )
    {
        assertEquals( "Hamburguesa Corral", producto1.getNombre( ) );
    }

    @Test
    void testGetNombreOtroProducto( )
    {
        assertEquals( "Papas Medianas", producto2.getNombre( ) );
    }

    @Test
    void testGetPrecio( )
    {
        assertEquals( 14000, producto1.getPrecio( ) );
    }

    @Test
    void testGetPrecioOtroProducto( )
    {
        assertEquals( 5500, producto2.getPrecio( ) );
    }

    @Test
    void testGenerarTextoFacturaContieneNombre( )
    {
        String factura = producto1.generarTextoFactura( );
        assertTrue( factura.contains( "Hamburguesa Corral" ) );
    }

    @Test
    void testGenerarTextoFacturaContienePrecio( )
    {
        String factura = producto1.generarTextoFactura( );
        assertTrue( factura.contains( "14000" ) );
    }

    @Test
    void testGenerarTextoFacturaFormatoCompleto( )
    {
        String factura = producto1.generarTextoFactura( );
        String esperado = "Hamburguesa Corral\n            14000\n";
        assertEquals( esperado, factura );
    }
}
