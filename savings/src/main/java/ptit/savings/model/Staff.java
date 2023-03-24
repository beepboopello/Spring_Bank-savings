package ptit.savings.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.validator.constraints.UniqueElements;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotEmpty(message = "The First name field cannot be empty")
    @Size(min = 3, message = "The First name field must greater that 3 characters")
    private String firstName;
    @NotEmpty(message = "The Last name field cannot be empty")
    @Size(min = 3, message = "The First name field must greater that 3 characters")
    private String lastName;

    @NotNull
    @Size(min = 3, message = "Username field must be greater that 8 characters")
    private String username;

    @Email
    @NotEmpty
    @Pattern(regexp = "([a-zA-Z0-9]+(?:[._+-][a-zA-Z0-9]+)*)@([a-zA-Z0-9]+(?:[.-][a-zA-Z0-9]+)*[.][a-zA-Z]{2,})", message = "Please Enter a Valid Email")
    private String email;

    @NotEmpty
    @NotNull
    @JsonIgnore
    private String password;

    @NotNull
    @JsonIgnore
    private int isAdmin;

    private String token;
    private String code;
    private int verified;
    private LocalDateTime verified_at;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;


    public Staff(String firstName,String lastName, String email, String username, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
