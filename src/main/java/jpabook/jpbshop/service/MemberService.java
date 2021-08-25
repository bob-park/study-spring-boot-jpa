package jpabook.jpbshop.service;

import jpabook.jpbshop.domain.Member;
import jpabook.jpbshop.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
// ! readOnly 로 할 경우 JPA 조회하는 쿼리에서 최적화함
// JPA Persistence Context 에서 DB Query 를 flush 하지 않, Dirty Checking 을 하지 않는 이점이 있다고 함
@Transactional(readOnly = true)
public class MemberService {

  private final MemberRepository memberRepository;

  public MemberService(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

  /**
   * 회원 가입
   *
   * @param member
   * @return
   */
  @Transactional // Class Level 애서 선언한 Annotation 보다 method 에서 선언한 Annotation 의 우선순위가 높음
  public Long join(Member member) {

    validateDuplicateMember(member);

    memberRepository.save(member);

    return member.getId();
  }

  /**
   * 전체 회원 조회
   *
   * @return
   */
  public List<Member> findMembers() {
    return memberRepository.findAll();
  }

  /**
   * 회원 단일 조회
   *
   * @param memberId
   * @return
   */
  public Member find(Long memberId) {
    return memberRepository.find(memberId);
  }

  /**
   * 중복 회원 검증
   *
   * @param member
   */
  private void validateDuplicateMember(Member member) {

    List<Member> findMembers = memberRepository.findByName(member.getName());

    if (!findMembers.isEmpty()) {
      throw new IllegalStateException("이미 존재하는 회원입니다.");
    }
  }
}