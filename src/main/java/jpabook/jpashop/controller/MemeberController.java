package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemeberController {

    private final MemberService memberService;

    //화면 조회
    @GetMapping("/members/new")
    public String createForm(Model model){
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    //정보를 등록
    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result){

        if(result.hasErrors()){
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member=new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";
    }

    //회원 목록 조회
    @GetMapping(value = "/members")
    public String list(Model model){
        List<Member> members = memberService.findMembers();
        model.addAttribute("members",members);
        return "members/memberList";
    }

    //회원 로그인
    @GetMapping(value = "/login")
    public String login(Model model){
        model.addAttribute("loginForm", new LoginForm());
        return "members/memberLogin";
    }

    @PostMapping("/login")
    public String loginId(@ModelAttribute("loginForm") LoginForm loginForm, HttpServletRequest request) {

        HttpSession session = request.getSession();

        if(memberService.login(loginForm)){
            Member nowMem = memberService.findOne(loginForm.getId());
            session.setAttribute("loginUser", nowMem);
            return "redirect:/";
        }
        return "members/memberLogin";
    }
}
