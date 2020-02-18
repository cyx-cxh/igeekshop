package com.igeek.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.igeek.domain.Product;
import com.igeek.service.CategoryService;
import com.igeek.service.ProductService;
import com.igeek.service.impl.CategoryServiceImpl;
import com.igeek.service.impl.ProductServiceImpl;


@WebServlet("/deleteCategoryByid")
public class DeleteCategoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String cid = request.getParameter("cid");
		CategoryService categoryService=new CategoryServiceImpl();
		categoryService.delete(cid);
		response.sendRedirect(request.getContextPath()+"/findAllCategory");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
