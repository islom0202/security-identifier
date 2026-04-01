package antifraud.example.antifraud.dto;

import java.sql.Timestamp;

public interface LinkedUsersRes {
    Long getUserId();
    String getLinkName();
    String getCode();
    String getUserDeviceId();
    Timestamp getSubmittedAt();
}
