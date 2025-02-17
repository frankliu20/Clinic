package by.gp.clinic.repository;

import by.gp.clinic.dbo.TicketDbo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

@Transactional
public interface TicketRepository extends CustomRepository<TicketDbo, Long> {

    @Query("select t.number from TicketDbo t where t.doctor.id = ?1 and t.dateTime > ?2 and t.dateTime < ?3")
    Optional<Integer> getLastTicketNumber(final Long id, final LocalDateTime from, final LocalDateTime to);

    @Query("select count(t) from TicketDbo t where t.doctor.id = ?1 and t.dateTime = ?2")
    int getByDoctorIdAndDateTime(final Long id, final LocalDateTime dateTime);
}
