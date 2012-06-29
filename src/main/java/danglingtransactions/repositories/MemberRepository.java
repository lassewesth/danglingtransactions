/*
 * Copyright (C) 2012 Neo Technology
 * All rights reserved
 */
package danglingtransactions.repositories;

import danglingtransactions.domain.Member;
import org.springframework.data.neo4j.repository.CRUDRepository;

public interface MemberRepository extends CRUDRepository<Member> {
}
