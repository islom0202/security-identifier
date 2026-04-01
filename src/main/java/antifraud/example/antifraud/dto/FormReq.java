package antifraud.example.antifraud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FormReq {
    private String adminId;
    private String phone;
    private String code;
    private double latitude;
    private double longitude;
    private String deviceId;
}
