package com.olaolu.XmlApplication.service;

import com.olaolu.XmlApplication.dto.CustomPage;
import com.olaolu.XmlApplication.dto.SearchParams;
import com.olaolu.XmlApplication.dto.XmlRequest;
import com.olaolu.XmlApplication.exception.BadRequestException;
import com.olaolu.XmlApplication.model.ParsedDetails;
import com.olaolu.XmlApplication.repository.XmlParseRepository;
import com.olaolu.XmlApplication.values.SortColumn;
import com.olaolu.XmlApplication.values.SortOrder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.apache.xerces.dom.DeferredElementImpl;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@RequiredArgsConstructor
@Service
public class XmlParseService {

  private final XmlParseRepository xmlParseRepository;
  public static final String SCHEMA_FILE = "validator.xsd";
  private final ResourceLoader resourceLoader;

  public void uploadFile(XmlRequest xmlRequest) {
    if (validateInputFile(xmlRequest)) {
      List<ParsedDetails> parsedDetails = mapXmlToModel(xmlRequest);
      xmlParseRepository.saveAll(parsedDetails);
    } else {
      throw new BadRequestException("Supplied xml is not the format expected");
    }
  }

  private boolean validateInputFile(XmlRequest input) {
    SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    if (input.getFile().isEmpty() || !input.getFile().getContentType().equals("application/xml")){
      throw new BadRequestException("Select a file and make sure it is xml");
    }
    try {
      Schema schema = schemaFactory.newSchema(new StreamSource(resourceLoader.getResource("classpath:" + SCHEMA_FILE).getInputStream()));
      Validator validator = schema.newValidator();
      File convFile = getFileFromRequest(input);
      validator.validate(new StreamSource(convFile));
      return true;
    } catch (SAXException | IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  private File getFileFromRequest(XmlRequest input) throws IOException {
    File convFile = new File(Objects.requireNonNull(input.getFile().getOriginalFilename()));
    FileOutputStream fos = new FileOutputStream(convFile);
    fos.write(input.getFile().getBytes());
    fos.close();
    return convFile;
  }

  private List<ParsedDetails> mapXmlToModel(XmlRequest request) {
    List<ParsedDetails> parsedDetailsList = new ArrayList<>();
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document document = builder.parse(new InputSource(new StringReader(new String(request.getFile().getBytes()))));
      Element rootElement = document.getDocumentElement();
      Map<Integer, Node> deviceInfosMap = new HashMap<>();
      for (int i = 0; i < rootElement.getChildNodes().getLength(); i++) {
        Node node = rootElement.getChildNodes().item(i);
        if (node.getNodeName().equals("deviceInfo")) {
          deviceInfosMap.put(i, node);
        }
      }
      for (Entry<Integer, Node> entry : deviceInfosMap.entrySet()) {
        ParsedDetails parsedDetails = new ParsedDetails();
        for (int j = 0; j < entry.getValue().getChildNodes().getLength(); j++) {
          Node modelNode = entry.getValue().getChildNodes().item(j);
          if (entry.getValue().getChildNodes().item(j).getNodeName().equals("screenInfo")) {
            String width = modelNode.getAttributes().getNamedItem("width").getNodeValue();
            String height = modelNode.getAttributes().getNamedItem("height").getNodeValue();
            String dpi = modelNode.getAttributes().getNamedItem("dpi").getNodeValue();
            parsedDetails.setDpi(Long.parseLong(dpi));
            parsedDetails.setHeight(Long.parseLong(height));
            parsedDetails.setWidth(Long.parseLong(width));
          }
          if (entry.getValue().getChildNodes().item(j).getNodeName().equals("appInfo")) {
            String newspaperName = ((DeferredElementImpl) modelNode).getElementsByTagName("newspaperName").item(0).getTextContent();
            parsedDetails.setNewspaperName(newspaperName);
            parsedDetails.setUploadTime(Instant.now());
            parsedDetails.setFileName(request.getFile().getName());
          }
          parsedDetailsList.add(parsedDetails);
        }
      }
    } catch (IOException | SAXException | ParserConfigurationException e) {
      e.printStackTrace();
    }
    return parsedDetailsList;
  }

  public CustomPage<ParsedDetails> getUploadedModels(int pageSize, int pageNumber, String fileName, String newspaperName, SortOrder sortOrder,
      SortColumn sortColumn) {
    PageRequest pageRequest = PageRequest.of(pageNumber, pageSize,
        SortOrder.ASC == sortOrder ? Sort.by(sortColumn.getColumn()).ascending() : Sort.by(sortColumn.getColumn()).descending());
    SearchParams searchParams = new SearchParams();
    searchParams.setFileName(fileName);
    searchParams.setNewspaperName(newspaperName);
    Page<ParsedDetails> parsedXml = xmlParseRepository.findParsedXml(searchParams, pageRequest);
    return getPaginatedParsedXml(parsedXml.getContent(), parsedXml);

  }

  CustomPage<ParsedDetails> getPaginatedParsedXml(List<ParsedDetails> parsedDetailsList, Page<ParsedDetails> parsedDetails) {
    return CustomPage.<ParsedDetails>builder()
        .content(parsedDetailsList)
        .pageNumber(parsedDetails.getNumber())
        .pageSize(parsedDetails.getSize())
        .totalElements(parsedDetails.getTotalElements())
        .totalPages(parsedDetails.getTotalPages()).build();
  }
}
