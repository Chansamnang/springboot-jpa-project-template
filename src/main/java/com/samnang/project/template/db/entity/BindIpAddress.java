package com.samnang.project.template.db.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ts_ip_Access")
public class BindIpAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pc_name")
    private String pcName;

    @Column(unique = true, nullable = false, length = 15)
    private String ip;

    @Column(name = "pc_role")
    private String pcRole;

    @Column(columnDefinition = "integer default 1")
    private Integer status;

}
