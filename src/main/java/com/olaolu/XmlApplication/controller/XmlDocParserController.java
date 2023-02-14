package com.olaolu.XmlApplication.controller;

import com.olaolu.XmlApplication.dto.CustomPage;
import com.olaolu.XmlApplication.dto.XmlRequest;
import com.olaolu.XmlApplication.exception.BadRequestException;
import com.olaolu.XmlApplication.model.ParsedDetails;
import com.olaolu.XmlApplication.service.XmlParseService;
import com.olaolu.XmlApplication.values.SortColumn;
import com.olaolu.XmlApplication.values.SortOrder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/xml")
@RequiredArgsConstructor
public class XmlDocParserController {
  private final XmlParseService xmlParseService;

  @PostMapping
  public void createXmlDetails(@ModelAttribute @Valid XmlRequest request){
    xmlParseService.uploadFile(request);
  }

  @GetMapping
  public CustomPage<ParsedDetails> getUploadedDetails(@RequestParam(required = false) String fileName, @RequestParam(required = false) String newspaperName,
  @RequestParam(required = false, defaultValue = "NEWS_PAPER_NAME") SortColumn sortColumn, @RequestParam(required = false, defaultValue = "DESC")SortOrder sortOrder,
      @RequestParam(required = false, defaultValue = "0") final Integer pageNumber,
      @RequestParam(required = false, defaultValue = "10") final Integer pageSize){
    return xmlParseService.getUploadedModels(pageSize,pageNumber,fileName,newspaperName,sortOrder,sortColumn);
  }

}
