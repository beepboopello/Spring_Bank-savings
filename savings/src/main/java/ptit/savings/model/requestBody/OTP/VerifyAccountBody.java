package ptit.savings.model.requestBody.OTP;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VerifyAccountBody {
    @NotNull(message = "Empty otp")
    private String otp;
    
    @NotNull(message = "Empty token")
    private String token;
}
