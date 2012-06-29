package danglingtransactions.domain;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class Member {
    @GraphId
    private Long id;

    private String name;

    private Member friend;

    public Member() {

    }

    public Member( String name ) {

        this.name = name;
    }

    public void befriend( Member member ) {
        friend = member;
    }
}
