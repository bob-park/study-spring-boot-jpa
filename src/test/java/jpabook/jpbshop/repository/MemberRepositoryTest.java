package jpabook.jpbshop.repository;

import jpabook.jpbshop.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class MemberRepositoryTest {

  @Autowired private MemberRepository memberRepository;

  // @Test 에서 @Transaction 을 쓰면 테스트가 끝난 후 rollback 해버림
  @Test
  @Transactional
  @Rollback(false)
  void testMember() throws Exception {
    // given
    Member member = new Member();

    member.setUsername("memberA");

    // when
    Long savedId = memberRepository.save(member);
    Member findMember = memberRepository.find(savedId);

    // then
    assertThat(findMember.getId()).isEqualTo(member.getId());
    assertThat(findMember.getUsername()).isEqualTo(member.getUsername());

    assertThat(findMember)
        .isEqualTo(member); // true -> 같은 transaction 내 entity manager 에 있는 것을 find 에서 반환해주기 때문
  }
}
