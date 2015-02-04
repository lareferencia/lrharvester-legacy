CREATE INDEX "oairecordvalidationresult_snapid_index" ON oairecordvalidationresult USING btree (snapshot_id);
CREATE INDEX "oairecordvalidationresult_recordid_index" ON oairecordvalidationresult USING btree (record_id);

CREATE INDEX "oairecord_snapid_index" ON oairecord USING btree (snapshot_id);	
CREATE INDEX "oairecord_snapid_status_id_index" ON oairecord USING btree (snapshot_id, status, id);	
CREATE INDEX "oairecord_repository_index" ON oairecord USING btree (repositorydomain);	

CREATE INDEX "networksnapshotlog_snapid_index" ON networksnapshotlog USING btree (snapshot_id);