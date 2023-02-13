package com.olaolu.XmlApplication.repository;

import com.olaolu.XmlApplication.dto.SearchParams;
import com.olaolu.XmlApplication.model.ParsedDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface XmlParseRepository extends JpaRepository<ParsedDetails, Long> {

  @Query("Select p from ParsedDetails p where (:#{#req.newspaperName} is null or p.newspaperName=:#{#req.newspaperName}) AND (:#{#req.fileName} is null or p.fileName= :#{#req.fileName})")
  Page<ParsedDetails> findParsedXml(@Param("req")SearchParams req, Pageable pageRequest);
}
