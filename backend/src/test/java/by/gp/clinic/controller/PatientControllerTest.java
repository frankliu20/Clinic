package by.gp.clinic.controller;

import by.gp.clinic.dto.AbstractDto;
import by.gp.clinic.dto.PageDto;
import by.gp.clinic.dto.PatientDto;
import by.gp.clinic.repository.PatientRepository;
import by.gp.clinic.search.PatientSearchRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static by.gp.clinic.mock.PatientMock.getPatientDtoMock;
import static by.gp.clinic.serializer.ClinicDateTimeSerializer.DATE_TIME_PATTERN;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PatientControllerTest extends AbstractControllerTest {

    private static final String PATIENT_URL = "/patient";

    @Autowired
    private PatientRepository patientRepository;

    @Test
    public void createPatientTest() {
        addEntity(getDtoMock(), "/admin" + getUrl());
    }

    @Test
    public void getPatientTest() {
        getEntityTest(PatientDto.class, 2L);
    }

    @Test
    public void removePatientTest() {
        removeEntityTest(1L);
    }

    @Test
    public void findPatientsTest() {
        findEntitiesTest(new PatientSearchRequest(), new TypeReference<PageDto<PatientDto>>() {
        });
    }

    @Test
    public void createPatientTwiceTest() {
        final var patient = getPatientDtoMock();
        addEntity(patient, "/admin" + getUrl());

        final var result = postQuery("/admin" + getUrl(), patient);
        final var answer = getJsonFormString(getContentAsString(result));

        assertEquals(400, result.getResponse().getStatus());
        assertNotNull(LocalDateTime.parse(getStringFromJson(answer, "time"), ofPattern(DATE_TIME_PATTERN)));
        assertNotNull(getStringFromJson(answer, "message"));
    }

    @Override
    protected String getUrl() {
        return PATIENT_URL;
    }

    @Override
    protected PatientRepository getRepository() {
        return patientRepository;
    }

    @Override
    protected AbstractDto getDtoMock() {
        return getPatientDtoMock();
    }
}
