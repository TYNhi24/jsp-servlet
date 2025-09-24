package com.multilang.dao;

import com.multilang.model.Product;
import com.multilang.model.ProductCategory;
import com.multilang.model.Language;
import com.multilang.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public void insertProduct(Product product) {
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
                            // This would need to be expanded to handle multiple language translations
                            pstmt2.setInt(1, productId);
                            pstmt2.setString(2, "vi"); // Default language
                            pstmt2.setString(3, product.getProductName());
                            pstmt2.setString(4, product.getProductDescription());
                            pstmt2.executeUpdate();
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
}