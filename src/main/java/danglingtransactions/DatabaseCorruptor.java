package danglingtransactions;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import danglingtransactions.services.CorruptionReporter;
import danglingtransactions.services.FriendshipService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class DatabaseCorruptor {
    public static final Log LOG = LogFactory.getLog( DatabaseCorruptor.class );
    public static final int NUMBER_OF_THREADS = 20;
    public static final int NUMBER_OF_RELATIONSHIPS_TO_CREATE = 5000;

    private final FriendshipService friendshipService;
    private final CorruptionReporter corruptionReporter;

    public DatabaseCorruptor( FriendshipService friendshipService, CorruptionReporter corruptionReporter ) {
        this.friendshipService = friendshipService;
        this.corruptionReporter = corruptionReporter;
    }

    private void corruptDB() throws Exception {
        cleanDB();
        insertData();
        reportCorruptions();
    }

    private void cleanDB() {
        LOG.info( "Cleaning DB..." );
        friendshipService.clearDatabase();
        LOG.info( "DB clean." );
    }

    private void insertData() throws InterruptedException {
        ExecutorService exec = Executors.newFixedThreadPool( NUMBER_OF_THREADS );

        for ( int i = 0; i < NUMBER_OF_RELATIONSHIPS_TO_CREATE; i += 1 ) {
            final String f1 = "m" + i;
            final String f2 = "m" + (i + NUMBER_OF_RELATIONSHIPS_TO_CREATE);

            exec.execute( new Runnable() {
                @Override
                public void run() {
                    try {
                        friendshipService.makeFriends( f1, f2 );
                    }
                    catch ( Exception e ) {
                        e.printStackTrace();
                    }
                }
            } );
        }

        exec.shutdown();
        LOG.info( "Inserting data..." );
        if ( !exec.awaitTermination( 10, TimeUnit.SECONDS ) ) {
            exec.shutdownNow();
            LOG.info( "Killing inserter..." );
            if ( !exec.awaitTermination( 5, TimeUnit.SECONDS ) ) {
                throw new UnsupportedOperationException( "Unclean shutdown!" );
            }
        }
        LOG.info( "Data inserted." );
    }

    private void reportCorruptions() {
        corruptionReporter.reportCorruptions();
    }

    public static void main( String[] args ) throws Exception {
        final FileSystemXmlApplicationContext ctx = new FileSystemXmlApplicationContext( "classpath:context.xml" );
        try {
            FriendshipService friendshipService = ctx.getBean( FriendshipService.class );
            CorruptionReporter corruptionReporter = ctx.getBean( CorruptionReporter.class );

            DatabaseCorruptor databaseCorruptor = new DatabaseCorruptor( friendshipService, corruptionReporter );

            databaseCorruptor.corruptDB();
        }
        finally {
            ctx.close();
        }
    }
}
