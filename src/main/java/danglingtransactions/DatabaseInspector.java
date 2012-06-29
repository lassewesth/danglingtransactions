package danglingtransactions;

import danglingtransactions.services.CorruptionReporter;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class DatabaseInspector {
    public static void main( String[] args ) {
        FileSystemXmlApplicationContext ctx = new FileSystemXmlApplicationContext( "classpath:context.xml" );
        try {
            CorruptionReporter corruptionReporter = ctx.getBean( CorruptionReporter.class );

            corruptionReporter.reportCorruptions();
        }
        finally {
            ctx.close();
        }
    }
}
