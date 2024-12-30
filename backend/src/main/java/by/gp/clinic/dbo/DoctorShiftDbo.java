package by.gp.clinic.dbo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "doctor_shift")
@EqualsAndHashCode(callSuper = true)
public class DoctorShiftDbo extends AbstractDbo {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private DoctorDbo doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_timing_id")
    private ShiftTimingDbo shiftTiming;

    private LocalDate date;
}
