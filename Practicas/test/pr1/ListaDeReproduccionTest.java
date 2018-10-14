package pr1;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pr01.ListaDeReproduccion;

public class ListaDeReproduccionTest {
	
	private ListaDeReproduccion lr1;  
	private ListaDeReproduccion lr2; 
	private final File FIC_TEST1 = new File( "test/res/No del grupo.mp4" ); 
	private final File FIC_TEST2 = new File( "" );

	@Before  
	public void setUp() throws Exception {   
		lr1 = new ListaDeReproduccion();   
		lr2 = new ListaDeReproduccion();   
		lr2.add( FIC_TEST1 );  
	} 
	 
	@After      
	public void tearDown() {   
		lr2.clear();  
	} 
	
	// Chequeo de funcionamiento correcto de get(índice)  
	@Test 
	public void testGet() {   
		assertEquals( FIC_TEST1, lr2.getFic(0) );  // El único dato es el fic-test1  
	}
	
	//Chequeo de funcionamiento correcto de intercambiar
	@Test
	public void testIntercambio() {
		ListaDeReproduccion lr3 = new ListaDeReproduccion();
		lr3.add(FIC_TEST2);
		lr3.add(FIC_TEST1);
		lr2.add(FIC_TEST2);
		lr2.intercambia(0, 1);
		assertEquals(lr3.getFic(1), lr2.getFic(1));
		
	}
	
	//Chequeo del añadir
	@Test
	public void testAñadir() {
		lr2.add(FIC_TEST2);
		assertEquals(FIC_TEST2, lr2.getFic(1));
	}
	
	//Chequeo del borrar
	@Test
	public void testRemove() {
		lr2.add(FIC_TEST2);
		lr2.removeFic(0);
		assertEquals(FIC_TEST2, lr2.getFic(0));
	}
	
	//Chequeo del size 
	@Test
	public void testSize() {
		assertEquals( 0, lr1.getSize());
	}
	
	//Test del método add
	@Test 
	public void addCarpeta() {   
		String carpetaTest = "test/res/";   
		String filtroTest = "*Pentatonix*.mp4"; 
		ListaDeReproduccion lr = new ListaDeReproduccion();   
		lr.add( carpetaTest, filtroTest );   
		fail( "Método sin acabar" );  
		
	}
	
	//Test Repro Aleatoria
	@Test
	public void testIrARandom() {
		assertEquals(false, lr1.irARandom());
		lr1.add(FIC_TEST1);
		assertEquals(true, lr1.irARandom());
		lr1.add(FIC_TEST2);
		assertEquals(true, lr1.irARandom());
	}
	
	
	

}
