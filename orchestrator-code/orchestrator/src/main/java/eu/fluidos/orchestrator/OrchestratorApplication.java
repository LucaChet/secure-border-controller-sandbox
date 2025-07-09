package eu.fluidos.orchestrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrchestratorApplication {
    
    public static void main( String[] args )
    { 
    	try {
            SpringApplication.run(OrchestratorApplication.class, args);
            System.out.println("Orchestrator REST API server started successfully.");


        } catch (Exception e){
        	System.out.println(e.toString());
        }
    }  

}
