package ptit.savings.model;

import java.time.LocalDateTime;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
public class Interest {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private int months;

    @NotNull
    @Positive
    private double rate;


    public Interest(String name, int months, double interestRate) {
        this.name = name;
        this.months = months;
        this.rate = interestRate;
    }




    // @JsonIgnore
    // @OneToMany(mappedBy = "interest", cascade = CascadeType.ALL)
    // private Collection<Saving> savings;

}
