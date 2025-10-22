package com.multilang.dao;

import com.multilang.model.Product;
import com.multilang.model.ProductCategory;
import com.multilang.model.Language;
import com.multilang.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductDAO {

    public List<Product> getAllProductsWithTranslation(String languageId) {
        List<Product> products = new ArrayList<>();
        String sql = """
            SELECT 
                p.ProductID,
                p.Price,
                p.Weight,
                p.ProductCategoryID,
                pt.ProductName,
                pt.ProductDescription,
                pct.CategoryName,
                pc.CanBeShipped
            FROM Product p
            LEFT JOIN ProductTranslation pt ON p.ProductID = pt.ProductID AND pt.LanguageID = ?
            LEFT JOIN ProductCategory pc ON p.ProductCategoryID = pc.ProductCategoryID
            LEFT JOIN ProductCategoryTranslation pct ON pc.ProductCategoryID = pct.ProductCategoryID AND pct.LanguageID = ?
            ORDER BY p.ProductID
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, languageId);
            pstmt.setString(2, languageId);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("ProductID"));
                product.setPrice(rs.getBigDecimal("Price"));
                product.setWeight(rs.getBigDecimal("Weight"));
                product.setProductName(rs.getString("ProductName"));
                product.setProductDescription(rs.getString("ProductDescription"));
                
                ProductCategory category = new ProductCategory();
                category.setProductCategoryId(rs.getInt("ProductCategoryID"));
                category.setCategoryName(rs.getString("CategoryName"));
                category.setCanBeShipped(rs.getBoolean("CanBeShipped"));
                
                product.setProductCategory(category);
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return products;
    }

    public Product getProductWithTranslation(int productId, String languageId) {
        Product product = null;
        String sql = """
            SELECT 
                p.ProductID,
                p.Price,
                p.Weight,
                p.ProductCategoryID,
                pt.ProductName,
                pt.ProductDescription,
                pct.CategoryName,
                pc.CanBeShipped
            FROM Product p
            LEFT JOIN ProductTranslation pt ON p.ProductID = pt.ProductID AND pt.LanguageID = ?
            LEFT JOIN ProductCategory pc ON p.ProductCategoryID = pc.ProductCategoryID
            LEFT JOIN ProductCategoryTranslation pct ON pc.ProductCategoryID = pct.ProductCategoryID AND pct.LanguageID = ?
            WHERE p.ProductID = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, languageId);
            pstmt.setString(2, languageId);
            pstmt.setInt(3, productId);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                product = new Product();
                product.setProductId(rs.getInt("ProductID"));
                product.setPrice(rs.getBigDecimal("Price"));
                product.setWeight(rs.getBigDecimal("Weight"));
                product.setProductName(rs.getString("ProductName"));
                product.setProductDescription(rs.getString("ProductDescription"));
                
                ProductCategory category = new ProductCategory();
                category.setProductCategoryId(rs.getInt("ProductCategoryID"));
                category.setCategoryName(rs.getString("CategoryName"));
                category.setCanBeShipped(rs.getBoolean("CanBeShipped"));
                
                product.setProductCategory(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return product;
    }

    public List<Language> getAllLanguages() {
        List<Language> languages = new ArrayList<>();
        String sql = "SELECT LanguageID, Language FROM Language ORDER BY Language";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Language language = new Language();
                language.setLanguageId(rs.getString("LanguageID"));
                language.setLanguage(rs.getString("Language"));
                languages.add(language);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return languages;
    }

    public void deleteProduct(int productId) {
        String deleteTranslations = "DELETE FROM ProductTranslation WHERE ProductID = ?";
        String deleteProduct = "DELETE FROM Product WHERE ProductID = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            try (PreparedStatement pstmt1 = conn.prepareStatement(deleteTranslations);
                 PreparedStatement pstmt2 = conn.prepareStatement(deleteProduct)) {
                
                pstmt1.setInt(1, productId);
                pstmt1.executeUpdate();
                
                pstmt2.setInt(1, productId);
                pstmt2.executeUpdate();
                
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ProductCategory> getAllProductCategories() {
        List<ProductCategory> categories = new ArrayList<>();
        String sql = "SELECT ProductCategoryID, CanBeShipped FROM ProductCategory ORDER BY ProductCategoryID";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                ProductCategory category = new ProductCategory();
                category.setProductCategoryId(rs.getInt("ProductCategoryID"));
                category.setCanBeShipped(rs.getBoolean("CanBeShipped"));
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return categories;
    }

    public List<ProductCategory> getAllProductCategoriesWithTranslation(String languageId) {
        List<ProductCategory> categories = new ArrayList<>();
        String sql = """
            SELECT 
                pc.ProductCategoryID,
                pc.CanBeShipped,
                pct.CategoryName
            FROM ProductCategory pc
            LEFT JOIN ProductCategoryTranslation pct ON pc.ProductCategoryID = pct.ProductCategoryID AND pct.LanguageID = ?
            ORDER BY pc.ProductCategoryID
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, languageId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ProductCategory category = new ProductCategory();
                category.setProductCategoryId(rs.getInt("ProductCategoryID"));
                category.setCanBeShipped(rs.getBoolean("CanBeShipped"));
                category.setCategoryName(rs.getString("CategoryName"));
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return categories;
    }

    public void insertProduct(Product product, Map<String, String[]> translations) {
        String insertProduct = """
            INSERT INTO Product (Price, Weight, ProductCategoryID) 
            VALUES (?, ?, ?)
            """;
        
        String insertTranslation = """
            INSERT INTO ProductTranslation (ProductID, LanguageID, ProductName, ProductDescription) 
            VALUES (?, ?, ?, ?)
            """;

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            try (PreparedStatement pstmt1 = conn.prepareStatement(insertProduct, Statement.RETURN_GENERATED_KEYS)) {
                pstmt1.setBigDecimal(1, product.getPrice());
                pstmt1.setBigDecimal(2, product.getWeight());
                pstmt1.setInt(3, product.getProductCategory().getProductCategoryId());
                
                int affectedRows = pstmt1.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creating product failed, no rows affected.");
                }

                try (ResultSet generatedKeys = pstmt1.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int productId = generatedKeys.getInt(1);
                        
                        // Insert translations for all languages
                        try (PreparedStatement pstmt2 = conn.prepareStatement(insertTranslation)) {
                            for (Map.Entry<String, String[]> entry : translations.entrySet()) {
                                String languageId = entry.getKey();
                                String[] values = entry.getValue();
                                String productName = values[0];
                                String productDescription = values.length > 1 ? values[1] : "";
                                
                                if (productName != null && !productName.trim().isEmpty()) {
                                    pstmt2.setInt(1, productId);
                                    pstmt2.setString(2, languageId);
                                    pstmt2.setString(3, productName.trim());
                                    pstmt2.setString(4, productDescription != null ? productDescription.trim() : "");
                                    pstmt2.executeUpdate();
                                }
                            }
                        }
                    } else {
                        throw new SQLException("Creating product failed, no ID obtained.");
                    }
                }
                
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProduct(Product product, Map<String, String[]> translations) {
        String updateProduct = """
            UPDATE Product 
            SET Price = ?, Weight = ?, ProductCategoryID = ?
            WHERE ProductID = ?
            """;
        
        String deleteTranslations = "DELETE FROM ProductTranslation WHERE ProductID = ?";
        
        String insertTranslation = """
            INSERT INTO ProductTranslation (ProductID, LanguageID, ProductName, ProductDescription) 
            VALUES (?, ?, ?, ?)
            """;

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                // Update basic product info
                try (PreparedStatement pstmt1 = conn.prepareStatement(updateProduct)) {
                    pstmt1.setBigDecimal(1, product.getPrice());
                    pstmt1.setBigDecimal(2, product.getWeight());
                    pstmt1.setInt(3, product.getProductCategory().getProductCategoryId());
                    pstmt1.setInt(4, product.getProductId());
                    pstmt1.executeUpdate();
                }
                
                // Delete existing translations
                try (PreparedStatement pstmt2 = conn.prepareStatement(deleteTranslations)) {
                    pstmt2.setInt(1, product.getProductId());
                    pstmt2.executeUpdate();
                }
                
                // Insert new translations
                try (PreparedStatement pstmt3 = conn.prepareStatement(insertTranslation)) {
                    for (Map.Entry<String, String[]> entry : translations.entrySet()) {
                        String languageId = entry.getKey();
                        String[] values = entry.getValue();
                        String productName = values[0];
                        String productDescription = values.length > 1 ? values[1] : "";
                        
                        if (productName != null && !productName.trim().isEmpty()) {
                            pstmt3.setInt(1, product.getProductId());
                            pstmt3.setString(2, languageId);
                            pstmt3.setString(3, productName.trim());
                            pstmt3.setString(4, productDescription != null ? productDescription.trim() : "");
                            pstmt3.executeUpdate();
                        }
                    }
                }
                
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Product> searchProductsWithTranslation(String keyword, String languageId) {
        List<Product> products = new ArrayList<>();
        String sql = """
            SELECT 
                p.ProductID,
                p.Price,
                p.Weight,
                p.ProductCategoryID,
                pt.ProductName,
                pt.ProductDescription,
                pct.CategoryName,
                pc.CanBeShipped
            FROM Product p
            LEFT JOIN ProductTranslation pt 
                ON p.ProductID = pt.ProductID 
                AND pt.LanguageID = ?
            LEFT JOIN ProductCategory pc 
                ON p.ProductCategoryID = pc.ProductCategoryID
            LEFT JOIN ProductCategoryTranslation pct 
                ON pc.ProductCategoryID = pct.ProductCategoryID 
                AND pct.LanguageID = ?
            WHERE pt.ProductName LIKE ? OR pt.ProductDescription LIKE ?
            ORDER BY p.ProductID
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, languageId);
            ps.setString(2, languageId);
            ps.setString(3, "%" + keyword + "%");
            ps.setString(4, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("ProductID"));
                product.setPrice(rs.getBigDecimal("Price"));
                product.setWeight(rs.getBigDecimal("Weight"));
                product.setProductName(rs.getString("ProductName"));
                product.setProductDescription(rs.getString("ProductDescription"));

                ProductCategory category = new ProductCategory();
                category.setProductCategoryId(rs.getInt("ProductCategoryID"));
                category.setCategoryName(rs.getString("CategoryName"));
                category.setCanBeShipped(rs.getBoolean("CanBeShipped"));

                product.setProductCategory(category);
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }


}