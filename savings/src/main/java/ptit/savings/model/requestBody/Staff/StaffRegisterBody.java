package ptit.savings.model.requestBody.Staff;

import jakarta.validation.constraints.*;
import lombok.Data;


@Data
public class StaffRegisterBody {
    @Size(min=3, message = "The First name field must greater that 3 characters")
    private String firstName;
    @Size(min=3, message = "The First name field must greater that 3 characters")
    private String lastName;

    @Size(min=3, message = "Username field must be greater that 8 characters")
    private String username;
    @Email
    @Pattern(regexp = "([a-zA-Z0-9]+(?:[._+-][a-zA-Z0-9]+)*)@([a-zA-Z0-9]+(?:[.-][a-zA-Z0-9]+)*[.][a-zA-Z]{2,})", message = "Please Enter a Valid Email")
    private String email;
    @NotEmpty
    private String password;
}
