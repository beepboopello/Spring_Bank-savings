package ptit.savings.model.requestBody.Interest;

import lombok.Data;

@Data
public class EditBody {
    private Long id;         // Id của lãi suất
    private String name;     // Tên lãi suất
    private int months;      // Số tháng áp dụng lãi suất
    private double rate;     // Lãi suất (%)
    private String token;    // Mã token của người dùng
}
