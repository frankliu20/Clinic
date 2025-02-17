package by.gp.clinic.controller;

import by.gp.clinic.dto.PageDto;
import by.gp.clinic.dto.PatientDto;
import by.gp.clinic.exception.EntityExistsException;
import by.gp.clinic.exception.EntityNotExistsException;
import by.gp.clinic.facade.PatientFacade;
import by.gp.clinic.search.PatientSearchRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Patient Controller",
    description = "API methods to work with patients")
public class PatientController {

    private final PatientFacade patientFacade;

    @PostMapping("admin/patient")
    @Operation(summary = "Create a patient card")
    public String createPatient(@RequestBody @Validated PatientDto patient) throws EntityExistsException {
        return new JSONObject().put("id", patientFacade.createPatient(patient)).toString();
    }

    @GetMapping(value = "/patient/{id}")
    @Operation(summary = "Get info about patient")
    public PatientDto getPatient(@PathVariable("id") Long id) throws EntityNotExistsException {
        return patientFacade.getPatient(id);
    }

    @PostMapping(value = "/patient/search")
    @Operation(summary = "Search patient")
    public PageDto<PatientDto> searchPatients(@RequestBody final PatientSearchRequest searchRequest) {
        return patientFacade.search(searchRequest);
    }

    @DeleteMapping(value = "/patient/{id}")
    @Operation(summary = "Burn patient card")
    public void removePatient(@PathVariable("id") Long id) throws EntityNotExistsException {
        patientFacade.removePatient(id);
    }
}
