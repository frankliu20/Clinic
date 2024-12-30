package by.gp.clinic.dbo;

import by.gp.clinic.enumerated.Gender;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDate;

@Data
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
public class ManDbo extends AbstractDbo {

    private String name;

    private String lastName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birthDate;
}
