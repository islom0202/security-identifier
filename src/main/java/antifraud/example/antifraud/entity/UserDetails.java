package antifraud.example.antifraud.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_details")
public class UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long adminId;
    private String userPhone;
    private String userIp;
    private String userDeviceId;
    private String userLocation;
    private Boolean isFraud;
    private Double latitude;
    private Double longitude;
    private Double ipLatitude;
    private Double ipLongitude;
    private String logMessage;
    private Timestamp createdAt;
}
