package com.multilang.servlet;

import com.multilang.dao.ProductDAO;
import com.multilang.model.Product;
import com.multilang.model.ProductCategory;
import com.multilang.model.Language;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Set UTF-8 encoding
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        
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
                case "edit":
                    showEditForm(request, response, languageId);
                    break;
                default:
                    listProducts(request, response, languageId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Set UTF-8 encoding
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String action = request.getParameter("action");
        
        try {
            switch (action != null ? action : "") {
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
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
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
        
        String productIdStr = request.getParameter("id");
        if (productIdStr == null || productIdStr.trim().isEmpty()) {
            response.sendRedirect("products?lang=" + languageId);
            return;
        }
        
        try {
            int productId = Integer.parseInt(productIdStr);
            Product product = productDAO.getProductWithTranslation(productId, languageId);
            
            if (product == null) {
                request.setAttribute("errorMessage", "Không tìm thấy sản phẩm với ID: " + productId);
                request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
                return;
            }
            
            List<Language> languages = productDAO.getAllLanguages();
            
            request.setAttribute("product", product);
            request.setAttribute("languages", languages);
            request.setAttribute("currentLang", languageId);
            
            request.getRequestDispatcher("/WEB-INF/jsp/product-detail.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("products?lang=" + languageId);
        }
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Language> languages = productDAO.getAllLanguages();
        List<ProductCategory> categories = productDAO.getAllProductCategoriesWithTranslation("vi");
        
        request.setAttribute("languages", languages);
        request.setAttribute("categories", categories);
        request.setAttribute("isEdit", false);
        
        request.getRequestDispatcher("/WEB-INF/jsp/product-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response, String languageId)
            throws ServletException, IOException {
        
        String productIdStr = request.getParameter("id");
        if (productIdStr == null || productIdStr.trim().isEmpty()) {
            response.sendRedirect("products?lang=" + languageId);
            return;
        }
        
        try {
            int productId = Integer.parseInt(productIdStr);
            Product product = productDAO.getProductWithTranslation(productId, languageId);
            
            if (product == null) {
                response.sendRedirect("products?lang=" + languageId);
                return;
            }
            
            List<Language> languages = productDAO.getAllLanguages();
            List<ProductCategory> categories = productDAO.getAllProductCategoriesWithTranslation(languageId);
            
            request.setAttribute("product", product);
            request.setAttribute("languages", languages);
            request.setAttribute("categories", categories);
            request.setAttribute("currentLang", languageId);
            request.setAttribute("isEdit", true);
            
            request.getRequestDispatcher("/WEB-INF/jsp/product-form.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("products?lang=" + languageId);
        }
    }

    private void createProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Lấy thông tin từ form
            String priceStr = request.getParameter("price");
            String weightStr = request.getParameter("weight");
            String categoryIdStr = request.getParameter("categoryId");
            
            // Validate dữ liệu cơ bản
            if (priceStr == null || categoryIdStr == null || 
                priceStr.trim().isEmpty() || categoryIdStr.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Vui lòng điền đầy đủ thông tin bắt buộc!");
                showAddForm(request, response);
                return;
            }
            
            // Parse dữ liệu
            BigDecimal price = new BigDecimal(priceStr);
            BigDecimal weight = null;
            if (weightStr != null && !weightStr.trim().isEmpty()) {
                weight = new BigDecimal(weightStr);
            }
            int categoryId = Integer.parseInt(categoryIdStr);
            
            // Tạo đối tượng Product
            Product product = new Product();
            product.setPrice(price);
            product.setWeight(weight);
            
            ProductCategory category = new ProductCategory();
            category.setProductCategoryId(categoryId);
            product.setProductCategory(category);
            
            // Lấy thông tin đa ngôn ngữ
            Map<String, String[]> translations = new HashMap<>();
            List<Language> languages = productDAO.getAllLanguages();
            
            boolean hasValidTranslation = false;
            for (Language lang : languages) {
                String productName = request.getParameter("productName_" + lang.getLanguageId());
                String productDesc = request.getParameter("productDescription_" + lang.getLanguageId());
                
                if (productName != null && !productName.trim().isEmpty()) {
                    translations.put(lang.getLanguageId(), new String[]{productName, productDesc});
                    hasValidTranslation = true;
                }
            }
            
            if (!hasValidTranslation) {
                request.setAttribute("errorMessage", "Vui lòng nhập tên sản phẩm cho ít nhất một ngôn ngữ!");
                showAddForm(request, response);
                return;
            }
            
            // Lưu vào database
            productDAO.insertProduct(product, translations);
            
            // Redirect về danh sách sau khi tạo thành công
            response.sendRedirect("products?message=create_success");
            
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Dữ liệu số không hợp lệ!");
            showAddForm(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra khi tạo sản phẩm: " + e.getMessage());
            showAddForm(request, response);
        }
    }

    private void updateProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Get product ID
            String productIdStr = request.getParameter("id");
            if (productIdStr == null || productIdStr.trim().isEmpty()) {
                response.sendRedirect("products?error=invalid_id");
                return;
            }
            
            int productId = Integer.parseInt(productIdStr);
            
            // Get form data
            String priceStr = request.getParameter("price");
            String weightStr = request.getParameter("weight");
            String categoryIdStr = request.getParameter("categoryId");
            
            // Validate basic data
            if (priceStr == null || categoryIdStr == null || 
                priceStr.trim().isEmpty() || categoryIdStr.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Vui lòng điền đầy đủ thông tin bắt buộc!");
                showEditForm(request, response, "vi");
                return;
            }
            
            // Parse data
            BigDecimal price = new BigDecimal(priceStr);
            BigDecimal weight = null;
            if (weightStr != null && !weightStr.trim().isEmpty()) {
                weight = new BigDecimal(weightStr);
            }
            int categoryId = Integer.parseInt(categoryIdStr);
            
            // Create Product object
            Product product = new Product();
            product.setProductId(productId);
            product.setPrice(price);
            product.setWeight(weight);
            
            ProductCategory category = new ProductCategory();
            category.setProductCategoryId(categoryId);
            product.setProductCategory(category);
            
            // Get multi-language information
            Map<String, String[]> translations = new HashMap<>();
            List<Language> languages = productDAO.getAllLanguages();
            
            boolean hasValidTranslation = false;
            for (Language lang : languages) {
                String productName = request.getParameter("productName_" + lang.getLanguageId());
                String productDesc = request.getParameter("productDescription_" + lang.getLanguageId());
                
                if (productName != null && !productName.trim().isEmpty()) {
                    translations.put(lang.getLanguageId(), new String[]{productName, productDesc});
                    hasValidTranslation = true;
                }
            }
            
            if (!hasValidTranslation) {
                request.setAttribute("errorMessage", "Vui lòng nhập tên sản phẩm cho ít nhất một ngôn ngữ!");
                showEditForm(request, response, "vi");
                return;
            }
            
            // Update in database
            productDAO.updateProduct(product, translations);
            
            // Redirect to list after successful update
            response.sendRedirect("products?message=update_success");
            
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Dữ liệu số không hợp lệ!");
            showEditForm(request, response, "vi");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra khi cập nhật sản phẩm: " + e.getMessage());
            showEditForm(request, response, "vi");
        }
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String productIdStr = request.getParameter("id");
        if (productIdStr == null || productIdStr.trim().isEmpty()) {
            response.sendRedirect("products?error=invalid_id");
            return;
        }
        
        try {
            int productId = Integer.parseInt(productIdStr);
            productDAO.deleteProduct(productId);
            response.sendRedirect("products?message=delete_success");
        } catch (NumberFormatException e) {
            response.sendRedirect("products?error=invalid_id");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("products?error=delete_failed");
        }
    }
}