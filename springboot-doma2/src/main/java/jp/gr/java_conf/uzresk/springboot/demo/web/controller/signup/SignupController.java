package jp.gr.java_conf.uzresk.springboot.demo.web.controller.signup;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.gr.java_conf.uzresk.springboot.demo.dao.MemberDao;
import jp.gr.java_conf.uzresk.springboot.demo.entity.Member;
import jp.gr.java_conf.uzresk.springboot.framework.validator.order.CheckOrder;

@Controller
@RequestMapping("signup")
public class SignupController {

	@Autowired
	private MemberDao memberDao;

	@Autowired
	SignupValidator signupValidator;

	@ModelAttribute
	SignupForm setUpForm() {
		return new SignupForm();
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(signupValidator);
	}

	@GetMapping
	String index(Model model) {

		return "signup/signup";
	}

	@PostMapping(path = "create")
	String create(@Validated(CheckOrder.class) SignupForm signupForm, BindingResult result, Model model) {

		if (result.hasErrors()) {
			result.getAllErrors().stream().forEach(s -> System.out.println(s));
			return index(model);
		}

		Member member = new Member();
		BeanUtils.copyProperties(signupForm, member);
		member.setPassword(new BCryptPasswordEncoder().encode(signupForm.getPassword()));
		memberDao.insert(member);

		return "redirect:complete";
	}

	@GetMapping(path = "complete")
	String complete(Model model) {

		return "signup/signup-complete";
	}
}