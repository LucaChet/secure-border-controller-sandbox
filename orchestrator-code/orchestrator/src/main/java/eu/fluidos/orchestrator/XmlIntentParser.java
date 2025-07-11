package eu.fluidos.orchestrator;

import eu.fluidos.jaxb.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;


public class XmlIntentParser {

    public static ITResourceOrchestrationType extractIntentsFromXMLFile(String file) {
        ITResourceOrchestrationType intent = null;
        
        try {
            JAXBContext jc = JAXBContext.newInstance("eu.fluidos.jaxb");
            Unmarshaller u = jc.createUnmarshaller();
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            // change the path to the XSD file (/app/xsd/mspl.xsd) once containerized
            //Schema sc = sf.newSchema(new File("./xsd/mspl.xsd"));
            Schema sc = sf.newSchema(new File("/app/xsd/mspl.xsd"));
            u.setSchema(sc);
            Object unmsarshalObject = u.unmarshal(new FileInputStream(file));
            intent = (ITResourceOrchestrationType) ((JAXBElement<?>) unmsarshalObject).getValue();
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("Error while unmarshalling the XML file: " + file);
        }

        return intent;
    }

    /**
     * Function used to extract the AuthorizationIntents from a given ITResourceOrchestrationType.
     * @param intent is the ITResourceOrchestrationType from which the intents are extracted.
     */
    public static RequestIntents extractRequestIntents(ITResourceOrchestrationType intent) {
		return intent.getITResource().stream()
				.filter(it -> it.getConfiguration().getClass().equals(RequestIntents.class))
				.map(it -> (RequestIntents) it.getConfiguration()).findFirst().orElse(null);
	}
}