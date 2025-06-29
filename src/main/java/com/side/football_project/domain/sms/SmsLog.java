package com.side.football_project.domain.sms;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "sms_log")
@Getter
@NoArgsConstructor
public class SmsLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String recipient;         // 수신번호

    @Column(nullable = false, columnDefinition = "TEXT")
    private String messageText;       // 발송 내용

    @Column(nullable = false, length = 10)
    private String messageType;       // sms or lms

    @Column(nullable = false, length = 20)
    private String status;            // success, fail

    @Column(columnDefinition = "TEXT")
    private String responseBody;      // JSON 전체 응답

    @CreationTimestamp
    private LocalDateTime sentAt;

    @Builder
    public SmsLog(String recipient, String messageText, String messageType, String status, String responseBody, LocalDateTime sentAt) {
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageType = messageType;
        this.status = status;
        this.responseBody = responseBody;
        this.sentAt = sentAt;
    }

    // 발송 시각
}
