package jpabook.jpbshop.controller;

import jpabook.jpbshop.domain.Address;
import jpabook.jpbshop.domain.Member;
import jpabook.jpbshop.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class MemberController {

  private final MemberService memberService;

  public MemberController(MemberService memberService) {
    this.memberService = memberService;
  }

  @GetMapping("/members/new")
  public String createForm(Model model) {
    model.addAttribute("memberForm", new MemberForm());

    return "members/createMemberForm";
  }

  /**
   *
   * ! BindingResult 를 사용할 경우 Error 를 포함한 Result 가 반환된다.
   *
   * ! 별도의 DTO 를 쓴 이유는 entity 의 validation 과 Controller 의 Validation 이 다를 수 있기 때문
   *
   * @param memberForm
   * @param result
   * @return
   */
  @PostMapping("/members/new")
  public String create(@Valid MemberForm memberForm, BindingResult result) {

    if(result.hasErrors()){
      return "members/createMemberForm";
    }

    Address address =
        new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode());

    Member member = new Member();

    member.setName(memberForm.getName());
    member.setAddress(address);

    memberService.join(member);

    return "redirect:/";
  }
}
