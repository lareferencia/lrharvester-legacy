/*******************************************************************************
 * Copyright (c) 2015
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Lautaro Matas (lmatas@gmail.com) - Desarrollo e implementación
 * 
 ******************************************************************************/

package org.lareferencia.backend.validation.transformer;

import lombok.Getter;
import lombok.Setter;

import org.lareferencia.backend.domain.OAIRecord;
import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.util.RepositoryNameHelper;


public class AddRepoNameRule extends AbstractTransformerRule {


	private RepositoryNameHelper repositoryNameHelper;
	
	@Getter
	@Setter
	private Boolean doRepoNameAppend;
	
	@Getter
	@Setter
	private Boolean doRepoNameReplace;
	
	@Getter
	@Setter
	private String repoNameField;
	
	@Getter
	@Setter
	private String repoNamePrefix;
	
	@Getter
	@Setter
	private Boolean doInstNameAppend;
	
	@Getter
	@Setter
	private Boolean doInstNameReplace;
	
	@Getter
	@Setter
	private String instNameField;
	
	@Getter
	@Setter
	private String instNamePrefix;


	public AddRepoNameRule() {
	}

	@Override
	public boolean transform(OAIRecord record) {
		
		// Se carga el helper para la resolución de nombre de repositorios
		repositoryNameHelper = new RepositoryNameHelper();
				
		OAIRecordMetadata metadata = record.getMetadata();
	
		
		// Si está configurado agrega a la metadata el reponame y el instname
		if ( doRepoNameAppend ) {
			repositoryNameHelper.appendNameToMetadata(metadata, repoNameField, repoNamePrefix, record.getSnapshot().getNetwork().getName(), doRepoNameReplace );
		}
		
		if ( doInstNameAppend ) 
			repositoryNameHelper.appendNameToMetadata(metadata, instNameField, instNamePrefix, record.getSnapshot().getNetwork().getInstitutionName(), doInstNameReplace );
						
		return doInstNameAppend || doRepoNameAppend;
	}

	

}
