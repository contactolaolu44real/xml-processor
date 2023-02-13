package com.olaolu.XmlApplication.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CustomPage<J> {
  private List<J> content;
  private Integer totalPages;
  private Long totalElements;
  private Integer pageSize;
  private Integer pageNumber;
}
