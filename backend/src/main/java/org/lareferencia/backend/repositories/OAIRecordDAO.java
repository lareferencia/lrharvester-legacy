package org.lareferencia.backend.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.lareferencia.backend.domain.NetworkSnapshot;
import org.lareferencia.backend.domain.OAIRecord;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author lmatas
 *
 * Se implementa un OAIRecordDAO por fuera de Spring Data para realizar bulk insert de registros.
 * Aquí se utilizan técnicas para minimizar el memory footprint de las tareas de almacenamiento de registros.
 *
 */


@Component
public class OAIRecordDAO {
	
	/** 
	 * TODO: Aunque flush and clear funcionan aceptablemente bien, sería preferible el uso de un stateless session hibernate 
	 */
	
    @PersistenceContext
    private EntityManager entityManager;
    
    public void store(OAIRecord record) {
        entityManager.merge(record);
    	//entityManager.flush();
    	//entityManager.clear();
    }

    @Transactional
    public void store(List<OAIRecord> records, NetworkSnapshot snapshot) {
    	
    	System.out.println();
    	
    	for (OAIRecord record:records) {
    		record.setSnapshot(snapshot);		
    		entityManager.merge(record);
    	}
    	
    	entityManager.flush();
    	
    	entityManager.clear();
    	
    	System.out.println();

      
    }
    
    public void flushAndClear() {
    	
    	entityManager.flush();
    	entityManager.clear();
		
	}
    
    @Transactional
    public void delete(Long recordID) {
        OAIRecord record = entityManager.find(OAIRecord.class, recordID);
        entityManager.remove(record);
    }
    @Transactional(readOnly = true)
    public OAIRecord findById(Long recordID) {
        return entityManager.find(OAIRecord.class, recordID);
    }
    @Transactional(readOnly = true)
    public List<OAIRecord> findAll() {
        Query query = entityManager.createQuery("from OAIRecord");
        return query.getResultList();
    }
}