//package com.example.TransactionAPI.data.models;
//import jakarta.persistence.*; import java.time.LocalDateTime;
//@Entity @Table(name="audit_logs")
//public class AuditLog {
// @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
// @Column(nullable=false) private String method; @Column(nullable=false) private String path;
// @Column(nullable=false) private int statusCode; @Column(nullable=false) private LocalDateTime occurredAt;
// protected AuditLog() {}
// public AuditLog(String method,String path,int statusCode,LocalDateTime occurredAt){this.method=method;this.path=path;this.statusCode=statusCode;this.occurredAt=occurredAt;}
// public Long getId(){return id;} public String getMethod(){return method;} public String getPath(){return path;} public int getStatusCode(){return statusCode;} public LocalDateTime getOccurredAt(){return occurredAt;}
//}
