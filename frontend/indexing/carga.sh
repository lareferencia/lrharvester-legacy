 #!/bin/bash  
for file in *.solr.xml; do ./post.sh biblio $file; done