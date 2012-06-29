package danglingtransactions.services;

import danglingtransactions.domain.Member;
import danglingtransactions.repositories.MemberRepository;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FriendshipService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private Neo4jTemplate template;

    @Transactional
    public void makeFriends( String f1, String f2 ) throws Exception {
        Member m1 = memberRepository.save( new Member( f1 ) );
        Member m2 = memberRepository.save( new Member( f2 ) );

        m1.befriend( m2 );

        memberRepository.save( m1 );
    }

    @Transactional
    public void clearDatabase() {
        for ( Node node : template.getGraphDatabaseService().getAllNodes() ) {
            for ( Relationship relationship : node.getRelationships() ) {
                relationship.delete();
            }
            node.delete();
        }
    }
}
