package eu.fluidos;

import eu.fluidos.Controller.KubernetesController;
import eu.fluidos.jaxb.ITResourceOrchestrationType;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Unmarshaller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.FileInputStream;
public class Main 
{
	public static Logger loggerInfo = LogManager.getLogger(Main.class);
	

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	
    public static void main( String[] args )
    { 
    	try {
        	KubernetesController controller = new KubernetesController();
			controller.start();

        } catch (Exception e){
        	System.out.println(e.toString());
        	System.exit(1);
        }
    }    
}
