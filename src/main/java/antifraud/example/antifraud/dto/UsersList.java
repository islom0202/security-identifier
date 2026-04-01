package antifraud.example.antifraud.dto;

import java.sql.Timestamp;

public interface UsersList {
    Long getUserId();
    String getUserPhone();
    String getUserCode();
    String getUserIp();
    String getUserDeviceId();
    Boolean getIsFraud();
    Timestamp getClickedAt();
}
