package com.igeek.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.igeek.domain.Category;
import com.igeek.domain.Product;
import com.igeek.service.CategoryService;
import com.igeek.service.ProductService;
import com.igeek.service.impl.CategoryServiceImpl;
import com.igeek.service.impl.ProductServiceImpl;

/**
 * Servlet implementation class FindProById
 */
@WebServlet("/FindProById")
public class FindProById extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pid = request.getParameter("pid");
		ProductService productService=new ProductServiceImpl();
		CategoryService categoryService=new CategoryServiceImpl();
		List<Category>cateList=categoryService.findAll();
		Product pro=productService.findById(pid);
		request.setAttribute("pro", pro);
		request.setAttribute("cateList", cateList);
		request.getRequestDispatcher("/admin/product/edit.jsp").forward(request, response);
	
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
