package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.PrintWriter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uniandes.dpoo.hamburguesas.excepciones.NoHayPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.excepciones.YaHayUnPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.mundo.Restaurante;

public class RestauranteTest
{
    private Restaurante restaurante;
    private File archivoIngredientes;
    private File archivoMenu;
    private File archivoCombos;
    private File carpetaFacturas;

    @BeforeEach
    void setUp( ) throws Exception
    {
        restaurante = new Restaurante( );

        archivoIngredientes = File.createTempFile( "test_ingredientes", ".txt" );
        archivoMenu = File.createTempFile( "test_menu", ".txt" );
        archivoCombos = File.createTempFile( "test_combos", ".txt" );

        PrintWriter pw = new PrintWriter( archivoIngredientes );
        pw.println( "tomate;1000" );
        pw.println( "queso mozzarella;2500" );
        pw.println( "lechuga;1000" );
        pw.close( );

        pw = new PrintWriter( archivoMenu );
        pw.println( "corral;14000" );
        pw.println( "papas medianas;5500" );
        pw.println( "gaseosa;5000" );
        pw.close( );

        pw = new PrintWriter( archivoCombos );
        pw.println( "combo corral;10%;corral;papas medianas;gaseosa" );
        pw.close( );

        carpetaFacturas = new File( "./facturas/" );
        carpetaFacturas.mkdirs( );
    }

    @AfterEach
    void tearDown( ) throws Exception
    {
        archivoIngredientes.delete( );
        archivoMenu.delete( );
        archivoCombos.delete( );

        if( carpetaFacturas.exists( ) && carpetaFacturas.listFiles( ) != null )
        {
            for( File archivo : carpetaFacturas.listFiles( ) )
            {
                archivo.delete( );
            }
            carpetaFacturas.delete( );
        }
    }

    @Test
    void testCargarIngredientesCargaBien( ) throws Exception
    {
        restaurante.cargarInformacionRestaurante( archivoIngredientes, archivoMenu, archivoCombos );
        assertEquals( 3, restaurante.getIngredientes( ).size( ) );
    }

    @Test
    void testCargarMenuCargaBien( ) throws Exception
    {
        restaurante.cargarInformacionRestaurante( archivoIngredientes, archivoMenu, archivoCombos );
        assertEquals( 3, restaurante.getMenuBase( ).size( ) );
    }

    @Test
    void testCargarCombosCargaBien( ) throws Exception
    {
        restaurante.cargarInformacionRestaurante( archivoIngredientes, archivoMenu, archivoCombos );
        assertEquals( 1, restaurante.getMenuCombos( ).size( ) );
    }

    @Test
    void testCargarNombresIngredientes( ) throws Exception
    {
        restaurante.cargarInformacionRestaurante( archivoIngredientes, archivoMenu, archivoCombos );
        assertEquals( "tomate", restaurante.getIngredientes( ).get( 0 ).getNombre( ) );
    }

    @Test
    void testCargarPreciosMenu( ) throws Exception
    {
        restaurante.cargarInformacionRestaurante( archivoIngredientes, archivoMenu, archivoCombos );
        assertEquals( 14000, restaurante.getMenuBase( ).get( 0 ).getPrecio( ) );
    }

    @Test
    void testIniciarPedidoPrimeroPedidoEnCursoEsNull( )
    {
        assertNull( restaurante.getPedidoEnCurso( ) );
    }

    @Test
    void testIniciarPedidoCreaElPedido( ) throws Exception
    {
        restaurante.iniciarPedido( "Juan Perez", "Calle 10" );
        assertNotNull( restaurante.getPedidoEnCurso( ) );
    }

    @Test
    void testIniciarPedidoNombreCliente( ) throws Exception
    {
        restaurante.iniciarPedido( "Juan Perez", "Calle 10" );
        assertEquals( "Juan Perez", restaurante.getPedidoEnCurso( ).getNombreCliente( ) );
    }

    @Test
    void testIniciarPedidoCuandoYaHayUnoLanzaExcepcion( ) throws Exception
    {
        restaurante.iniciarPedido( "Juan Perez", "Calle 10" );
        assertThrows( YaHayUnPedidoEnCursoException.class,
                ( ) -> restaurante.iniciarPedido( "Maria Lopez", "Carrera 7" ) );
    }

    @Test
    void testCerrarPedidoSinPedidoEnCursoLanzaExcepcion( )
    {
        assertThrows( NoHayPedidoEnCursoException.class,
                ( ) -> restaurante.cerrarYGuardarPedido( ) );
    }

    @Test
    void testCerrarPedidoPedidoEnCursoVuelveANull( ) throws Exception
    {
        restaurante.iniciarPedido( "Juan Perez", "Calle 10" );
        restaurante.cerrarYGuardarPedido( );
        assertNull( restaurante.getPedidoEnCurso( ) );
    }

    @Test
    void testCerrarPedidoAgregaAlHistorico( ) throws Exception
    {
        restaurante.iniciarPedido( "Juan Perez", "Calle 10" );
        restaurante.cerrarYGuardarPedido( );
        assertEquals( 1, restaurante.getPedidos( ).size( ) );
    }

    @Test
    void testDosConCerradosAmbosEnHistorico( ) throws Exception
    {
        restaurante.iniciarPedido( "Juan Perez", "Calle 10" );
        restaurante.cerrarYGuardarPedido( );

        restaurante.iniciarPedido( "Maria Lopez", "Carrera 7" );
        restaurante.cerrarYGuardarPedido( );

        assertEquals( 2, restaurante.getPedidos( ).size( ) );
    }

    @Test
    void testCerrarPedidoCreaArchivoFactura( ) throws Exception
    {
        restaurante.iniciarPedido( "Juan Perez", "Calle 10" );
        int idPedido = restaurante.getPedidoEnCurso( ).getIdPedido( );
        restaurante.cerrarYGuardarPedido( );

        File archivoFactura = new File( "./facturas/factura_" + idPedido + ".txt" );
        assertTrue( archivoFactura.exists( ) );
    }

    @Test
    void testGetMenuBaseVacioAlInicio( )
    {
        assertEquals( 0, restaurante.getMenuBase( ).size( ) );
    }

    @Test
    void testGetMenuCombosVacioAlInicio( )
    {
        assertEquals( 0, restaurante.getMenuCombos( ).size( ) );
    }

    @Test
    void testGetIngredientesVacioAlInicio( )
    {
        assertEquals( 0, restaurante.getIngredientes( ).size( ) );
    }
}
