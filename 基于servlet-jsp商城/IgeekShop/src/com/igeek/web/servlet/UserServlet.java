package com.igeek.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;

import com.igeek.domain.PageBean;
import com.igeek.domain.Product;
import com.igeek.domain.User;
import com.igeek.service.ProductService;
import com.igeek.service.UserService;
import com.igeek.service.impl.ProductServiceImpl;
import com.igeek.service.impl.UserServiceImpl;
import com.igeek.utils.CommonUtils;
import com.igeek.utils.MailUtils;


@WebServlet("/user")
public class UserServlet extends BaseServelt {
	private static final long serialVersionUID = 1L;
       


  //用户登录
  	public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  		request.setCharacterEncoding("UTF-8");
  		
  		String checkcode = (String)request.getSession().getAttribute("checkcode");
  		String checkcode2 = request.getParameter("checkcode");
  	
  		if (!checkcode.equalsIgnoreCase(checkcode2)) {
  			request.setAttribute("msg","验证码错误");
  			request.getRequestDispatcher("/login.jsp").forward(request, response);
  			return;
  		}
  		Map<String, String[]> map = request.getParameterMap();
  		User user =new User();
  		try {
  			BeanUtils.populate(user, map);
  		} catch (IllegalAccessException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		} catch (InvocationTargetException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
  	UserService userService=new UserServiceImpl();
  	User loginUser=userService.login(user);
  	if (loginUser==null) {
  		request.setAttribute("msg", "用户名或者密码错误");
  		request.getRequestDispatcher("/login.jsp").forward(request, response);
  				return;
  	}else {
  		String remember=request.getParameter("remember");
  		System.out.println(remember);
  		if ("yes".equals(remember)) {
  			Cookie cookie=new Cookie("username",loginUser.getEmail());
  			cookie.setMaxAge(60*60*24*7);
  			cookie.setPath("/");
  			response.addCookie(cookie);
//  			request.setAttribute("cookie", cookie);
  		}
  		else {
  			Cookie cookie=new Cookie("username","");
  			cookie.setMaxAge(0);
  			cookie.setPath("/");
  			response.addCookie(cookie);
  		}
  		request.getSession().setAttribute("loginUser", loginUser);
  		response.sendRedirect(request.getContextPath()+"/product?method=IndexServlet");
  		return;
  	}
  	}

  	/**
  	 * 用户注册
  	 */
  	public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  	
  		request.setCharacterEncoding("UTF-8");
  		response.setContentType("text/html;charset=UTF-8");
  		User user=new User();
  		try {
  			BeanUtils.populate(user, request.getParameterMap());
  			
  		} catch (IllegalAccessException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		} catch (InvocationTargetException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
  		//uid使用UUID生成
  	user.setUid(CommonUtils.getUUID());
  	//code激活码，使用UUID生成
  	
  	user.setCode(CommonUtils.getUUID());
//  	System.out.println(user);
  	UserService service=new UserServiceImpl();
  	boolean isSuccess = service.regist(user);
  	String activeCode = user.getCode();
  	if (isSuccess) {
  		String emailMsg="恭喜您注册成功，请点击下面的连接进行激活账户"+"<a  href='http://localhost:8080/IgeekShop/active?activeCode="+activeCode+"'>"
  	                   +"http://localhost:8080/IgeekShop/active?activeCode="+activeCode+"</a>";
  		//发送激活邮件				
  		try {
  			MailUtils.sendMail(user.getEmail(), emailMsg);
  		} catch (AddressException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		} catch (MessagingException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
  		response.sendRedirect(request.getContextPath()+"/login.jsp");
  	}
  	else {
  		response.sendRedirect(request.getContextPath()+"/register.jsp");
  	}
  	}


public void loginOut(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
			HttpSession session = request.getSession();
			session.removeAttribute("loginUser");
			Cookie cookname = new Cookie("cookname", "");
			cookname.setMaxAge(0);
			response.addCookie(cookname);
			response.sendRedirect(request.getContextPath()+"/login.jsp");
}
  	





 

}
