package co.micol.prj.member.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import co.micol.prj.common.Sha256;
import co.micol.prj.common.ViewResolve;
import co.micol.prj.member.service.MemberService;
import co.micol.prj.member.service.MemberVO;
import co.micol.prj.member.serviceImpl.MemberServiceImpl;

@WebServlet("/login.do")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public LoginController() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 로그인 처리
		HttpSession session = request.getSession();
		MemberService dao = new MemberServiceImpl();
		MemberVO member = new MemberVO();
		member.setMemberId(request.getParameter("memberId"));
		member.setMemberPassword(Sha256.encrypt(request.getParameter("memberPassword")));
		
		member = dao.memberSelect(member);
		String page = null;
		
		if(member != null) {
			session.setAttribute("id", member.getMemberId());
			session.setAttribute("author", member.getMemberAuthor());
			if(member.getMemberAuthor().equals("ADMIN")) {
				page = "adminhome.do";
			}else {
				page = "home.do";
			}			
			response.sendRedirect(page);  //ViewResolve 를 안태운다
		}else {
			request.setAttribute("message", "아이디 또는 패스워드가 틀렸다");
			String path = "member/membermessage.jsp";
			ViewResolve.forward(request, response, path);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
