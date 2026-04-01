package antifraud.example.antifraud.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "linked_users")
public class LinkedUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long linkId;
    private String userCode;
    private Timestamp sentAt;
    private Timestamp clickedAt;
}
