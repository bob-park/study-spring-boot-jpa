package jpabook.jpbshop.api;

import jpabook.jpbshop.domain.Member;
import jpabook.jpbshop.service.MemberService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MemberApiController {

  private final MemberService memberService;

  public MemberApiController(MemberService memberService) {
    this.memberService = memberService;
  }

  // == v1 == //

  /**
   * 회원 조회
   *
   * <p>! 이렇게 하면 안된다.
   *
   * <p>* 문제점
   *
   * <pre>
   * - entity 가 그대로 노출된다. (Spring Framework 는 기본적으로 Jackson 을 사용하기 때무에 @JsonIgnore 를 사용하여 감출 수 있지만, 다른곳에서도 사용하지 못하게 될 수 있다.)
   * - entity 가 변경되면 API Spec 이 변경된다.
   * - APC Spec 에 다양할 수록 entity 내부의 로직이 들어갈 수 밖에 없다.
   * - Data 구조에 확장성이 없다.
   * </pre>
   *
   * @return
   */
  @GetMapping(path = "api/v1/members")
  public List<Member> membersV1() {
    return memberService.findMembers();
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

  // == v2 == //

  /**
   * 회원 조회
   *
   * ! 장점
   *
   * <pre>
   *     - API Spec 유지
   *     - 반환하고 싶은 데이터만 반환 가능
   *     - entity 가 변경되도 API Spec 유지
   *     - 유지보수 👍
   * </pre>
   *
   * @return
   */
  @GetMapping(path = "api/v2/members")
  public Result<List<MemberDto>> memberV2() {
    return new Result<>(
        memberService.findMembers().stream()
            .map(findMember -> new MemberDto(findMember.getName()))
            .collect(Collectors.toList()));
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

  static class MemberDto {
    private final String name;

    public MemberDto(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }

  static class Result<T> {
    private final T data;

    public Result(T data) {
      this.data = data;
    }

    public T getData() {
      return data;
    }
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
