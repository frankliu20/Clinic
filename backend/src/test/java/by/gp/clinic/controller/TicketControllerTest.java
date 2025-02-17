package by.gp.clinic.controller;

import by.gp.clinic.dto.PageDto;
import by.gp.clinic.dto.TicketDto;
import by.gp.clinic.repository.DoctorRepository;
import by.gp.clinic.repository.PatientRepository;
import by.gp.clinic.repository.TicketRepository;
import by.gp.clinic.search.TicketSearchRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static by.gp.clinic.mock.TicketMock.getTicketDtoMock;
import static by.gp.clinic.serializer.ClinicDateTimeSerializer.DATE_TIME_PATTERN;
import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TicketControllerTest extends AbstractControllerTest {

    private static final String TICKET_URL = "/ticket";

    @Autowired
    private TicketRepository repository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;

    @Test
    public void createTicketTest() {
        addEntity();
    }

    @Test
    public void createTicketCheckNumberTest() {
        final var ticket = getTicketDtoMock();
        final long number = addEntity(ticket);

        ticket.setDateTime(ticket.getDateTime().plusMinutes(15L));
        final long nextNumber = addEntity(ticket);

        assertEquals(number + 1, nextNumber);
    }

    @Test
    public void createTicketTwiceTest() {
        final var ticket = getTicketDtoMock();
        final var time = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN).format(ticket.getDateTime());

        addEntity(ticket);
        addEntityWithStatus(ticket, 400, "Ticket for time " + time + " already taken");
    }

    @Test
    public void addTicketInPastTest() {
        final var ticketDtoMock = getDtoMock();
        ticketDtoMock.setDateTime(LocalDateTime.now().minusHours(1).withMinute(15).withSecond(0));
        addEntityWithStatus(ticketDtoMock, 400, "Date must be in future");
    }

    @Test
    public void addTicketWrongTimeTest() {
        final var ticketDtoMock = getDtoMock();
        ticketDtoMock.setDateTime(ticketDtoMock.getDateTime().withMinute(13));
        addEntityWithStatus(ticketDtoMock, 400, "Ticket date must be a multiple of 15");
    }

    @Test
    public void searchTicketTest() {
        final var patients = patientRepository.findAll();
        final var doctors = doctorRepository.findAll();

        patients.forEach(p -> {
            for (var i = 0; i < 10; i++) {
                final var ticketDto = new TicketDto();
                ticketDto.setDoctorId(doctors.get((int) (doctors.size() * Math.random())).getId());
                ticketDto.setPatientId(p.getId());
                ticketDto.setDateTime(now().plusHours((long) (Math.random() * 200) + 2).withMinute(15).withSecond(0));
                postQuery(getUrl(), ticketDto);
            }
        });

        findEntitiesTest(new TicketSearchRequest(), new TypeReference<PageDto<TicketDto>>() {
        });
    }

    @Override
    protected String getUrl() {
        return TICKET_URL;
    }

    @Override
    protected TicketRepository getRepository() {
        return repository;
    }

    @Override
    protected TicketDto getDtoMock() {
        return getTicketDtoMock();
    }
}
