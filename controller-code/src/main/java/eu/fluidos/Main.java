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
    	//String arg_1 = "./testfile/provider_MSPL_demo.xml";
    	String arg_2 = "./testfile/consumer_MSPL_demo.xml";
		String arg_1 = "./testfile/My_test2.xml";
		String arg_docker_1 = "/app/testfile/My_test2.xml";
    	/*
    	System.out.println(ANSI_PURPLE + "-".repeat(150)+ ANSI_RESET);
    	System.out.println(ANSI_PURPLE + "[DEMO_INFO]  "+ ANSI_YELLOW + "   Harmonization Module" + ANSI_RESET + " has the scope of detecting and correcting all the discordances between consumer and provider intents.");
    	System.out.println("\t\tThe required inputs are:\n\t\t\t\t(1) Request Intents of the CONSUMER\n\t\t\t\t(2) Authorization intents of the PROVIDER\n\t\t\t\t(3) information about the cluster resources.");
    	System.out.println("\t\tThe output will be the harmonized sets of intents, free of all discordances.");
    	System.out.println(ANSI_PURPLE + "-".repeat(150)+ ANSI_RESET);*/
    	// To convert XML files into Java objects
        try {
        	JAXBContext jc = JAXBContext.newInstance("eu.fluidos.jaxb");
        	loggerInfo.debug("Instantiated new JAXB Context");
        	Unmarshaller u =  jc.createUnmarshaller();
        	loggerInfo.debug("Unmarshaller created.");
        	SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        	//Schema sc = sf.newSchema(new File("./xsd/mspl.xsd"));
			Schema sc = sf.newSchema(new File("/app/xsd/mspl.xsd"));
        	u.setSchema(sc);
        	loggerInfo.debug("Added MSPL schema to the unmarshaller.");
        	// User requesting the offloading
        	Object tmp_1 = u.unmarshal(new FileInputStream(arg_docker_1));
        	ITResourceOrchestrationType intents_1 = (ITResourceOrchestrationType) JAXBElement.class.cast(tmp_1).getValue();
        	loggerInfo.debug("Successfull unmarshalling of first input file ["+arg_1+"].");
        	// User offering some resources
        	//Object tmp_2 = u.unmarshal(new FileInputStream(arg_2));
        	//ITResourceOrchestrationType intents_2 = (ITResourceOrchestrationType) JAXBElement.class.cast(tmp_2).getValue(); 
			//Traslator intent_traslation = new Traslator(intents_1);
			//Module module = new Module(intents_1);

			//Qui richiamo il controllore che poi va a richiamare al suo interno il modulo che effettua la traduzione appena un namespace viene offloadato
			KubernetesController controller = new KubernetesController(intents_1);
			controller.start();
			
        	//HarmonizationManager res = new HarmonizationManager(intents_1, intents_2);
        	// //Here output the "Harmonized" set of intents
        	// Scanner scan = new Scanner(System.in);
        	// Marshaller m = jc.createMarshaller();
			// m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			// m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "./xsd/mspl.xsd");
			// QName qName = new QName("eu.fluidos.jaxb.ITResourceOrchestrationType", "ITResourceOrchestrationType");
			// JAXBElement<ITResourceOrchestrationType> root = new JAXBElement<>(qName, ITResourceOrchestrationType.class, res.getConsumerIntents());
	    	// System.out.println(ANSI_PURPLE + "-".repeat(150)+ ANSI_RESET);
	    	// System.out.println(ANSI_PURPLE + "[DEMO_INFO]  "+ ANSI_RESET + "Press ENTER to output " + ANSI_YELLOW + "Consumer" + ANSI_RESET + " MSPL schema...");
			// scan.nextLine();
			// loggerInfo.info("[harmonization] Consumer MSPL OUTPUT");
			// StringWriter stringWriter = new StringWriter();
			// m.marshal(root, stringWriter);
			// loggerInfo.info("\n" + stringWriter.toString());
	    	// System.out.println(ANSI_PURPLE + "-".repeat(150)+ ANSI_RESET);
	    	// System.out.println(ANSI_PURPLE + "[DEMO_INFO]  "+ ANSI_RESET + "Press ENTER to output " + ANSI_YELLOW + "Provider" + ANSI_RESET + " MSPL schema...");
			// scan.nextLine();
			// root = new JAXBElement<>(qName, ITResourceOrchestrationType.class, res.getProviderIntents());
			// loggerInfo.info(" [harmonization] Provider MSPL OUTPUT");
			// StringWriter stringWriter_2 = new StringWriter();
			// m.marshal(root, stringWriter_2);
			// loggerInfo.info("\n" + stringWriter_2.toString());


        	
        	
        } catch (Exception e){
        	System.out.println(e.toString());
        	System.exit(1);
        }
    }
    
    
    
}
