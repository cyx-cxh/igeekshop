package com.igeek.web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import com.igeek.domain.User;
import com.igeek.service.UserService;
import com.igeek.service.impl.UserServiceImpl;

/**
 * Servlet implementation class LoginServlet
 */
//@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
  		response.sendRedirect(request.getContextPath()+"/IndexServlet");
  		return;
  	}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
