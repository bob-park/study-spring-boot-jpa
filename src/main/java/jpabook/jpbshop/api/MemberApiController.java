package jpabook.jpbshop.api;

import jpabook.jpbshop.domain.Member;
import jpabook.jpbshop.service.MemberService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
public class MemberApiController {

  private final MemberService memberService;

  public MemberApiController(MemberService memberService) {
    this.memberService = memberService;
  }

  /**
   * 회원 등록 V1
   *
   * <p>! 절대 entity 를 dto 로 사용하지 말것
   *
   * <p>* 이유
   *
   * <pre>
   * - validation 문제가 분명히 발생할 수 있다.
   * - 실무에서는 entity 로 결코 해결되지 않는다.
   * - entity 가 바뀔 경우, API 의 spec 이 변경되기 때문 (API Spec 은 변경되면 안된다.)
   * - entity 만 가지고 request data 를 알 수 없다.
   * </pre>
   *
   * @param member
   * @return
   */
  @PostMapping(path = "api/v1/members")
  public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
    Long memberId = memberService.join(member);

    return new CreateMemberResponse(memberId);
  }

  /**
   * 회원 등록 V2
   *
   * <p>! 별도의 DTO 생성
   *
   * <p>! 절대 entity 를 외부로 노출하면 안된다. (request and response )
   *
   * <pre>
   * - API Spec 을 유지할 수 있다.
   * - validation 을 API Spec 에 따라 설정할 수 있다.
   * - entity 를 변경해도 API Spec 이 변경되지 않을 수 있다.
   * </pre>
   *
   * @param request
   * @return
   */
  @PostMapping(path = "api/v2/members")
  public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
    Member member = new Member();

    member.setName(request.getName());

    return new CreateMemberResponse(memberService.join(member));
  }

  @PutMapping(path = "api/v2/members/{id}")
  public UpdateMemberResponse updateMemberV2(
      @PathVariable Long id, @RequestBody @Valid UpdateMemberRequest request) {

    memberService.update(id, request.getName());

    Member findMember = memberService.find(id);

    return new UpdateMemberResponse(findMember.getId(), findMember.getName());
  }

  static class CreateMemberRequest {

    @NotNull private String name;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }

  static class CreateMemberResponse {
    private Long id;

    public CreateMemberResponse(Long id) {
      this.id = id;
    }

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }
  }

  private static class UpdateMemberRequest {

    @NotNull private String name;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }

  private static class UpdateMemberResponse {

    private Long id;
    private String name;

    public UpdateMemberResponse(Long id, String name) {
      this.id = id;
      this.name = name;
    }

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }
}
