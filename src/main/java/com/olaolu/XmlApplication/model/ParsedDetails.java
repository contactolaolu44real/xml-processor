package com.olaolu.XmlApplication.model;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@Entity
@Data
@Table(name = "parsed_xml_details")
public class ParsedDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Integer id;
    private String newspaperName;
    private Long width;
    private Long height;
    private Long dpi;
    private Instant uploadTime;
    private String fileName;
}
