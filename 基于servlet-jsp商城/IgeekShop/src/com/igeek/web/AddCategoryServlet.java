package com.igeek.web;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.igeek.domain.Category;
import com.igeek.service.CategoryService;
import com.igeek.service.impl.CategoryServiceImpl;

/**
 * Servlet implementation class AddCategoryServlet
 */
@WebServlet("/addCategory")
public class AddCategoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddCategoryServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		//获取参数
		String cname=request.getParameter("cname");
		//封装数据到category对象
		Category cate =new Category();
		cate.setCname(cname);
		cate.setCid(UUID.randomUUID().toString().replace("-", ""));
		CategoryService categoryService=new CategoryServiceImpl();
		categoryService.add(cate);
		response.sendRedirect(request.getContextPath()+"/findAllCategory");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
