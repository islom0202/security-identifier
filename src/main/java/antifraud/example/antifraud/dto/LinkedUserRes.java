package antifraud.example.antifraud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinkedUserRes {
    private Long id;
    private String adminName;
    private String linkName;
    private Timestamp createdAt;
    private Timestamp expiresAt;
    private Boolean isExpired;
}
