package com.igeek.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.igeek.domain.Category;
import com.igeek.service.CategoryService;
import com.igeek.service.impl.CategoryServiceImpl;

/**
 * Servlet implementation class FindCategoryById
 */
@WebServlet("/findCategoryById")
public class FindCategoryById extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cid=request.getParameter("cid");
		System.out.println(cid);
		CategoryService categoryService=new CategoryServiceImpl();
	Category category=	categoryService.findByid(cid);
	System.out.println(category);
		request.setAttribute("category", category);
		request.getRequestDispatcher("/admin/category/edit.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
