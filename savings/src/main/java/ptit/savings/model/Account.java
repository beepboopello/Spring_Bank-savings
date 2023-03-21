package ptit.savings.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Account {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @NotEmpty(message = "The First name field cannot be empty")
    @Size(min=3, message = "The First name field must greater that 3 characters")
    private String first_name;
    @NotEmpty(message = "The Last name field cannot be empty")
    @Size(min=3, message = "The First name field must greater that 3 characters")
    private String last_name;

    private String stk,cccd;
    private String email;
    private Long balance;

    private LocalDate verified_at;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

}
