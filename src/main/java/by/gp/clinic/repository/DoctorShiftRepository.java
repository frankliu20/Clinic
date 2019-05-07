package by.gp.clinic.repository;

import by.gp.clinic.dbo.DoctorShiftDbo;
import by.gp.clinic.dbo.ShiftTimingDbo;
import by.gp.clinic.enums.ShiftOrder;
import by.gp.clinic.enums.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Transactional
public interface DoctorShiftRepository extends JpaRepository<DoctorShiftDbo, Long> {

    @Query("select s.shiftTiming.shiftOrder from DoctorShiftDbo s where s.date = ?1 and s.doctor.speciality = ?2")
    List<ShiftOrder> findShiftOrdersByDateAndSpeciality(final LocalDate date, final Speciality speciality);

    @Query("select s.shiftTiming from DoctorShiftDbo s where s.doctor.id = ?1 and s.date = ?2")
    ShiftTimingDbo getShiftTimingByDoctorIdAndDate(final Long id, final LocalDate date);

    boolean existsByDoctorIdAndDate(final Long id, final LocalDate date);

    List<DoctorShiftDbo> getAllByDoctorId(final Long id);
}