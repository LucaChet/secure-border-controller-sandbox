package eu.fluidos.orchestrator;

import eu.fluidos.jaxb.ITResourceOrchestrationType;
import eu.fluidos.orchestrator.OrchestratorApplication;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Unmarshaller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.FileInputStream;

public class Main{
    public static void main( String[] args )
    { 
    	try {
            OrchestratorApplication orchestrator = new OrchestratorApplication();
			orchestrator.start(args);
            SpringApplication.run(OrchestratorApplication.class, args);
            System.out.println("Orchestrator REST API server started successfully.");


        } catch (Exception e){
        	System.out.println(e.toString());
            //e.printStackTrace();
        	//System.exit(1);
        }
    }    
}
