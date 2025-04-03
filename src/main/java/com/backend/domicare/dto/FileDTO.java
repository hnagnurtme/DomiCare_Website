package com.backend.domicare.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileDTO {
    private Long id;
    private String name;
    private String type;
    private String size;
    private String url;

    private String createBy;
    private String updateBy;
    private Instant createAt;
    private Instant updateAt;
}
