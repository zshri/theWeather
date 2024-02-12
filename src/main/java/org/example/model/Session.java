package org.example.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class Session {

    @Id
    private String id;
    private Long userId;
    private Timestamp expiresAt;

    public Session(){}

    public Session(Long userId, Timestamp expiresAt) {
        this.userId = userId;
        this.expiresAt = expiresAt;
    }

    public Session(String id, Long userId, Timestamp expiresAt) {
        this.id = id;
        this.userId = userId;
        this.expiresAt = expiresAt;
    }



}
