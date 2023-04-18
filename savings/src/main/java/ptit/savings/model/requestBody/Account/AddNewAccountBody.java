package ptit.savings.model.requestBody.Account;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddNewAccountBody {
    @Pattern(regexp = "([a-zA-Z0-9]+(?:[._+-][a-zA-Z0-9]+)*)@([a-zA-Z0-9]+(?:[.-][a-zA-Z0-9]+)*[.][a-zA-Z]{2,})", message = "Please Enter a Valid Email")
    private String email;

    @Size(min = 3, message = "The First name field must greater that 3 characters")
    private String firstName;

    @Size(min = 3, message = "The Last name field must greater that 3 characters")
    private String lastName;

    @NotNull(message = "Empty cccd")
    private String cccd;
    @NotNull(message = "Empty address")
    private String address;
    @NotNull(message = "Empty phone")
    private String phone;
    @NotNull(message = "The DOB field cannot be empty")
    private String dob;
    @NotNull
    private String gender;
    @NotNull(message = "Empty token")
    private String token;
}
