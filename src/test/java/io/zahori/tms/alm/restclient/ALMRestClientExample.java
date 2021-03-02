package io.zahori.tms.alm.restclient;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.zahori.model.Defect;
import io.zahori.model.Run;
import io.zahori.model.Status;
import io.zahori.model.Step;
import io.zahori.model.TestCase;
import io.zahori.model.TestInstance;
import io.zahori.model.TestSet;
import io.zahori.tms.alm.restclient.ALMRestClient.EntityType;
import io.zahori.tms.alm.restclient.ALMRestClient.Protocol;

public class ALMRestClientExample {

    // SET YOUR DATA HERE !!!
    private static final String HOST = "alm_host";
    private static final String PORT = "80";
    private static final String DOMAIN = "MY_DOMAIN";
    private static final String PROJECT = "MY_PROJECT";
    private static final String USERNAME = "MY_USER";
    private static final String PASSWORD = "MY_PASSWORD";
    private static String testSetId = "5201";
    private static String testId = "1181";

    public static void main(String[] args) {

        ALMRestClient alm = new ALMRestClient(Protocol.http, HOST, PORT, DOMAIN, PROJECT);

        System.out.println("--- AUTHENTICATION ---");
        // Authentication
        // alm.loginWithForm(USERNAME, PASSWORD);
        alm.loginWithBasicAuth(USERNAME, PASSWORD);

        /* ******************** EXAMPLE 1: GET TEST SET *************** */

        System.out.println("--- GET TEST SET ---");
        TestSet testSet = alm.getTestSet(testSetId);

        /* ******************** EXAMPLE 2: GET TEST ******************* */

        System.out.println("--- GET TEST ---");
        TestCase test = alm.getTest(testId);

        /* ******************** EXAMPLE 3: GET RUN ******************** */

        System.out.println("--- GET RUN ---");
        Run run1 = alm.getRun("8402");

        /* ******************** EXAMPLE 4: GET TEST INSTANCE ********** */

        System.out.println("--- GET TEST INSTANCE ---");
        TestInstance testInstance = alm.getTestInstance(testSetId, testId);

        /* ******************** EXAMPLE 5: CREATE RUN WITH STEPS ****** */

        System.out.println("--- CREATE RUN ---");

        // Attachments
        List<File> attachments = new ArrayList<File>();
        File attachment = new File("src/test/resources/attachments/pruebaAttachmentRest.txt");
        attachments.add(attachment);

        // Run
        Run newRun = new Run(null, "prueba api rest", Status.PASSED, 20, attachments);

        // Run steps
        Step runStep1 = new Step(null, "Paso 1", Status.PASSED, "Descripcion paso 1", attachments);
        runStep1.setExpected("Expected paso 1");
        runStep1.setActual("Actual paso 1");
        newRun.addStep(runStep1);

        Step runStep2 = new Step(null, "Paso 2", Status.PASSED, "Descripcion paso 2", attachments);
        runStep2.setExpected("Expected paso 2");
        runStep2.setActual("Actual paso 2");
        newRun.addStep(runStep2);

        // Create run
        Run runCreated = alm.createRun(newRun, testSetId, testId);

        /* ******************** EXAMPLE 6: CREATE ATTACHMENT ******* */

        System.out.println("--- CREATE ATTACHMENT ---");

        alm.createAttachment(EntityType.TEST_SETS, testSetId, new File("src/test/resources/attachments/pruebaAttachmentRest.txt"));

        /* ******************** EXAMPLE 7: CREATE NEW DEFECT ******* */

        System.out.println("--- CREATE DEFECT ---");

        if (true) {
            System.out.println("Skipped");
            return; // Skip defect creation
        }

        // Custom fields
        Map<String, String> customFields = new HashMap<String, String>();
        customFields.put("user-08", "Seguros"); // Aplicación
        customFields.put("user-11", "V01R00F000"); // Aplicación V.R.
        customFields.put("user-01", "P5"); // Categorización
        customFields.put("user-14", "1"); // Ciclo de apertura
        customFields.put("user-06", "XXX"); // Cliente
        customFields.put("user-template-03", "No"); // Desarrollo Externo
        customFields.put("user-20", "CER"); // Entorno
        customFields.put("user-34", "Aplicación Completa"); // Servicio funcional
        customFields.put("user-09", "Otros"); // Tipo de incidencia
        customFields.put("user-18", "Nuevo Desarrollo"); // Tipo de proyecto

        // Defect attachments
        List<File> defectAttachments = new ArrayList<File>();
        defectAttachments.add(new File("src/test/resources/attachments/pruebaAttachmentRest.txt"));

        // Create defect
        alm.createDefect("Título", "Descripción", USERNAME, USERNAME, Defect.STATUS_CLOSED, Defect.SEVERITY_LOW, Defect.PRIORITY_LOW, customFields,
                defectAttachments);

        System.out.println("Test End");
    }
}
