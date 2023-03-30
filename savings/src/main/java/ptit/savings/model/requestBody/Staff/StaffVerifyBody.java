package ptit.savings.model.requestBody.Staff;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class StaffVerifyBody {
    @Positive(message = "Invalid staff id")
    @NotNull(message = "Empty staff id")
    private Long id;

    @NotNull(message = "Empty token")
    private String token;
}
