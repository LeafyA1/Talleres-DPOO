package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ComboTest
{
    private ProductoMenu hamburguesa;
    private ProductoMenu papas;
    private ProductoMenu gaseosa;
    private ArrayList<ProductoMenu> itemsCombo;
    private Combo comboCorral;
    private Combo comboEspecial;
    private Combo comboSinDescuento;

    @BeforeEach
    void setUp( ) throws Exception
    {
        hamburguesa = new ProductoMenu( "corral", 14000 );
        papas = new ProductoMenu( "papas medianas", 5500 );
        gaseosa = new ProductoMenu( "gaseosa", 5000 );

        itemsCombo = new ArrayList<ProductoMenu>( );
        itemsCombo.add( hamburguesa );
        itemsCombo.add( papas );
        itemsCombo.add( gaseosa );

        comboCorral = new Combo( "Combo Corral", 0.10, itemsCombo );
        comboEspecial = new Combo( "Combo Especial", 0.095, itemsCombo );
        comboSinDescuento = new Combo( "Combo Normal", 0.0, itemsCombo );
    }

    @AfterEach
    void tearDown( ) throws Exception
    {
    }

    @Test
    void testGetNombre( )
    {
        assertEquals( "Combo Corral", comboCorral.getNombre( ) );
    }

    @Test
    void testGetNombreOtroCombo( )
    {
        assertEquals( "Combo Especial", comboEspecial.getNombre( ) );
    }

    @Test
    void testGetPrecioConDescuentoDiez( )
    {
        assertEquals( 22050, comboCorral.getPrecio( ) );
    }

    @Test
    void testGetPrecioConDescuentoCasINueve( )
    {
        int precioEsperado = (int)( 24500 * ( 1 - 0.095 ) );
        assertEquals( precioEsperado, comboEspecial.getPrecio( ) );
    }

    @Test
    void testGetPrecioSinDescuento( )
    {
        assertEquals( 24500, comboSinDescuento.getPrecio( ) );
    }

    @Test
    void testGenerarTextoFacturaContieneNombre( )
    {
        String factura = comboCorral.generarTextoFactura( );
        assertTrue( factura.contains( "Combo Corral" ) );
    }

    @Test
    void testGenerarTextoFacturaContieneDescuento( )
    {
        String factura = comboCorral.generarTextoFactura( );
        assertTrue( factura.contains( "0.1" ) );
    }

    @Test
    void testGenerarTextoFacturaContienePrecio( )
    {
        String factura = comboCorral.generarTextoFactura( );
        assertTrue( factura.contains( "22050" ) );
    }

    @Test
    void testGenerarTextoFacturaFormatoCompleto( )
    {
        String factura = comboCorral.generarTextoFactura( );
        String esperado = "Combo Combo Corral\n Descuento: 0.1\n            22050\n";
        assertEquals( esperado, factura );
    }
}
