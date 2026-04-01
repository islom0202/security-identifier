package antifraud.example.antifraud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailRes {
    private Long id;
    private String userPhone;
    private String userIp;
    private String userDeviceId;
    private String userLocation;
    private Boolean isFraud;
    private Long adminName;
    private String generatedLink;
}
