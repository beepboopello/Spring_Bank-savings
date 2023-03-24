package ptit.savings.model.requestBody.Account;

import lombok.Data;

@Data
public class AddAccountBody {
    private String email;     // Tên lãi suất
    private String firstname;      // Số tháng áp dụng lãi suất
    private String lastname;     // Lãi suất (%)
    private String cccd;     // Lãi suất (%)
    private String token;    // Mã token của người dùng
}
