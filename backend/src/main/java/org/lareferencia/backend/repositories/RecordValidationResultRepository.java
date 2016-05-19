package org.lareferencia.backend.repositories;

import org.lareferencia.backend.domain.RecordValidationResult;
import org.springframework.data.solr.repository.SolrCrudRepository;

public interface RecordValidationResultRepository extends
		SolrCrudRepository<RecordValidationResult, String> {
}
