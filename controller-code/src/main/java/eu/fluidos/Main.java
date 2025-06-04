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
    	String arg_2 = "./testfile/consumer_MSPL_demo.xml";
		String arg_1 = "./testfile/My_test2.xml";
		String arg_docker_1 = "/app/testfile/My_test2.xml";
    	try {
        	JAXBContext jc = JAXBContext.newInstance("eu.fluidos.jaxb");
        	loggerInfo.debug("Instantiated new JAXB Context");
        	Unmarshaller u =  jc.createUnmarshaller();
        	loggerInfo.debug("Unmarshaller created.");
        	SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        	Schema sc = sf.newSchema(new File("/app/xsd/mspl.xsd"));
        	u.setSchema(sc);
        	loggerInfo.debug("Added MSPL schema to the unmarshaller.");
        	Object tmp_1 = u.unmarshal(new FileInputStream(arg_docker_1));
        	ITResourceOrchestrationType intents_1 = (ITResourceOrchestrationType) JAXBElement.class.cast(tmp_1).getValue();
        	loggerInfo.debug("Successfull unmarshalling of first input file ["+arg_1+"].");
        	KubernetesController controller = new KubernetesController(intents_1);
			controller.start();

        } catch (Exception e){
        	System.out.println(e.toString());
        	System.exit(1);
        }
    }    
}
