package io.zahori.tms.alm.restclient;

/*-
 * #%L
 * alm-rest-client
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2021 PANEL SISTEMAS INFORMATICOS,S.L
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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

/**
 * The type Alm rest client example.
 */
public class ALMRestClientExample {

    /**
     * The constant HOST.
     */
// SET YOUR DATA HERE !!!
    private static final String HOST = "alm_host";
    /**
     * The constant PORT.
     */
    private static final String PORT = "80";
    /**
     * The constant DOMAIN.
     */
    private static final String DOMAIN = "MY_DOMAIN";
    /**
     * The constant PROJECT.
     */
    private static final String PROJECT = "MY_PROJECT";
    /**
     * The constant USERNAME.
     */
    private static final String USERNAME = "MY_USER";
    /**
     * The constant PASSWORD.
     */
    private static final String PASSWORD = "MY_PASSWORD";
    /**
     * The constant testSetId.
     */
    private static String testSetId = "5201";
    /**
     * The constant testId.
     */
    private static String testId = "1181";

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
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
