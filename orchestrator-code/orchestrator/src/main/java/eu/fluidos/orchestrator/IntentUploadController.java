package eu.fluidos.orchestrator;

import java.io.IOException;
import java.io.File;

import eu.fluidos.orchestrator.*;
import eu.fluidos.jaxb.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/intents")
public class IntentUploadController {
    
    private final KubernetesService kubernetesService;

    public IntentUploadController(KubernetesService kubernetesService) {
        this.kubernetesService = kubernetesService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleXmlUpload(@RequestParam("file") MultipartFile file) {
        try {
            File tempFile = File.createTempFile("intent", ".xml");
            file.transferTo(tempFile);
            ITResourceOrchestrationType intent = XmlIntentParser.extractIntentsFromXMLFile(tempFile.getAbsolutePath());
            RequestIntents reqIntents = XmlIntentParser.extractRequestIntents(intent);            
            kubernetesService.buildConfigMapFromIntents(reqIntents, "fluidos");

            return ResponseEntity.ok("Intent file processed successfully.\n");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing intent file.");
        }
    }
}