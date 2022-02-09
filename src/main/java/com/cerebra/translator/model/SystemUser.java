package com.cerebra.translator.model;

import com.cerebra.translator.model.enums.Roles;
import com.cerebra.translator.model.enums.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "TRANSLATOR_USER")
public class SystemUser {

    @GenericGenerator(
            name = "TRANSLATOR_USER_SEQ",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "TRANSLATOR_USER_SEQ", value = "SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "TRANSLATOR_USER_SEQ")
    @Column(name = "Id")
    private Long id;

    @Column(name = "Username", unique = true)
    private String userName;

    @Column(name = "Password")
    private String password;

    @Column(name = "Status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "Is_Active")
    private boolean active = true;

    @Column(name = "Is_Locked")
    private boolean isLocked = false;

    @Column(name = "Is_Expired")
    private boolean isExpired = false;

    @Column(name = "Is_Enabled")
    private boolean isEnabled = true;

    @Column(name = "Role")
    @Enumerated(EnumType.STRING)
    private Roles role;

}
