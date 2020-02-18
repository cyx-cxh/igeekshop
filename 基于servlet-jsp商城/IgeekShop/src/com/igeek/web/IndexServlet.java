package com.igeek.web;

import java.io.IOException;
import java.sql.SQLException;
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
 * Servlet implementation class IndexServlet
 */
//@WebServlet("/IndexServlet")
public class IndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ProductService service=new ProductServiceImpl();
		CategoryService categoryService=new CategoryServiceImpl();
		try {
			List<Product> hotProductList = service.findHotProducts();
			List<Product> NewProductList = service.findNewProducts();
			List<Category> categoryList =categoryService.findCategoryList();
			request.setAttribute("hotProductList", hotProductList);
			request.setAttribute("NewProductList", NewProductList);
			request.setAttribute("categoryList", categoryList);
			request.getRequestDispatcher("/index.jsp").forward(request, response);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
