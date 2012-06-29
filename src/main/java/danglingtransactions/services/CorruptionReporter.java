package danglingtransactions.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CorruptionReporter {
    private static final Log LOG = LogFactory.getLog( CorruptionReporter.class );

    @Autowired
    private GraphDatabaseService graphDatabaseService;

    public void reportCorruptions() {
        int validNodeCount = 0;
        int invalidNodeCount = 0;
        for ( Node node : graphDatabaseService.getAllNodes() ) {
            if ( !node.hasProperty( "__type__" ) ) {
                LOG.info( "No property '__type__' for node " + node );
                invalidNodeCount++;
            }
            else {
                validNodeCount++;
            }
        }

        System.out.println( invalidNodeCount == 0 ? "Database is healthy" : "Database is corrupt!" );
        System.out.println( "#valid nodes: " + validNodeCount );
        System.out.println( "#invalid nodes: " + invalidNodeCount );
    }
}