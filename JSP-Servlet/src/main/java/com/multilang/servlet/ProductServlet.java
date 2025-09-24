package com.multilang.servlet;

import com.multilang.dao.ProductDAO;
import com.multilang.model.Product;
import com.multilang.model.Language;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {
    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        String languageId = request.getParameter("lang");
        
        if (languageId == null) {
            languageId = "vi"; // Default language
        }
        
        try {
            switch (action != null ? action : "list") {
                case "list":
                    listProducts(request, response, languageId);
                    break;
                case "detail":
                    showProductDetail(request, response, languageId);
                    break;
                case "add":
                    showAddForm(request, response);
                    break;
                default:
                    listProducts(request, response, languageId);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        try {
            switch (action) {
                case "create":
                    createProduct(request, response);
                    break;
                case "update":
                    updateProduct(request, response);
                    break;
                case "delete":
                    deleteProduct(request, response);
                    break;
                default:
                    response.sendRedirect("products");
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void listProducts(HttpServletRequest request, HttpServletResponse response, String languageId)
            throws ServletException, IOException {
        
        List<Product> products = productDAO.getAllProductsWithTranslation(languageId);
        List<Language> languages = productDAO.getAllLanguages();
        
        request.setAttribute("products", products);
        request.setAttribute("languages", languages);
        request.setAttribute("currentLang", languageId);
        
        request.getRequestDispatcher("/WEB-INF/jsp/product-list.jsp").forward(request, response);
    }

    private void showProductDetail(HttpServletRequest request, HttpServletResponse response, String languageId)
            throws ServletException, IOException {
        
        int productId = Integer.parseInt(request.getParameter("id"));
        Product product = productDAO.getProductWithTranslation(productId, languageId);
        List<Language> languages = productDAO.getAllLanguages();
        
        request.setAttribute("product", product);
        request.setAttribute("languages", languages);
        request.setAttribute("currentLang", languageId);
        
        request.getRequestDispatcher("/WEB-INF/jsp/product-detail.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Language> languages = productDAO.getAllLanguages();
        request.setAttribute("languages", languages);
        
        request.getRequestDispatcher("/WEB-INF/jsp/product-form.jsp").forward(request, response);
    }

    private void createProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Create product logic here
        response.sendRedirect("products");
    }

    private void updateProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Update product logic here
        response.sendRedirect("products");
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int productId = Integer.parseInt(request.getParameter("id"));
        productDAO.deleteProduct(productId);
        response.sendRedirect("products");
    }
}