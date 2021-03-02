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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.zahori.model.Defect;
import io.zahori.model.Run;
import io.zahori.model.Step;
import io.zahori.model.TestCase;
import io.zahori.model.TestInstance;
import io.zahori.model.TestSet;
import io.zahori.tms.alm.http.Http;
import io.zahori.tms.alm.restclient.infrastructure.Entities;
import io.zahori.tms.alm.restclient.infrastructure.Entity;

public class ALMRestClient {

    private final String url;
    private final String baseUrl;

    private String username;

    private Http http = new Http();

    public ALMRestClient(Protocol protocol, String host, String port, String domain, String project) {
        this.url = protocol + "://" + host + ":" + port + "/qcbin/";
        this.baseUrl = url + "rest/domains/" + domain + "/projects/" + project + "/";
    }

    /**
     * 
     * @param url     example: http://host:port/qcbin/
     * @param domain
     * @param project
     */
    public ALMRestClient(String url, String domain, String project) {
        this.url = url;
        this.baseUrl = url + "rest/domains/" + domain + "/projects/" + project + "/";
    }

    public void loginWithForm(String username, String password) {
        this.username = username;

        String auth_url = url + "authentication-point/j_spring_security_check";
        http.loginWithForm(auth_url, "j_username", username, "j_password", password);
    }

    public void loginWithBasicAuth(String username, String password) {
        this.username = username;

        String auth_url = url + "api/authentication/sign-in";
        http.loginWithBasicAuth(auth_url, username, password);
    }

    public TestSet getTestSet(String testSetId) {

        /* REQUEST */
        String url = baseUrl + EntityType.TEST_SETS.getValue() + "/" + testSetId;
        Entity entity = http.getXml(url, Entity.class);

        /* RESPONSE */
        final TestSet testSet = new TestSet(testSetId);

        final List<File> entityAttachments = getTestSetAttachments(testSetId);
        testSet.setAttachments(entityAttachments);

        return testSet;
    }

    public TestCase getTest(String testCaseId) {

        /* REQUEST */
        String url = baseUrl + EntityType.TESTS.getValue() + "/" + testCaseId;
        Entity entity = http.getXml(url, Entity.class);

        /* RESPONSE */
        final TestCase test = new TestCase(testCaseId, entity.getField("name"));

        final List<File> entityAttachments = getTestAttachments(testCaseId);
        test.setAttachments(entityAttachments);

        return test;
    }

    public TestInstance getTestInstance(String testSetId, String testId) {

        /* REQUEST */

        // url
        final String url = baseUrl + EntityType.TEST_INSTANCES.getValue();

        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("query", "{cycle-id[" + testSetId + "];test-id[" + testId + "]}");

        Entities<Entity> entities = http.getXml(url, urlParams, Entities.class);

        /* RESPONSE */
        final Entity entity = entities.entities().get(0);
        final String testInstanceId = entity.getField("id");

        final TestInstance testInstance = new TestInstance(testInstanceId);

        final List<File> entityAttachments = getTestInstanceAttachments(testInstanceId);
        testInstance.setAttachments(entityAttachments);

        return testInstance;
    }

    public Run getRun(String runId) {

        /* REQUEST */
        String url = baseUrl + EntityType.RUNS.getValue() + "/" + runId;
        Entity runEntity = http.getXml(url, Entity.class);

        /* RESPONSE */
        String testId = runEntity.getField("test-id");
        String runName = runEntity.getField("name");
        String status = runEntity.getField("status");
        String duration = runEntity.getField("duration");
        String testInstanceId = runEntity.getField("testcycl-id");
        String owner = runEntity.getField("owner");
        String subTypeId = runEntity.getField("subtype-id");

        // get attachments
        List<File> runAttachments = getRunAttachments(runId);

        Run run = new Run(runId, runName, status, Integer.valueOf(duration), runAttachments);
        run.setTestId(testId);
        run.setTestInstanceId(testInstanceId);
        run.setTestId(testId);
        run.setOwner(owner);
        run.setCustomField("subtype-id", subTypeId);

        // get run steps
        List<Step> runSteps = getRunSteps(runId);
        run.setSteps(runSteps);

        return run;
    }

    public List<Step> getRunSteps(String runId) {

        /* REQUEST */
        String urlSteps = baseUrl + EntityType.RUNS.getValue() + "/" + runId + "/" + EntityType.RUN_STEPS.getValue();
        Entities entities = http.getXml(urlSteps, Entities.class);

        /* RESPONSE */
        List<Step> runSteps = new ArrayList<Step>();
        List<Entity> steps = entities.entities();
        for (Entity stepEntity : steps) {

            String runStepId = stepEntity.getField("id");
            String runStepName = stepEntity.getField("name");
            String stepStatus = stepEntity.getField("status");
            String description = stepEntity.getField("description");
            String expected = stepEntity.getField("expected");
            String actual = stepEntity.getField("actual");
            String executionTime = stepEntity.getField("execution-time");

            // get attachments
            List<File> attachments = getRunStepAttachments(runStepId);

            Step runStep = new Step(runStepId, runStepName, stepStatus, description, attachments);
            runStep.setActual(actual);
            runStep.setExpected(expected);
            runStep.setExecutionTime(executionTime);

            runSteps.add(runStep);
        }

        return runSteps;
    }

    private List<File> getEntityAttachments(EntityType entityType, String entityId) {

        /* REQUEST */
        String urlAttachments = baseUrl + entityType.getValue() + "/" + entityId + "/attachments";
        Entities entities = http.getXml(urlAttachments, Entities.class);

        /* RESPONSE */
        List<Entity> attachments = entities.entities();

        List<File> files = new ArrayList<File>();
        for (Entity attachment : attachments) {
            String attachmentId = attachment.getField("id");
            String filename = attachment.getField("name");

            String urlAttachmentFile = baseUrl + "attachments/" + attachmentId;
            byte[] attachFile = http.getOctetStream(urlAttachmentFile, byte[].class);
            try {
                Path path = Files.write(Paths.get(filename), attachFile);
                files.add(path.toFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return files;
    }

    public List<File> getRunAttachments(String runId) {
        return getEntityAttachments(EntityType.RUNS, runId);
    }

    public List<File> getRunStepAttachments(String runStepId) {
        return getEntityAttachments(EntityType.RUN_STEPS, runStepId);
    }

    public List<File> getTestAttachments(String testId) {
        return getEntityAttachments(EntityType.TESTS, testId);
    }

    public List<File> getTestSetAttachments(String testSetId) {
        return getEntityAttachments(EntityType.TEST_SETS, testSetId);
    }

    public List<File> getTestInstanceAttachments(String testInstanceId) {
        return getEntityAttachments(EntityType.TEST_INSTANCES, testInstanceId);
    }

    public Run createRun(Run run, String testSetId, String testId) {
        if (run == null) {
            return null;
        }

        run.setTestSetId(testSetId);
        run.setTestId(testId);
        return createRun(run);
    }

    public Run createRun(Run run) {

        TestInstance testInstance = getTestInstance(run.getTestSetId(), run.getTestId());
        String testInstanceId = testInstance.getId();

        /* REQUEST */

        // url
        String url = baseUrl + EntityType.RUNS.getValue();

        // body
        Entity runEntity = new Entity();
        runEntity.setType("run");
        runEntity.addField("cycle-id", run.getTestSetId());
        runEntity.addField("test-id", run.getTestId());
        runEntity.addField("name", run.getName());
        runEntity.addField("duration", String.valueOf(run.getDuration()));
        // First run status must be "Not Completed"
        runEntity.addField("status", "Not Completed");
        runEntity.addField("testcycl-id", testInstanceId);
        runEntity.addField("owner", username);
        runEntity.addField("subtype-id", "hp.qc.run.MANUAL");

        // request
        Entity runCreated = http.postXml(url, runEntity, Entity.class);

        /* RESPONSE */
        String runId = runCreated.getField("id");

        run.setId(runId);

        // Create run steps
        List<Step> runStepList = run.getSteps();
        if (runStepList != null) {
            for (int i = 0; i < run.getSteps().size(); i++) {
                Step runStep = runStepList.get(i);
                Step createRunStep = createRunStep(runId, runStep);
                runStepList.set(i, createRunStep);
            }
        }

        // Upload attachments
        if (run.getAttachments() != null) {
            for (File attachment : run.getAttachments()) {
                createAttachment(EntityType.RUNS, runId, attachment);
            }
        }

        // Update run with final status
        // NOTE: Is mandatory to update status (put request) after the run
        // creation (post request) with "Not
        // Completed" status for ALM to update correctly the test instance
        // status
        Entity runEntityUpdate = new Entity();
        runEntityUpdate.setType("run");
        runEntityUpdate.addField("status", run.getStatus());
        Entity runUpdated = http.putXml(url + "/" + runId, runEntityUpdate, Entity.class);

        return run;
    }

    public Step createRunStep(String runId, String name, String status, String description, String expected, String actual, String executionTime,
            List<File> attachments) {

        Step runStep = new Step(null, name, status, description, attachments);
        runStep.setExpected(expected);
        runStep.setActual(actual);
        runStep.setExecutionTime(executionTime);

        return createRunStep(runId, runStep);
    }

    public Step createRunStep(String runId, Step runStep) {

        /* REQUEST */

        // url
        String url = baseUrl + EntityType.RUNS.getValue() + "/" + runId + "/" + EntityType.RUN_STEPS.getValue();

        // body
        Entity runStepEntity = new Entity();
        runStepEntity.setType("run-step");
        runStepEntity.addField("name", runStep.getName());
        runStepEntity.addField("status", runStep.getStatus());
        runStepEntity.addField("description", runStep.getDescription());
        runStepEntity.addField("expected", runStep.getExpected());
        runStepEntity.addField("actual", runStep.getActual());
        runStepEntity.addField("execution-time", runStep.getExecutionTime());

        // request
        Entity runStepCreated = http.postXml(url, runStepEntity, Entity.class);

        /* RESPONSE */
        String runStepId = runStepCreated.getField("id");
        runStep.setId(runStepId);

        if (runStep.getAttachments() != null) {
            for (File attachment : runStep.getAttachments()) {
                createAttachment(EntityType.RUN_STEPS, runStepId, attachment);
            }
        }

        return runStep;
    }

    public Defect createDefect(String title, String description, String detectedBy, String assignedTo, String status, String severity, String priority,
            Map<String, String> customFields, List<File> attachments) {

        Defect defect = new Defect(null, title, description, detectedBy, assignedTo, status, severity, priority, attachments);
        return createDefect(defect);
    }

    public Defect createDefect(Defect defect) {

        /* REQUEST */

        // url
        String url = baseUrl + EntityType.DEFECTS.getValue();

        // body
        Entity defectEntity = new Entity();
        defectEntity.setType("defect");
        defectEntity.addField("name", defect.getTitle());
        defectEntity.addField("description", defect.getDescription());
        defectEntity.addField("detected-by", defect.getDetectedBy());
        defectEntity.addField("owner", defect.getAssignedTo());
        defectEntity.addField("status", defect.getStatus());
        defectEntity.addField("severity", defect.getSeverity());
        defectEntity.addField("priority", defect.getPriority());
        // Creation date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        defectEntity.addField("creation-time", sdf.format(new Date()));

        // Custom fields
        if (defect.getCustomFields() != null) {
            for (Map.Entry<String, String> field : defect.getCustomFields().entrySet()) {
                defectEntity.addField(field.getKey(), field.getValue());
            }
        }

        // request
        Entity defectCreated = http.postXml(url, defectEntity, Entity.class);

        /* RESPONSE */
        String defectId = defectCreated.getField("id");
        defect.setId(defectId);

        // Create attachments
        if (defect.getAttachments() != null) {
            for (File attachment : defect.getAttachments()) {
                createAttachment(EntityType.DEFECTS, defectId, attachment);
            }
        }

        return defect;
    }

    public void createAttachment(EntityType entityType, String entityId, File file) {
        try {
            /* REQUEST */

            // url
            String url = baseUrl + entityType.getValue() + "/" + entityId + "/attachments";

            // body
            byte[] fileData = Files.readAllBytes(Paths.get(file.getPath()));

            // headers
            http.addHeader("Slug", file.getName());

            // request
            Entity entity = http.postOctetStream(url, fileData, Entity.class);

        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public enum Protocol {
        http, https;
    }

    public enum EntityType {
        TEST_SETS("test-sets"), TESTS("tests"), TEST_INSTANCES("test-instances"), RUNS("runs"), RUN_STEPS("run-steps"), DEFECTS("defects");

        private String value;

        private EntityType(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

}
