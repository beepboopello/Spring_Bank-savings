package ptit.savings.model.requestBody.Interest;

import lombok.Data;

@Data
public class CalculateBody {
    private Long id;         // Id của lãi suất
    private Long amount;   // Số tiền gửi
    private String token;    // Mã token của người dùng
}
