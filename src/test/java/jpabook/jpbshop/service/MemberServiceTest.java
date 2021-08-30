package jpabook.jpbshop.service;

import jpabook.jpbshop.domain.Member;
import jpabook.jpbshop.repository.MemberRepositoryOld;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
// test 에서 @Transactional 를 선언할 경우 테스트가 완료된 후 rollback 된다.
// ! rollback 하기 원치 않을 경우 @Rollback(false) 로 선언하면 된다.
@Transactional
class MemberServiceTest {

  @Autowired private MemberService memberService;
  @Autowired private MemberRepositoryOld memberRepositoryOld;

  @Test
  @Rollback(false)
  void join() throws Exception {
    // given
    Member member = new Member();
    member.setName("kim");

    // when
    Long savedId = memberService.join(member);

    // then
    assertEquals(member, memberRepositoryOld.find(savedId));
  }

  @Test
  void validateMemberThrowException() throws Exception {
    // given
    Member member1 = new Member();
    member1.setName("kim1");

    Member member2 = new Member();
    member2.setName("kim1");

    // when
    memberService.join(member1);

    assertThrows(IllegalStateException.class, () -> memberService.join(member2)); // 예외 발생

    // then

  }
}
