package com.igeek.web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import com.igeek.domain.User;
import com.igeek.service.UserService;
import com.igeek.service.impl.UserServiceImpl;
import com.igeek.utils.CommonUtils;
import com.igeek.utils.MailUtils;

/**
 * Servlet implementation class RegistServlet
 */
//@WebServlet("/regist")
public class RegistServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
