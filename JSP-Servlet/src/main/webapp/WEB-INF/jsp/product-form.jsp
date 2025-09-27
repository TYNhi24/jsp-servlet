<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${isEdit ? 'Chỉnh sửa' : 'Thêm'}" /> sản phẩm - Multi-Language Product System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .form-container {
            background: white;
            border-radius: 20px;
            box-shadow: 0 15px 35px rgba(0,0,0,0.1);
            padding: 40px;
            margin: 30px 0;
        }
        .form-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 30px;
            border-radius: 15px;
            text-align: center;
            margin-bottom: 40px;
        }
        .form-section {
            background: #f8f9fa;
            border-radius: 15px;
            padding: 25px;
            margin-bottom: 25px;
            border-left: 4px solid #667eea;
        }
        .form-control, .form-select {
            border-radius: 10px;
            border: 2px solid #e9ecef;
            padding: 12px 15px;
            font-size: 1rem;
            transition: all 0.3s ease;
        }
        .form-control:focus, .form-select:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
        }
        .btn-submit {
            background: linear-gradient(45deg, #667eea, #764ba2);
            border: none;
            color: white;
            padding: 15px 40px;
            font-size: 1.1rem;
            border-radius: 50px;
            transition: all 0.3s ease;
        }
        .btn-submit:hover {
            background: linear-gradient(45deg, #764ba2, #667eea);
            transform: translateY(-2px);
            box-shadow: 0 10px 25px rgba(0,0,0,0.2);
            color: white;
        }
        .language-tabs {
            margin-bottom: 30px;
        }
        .nav-pills .nav-link {
            border-radius: 25px;
            margin: 0 5px;
            transition: all 0.3s ease;
        }
        .nav-pills .nav-link.active {
            background: linear-gradient(45deg, #667eea, #764ba2);
        }
        .required {
            color: #ff4757;
        }
    </style>
</head>
<body class="bg-light">
    <div class="container py-4">
        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb" class="mb-4">
            <ol class="breadcrumb">
                <li class="breadcrumb-item">
                    <a href="<c:out value='${pageContext.request.contextPath}' />/products" class="text-decoration-none">
                        <i class="fas fa-home me-1"></i>Danh sách sản phẩm
                    </a>
                </li>
                <li class="breadcrumb-item active">
                    <c:out value="${isEdit ? 'Chỉnh sửa sản phẩm' : 'Thêm sản phẩm mới'}" />
                </li>
            </ol>
        </nav>

        <div class="form-container">
            <!-- Form Header -->
            <div class="form-header">
                <i class="fas fa-<c:out value='${isEdit ? \"edit\" : \"plus-circle\"}' /> fa-3x mb-3"></i>
                <h1 class="display-5 fw-bold mb-2">
                    <c:out value="${isEdit ? 'Chỉnh sửa sản phẩm' : 'Thêm sản phẩm mới'}" />
                </h1>
                <p class="lead mb-0">
                    <c:out value="${isEdit ? 'Cập nhật thông tin sản phẩm' : 'Điền thông tin để tạo sản phẩm mới'}" />
                </p>
            </div>

            <!-- Error Messages -->
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-triangle me-2"></i>
                    <strong>Lỗi!</strong> <c:out value="${errorMessage}" />
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <!-- Product Form -->
            <form action="<c:out value='${pageContext.request.contextPath}' />/products" method="post" id="productForm" novalidate>
                <input type="hidden" name="action" value="<c:out value='${isEdit ? \"update\" : \"create\"}' />">
                <c:if test="${isEdit}">
                    <input type="hidden" name="id" value="<c:out value='${product.productId}' />">
                </c:if>

                <!-- Basic Information Section -->
                <div class="form-section">
                    <h4 class="fw-bold mb-4">
                        <i class="fas fa-info-circle me-2 text-primary"></i>
                        Thông tin cơ bản
                    </h4>
                    
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="price" class="form-label">Giá bán <span class="required">*</span></label>
                            <div class="input-group">
                                <input type="number" 
                                       class="form-control" 
                                       id="price" 
                                       name="price" 
                                       step="0.01" 
                                       min="0"
                                       value="<c:out value='${isEdit ? product.price : \"\"}' />" 
                                       required>
                                <span class="input-group-text">VNĐ</span>
                                <div class="invalid-feedback">
                                    Vui lòng nhập giá sản phẩm hợp lệ
                                </div>
                            </div>
                        </div>
                        
                        <div class="col-md-6 mb-3">
                            <label for="weight" class="form-label">Trọng lượng (kg)</label>
                            <div class="input-group">
                                <input type="number" 
                                       class="form-control" 
                                       id="weight" 
                                       name="weight" 
                                       step="0.01" 
                                       min="0"
                                       value="<c:out value='${isEdit ? product.weight : \"\"}' />">
                                <span class="input-group-text">kg</span>
                                <div class="invalid-feedback">
                                    Trọng lượng phải là số dương
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="row">
                        <div class="col-md-12 mb-3">
                            <label for="categoryId" class="form-label">
                                Danh mục sản phẩm <span class="required">*</span>
                            </label>
                            <select class="form-select" id="categoryId" name="categoryId" required>
                                <option value="">-- Chọn danh mục --</option>
                                <option value="1" <c:if test="${isEdit && product.productCategory.productCategoryId == 1}">selected</c:if>>
                                    Điện tử (Có thể giao hàng)
                                </option>
                                <option value="2" <c:if test="${isEdit && product.productCategory.productCategoryId == 2}">selected</c:if>>
                                    Quần áo (Có thể giao hàng)
                                </option>
                                <option value="3" <c:if test="${isEdit && product.productCategory.productCategoryId == 3}">selected</c:if>>
                                    Dịch vụ số (Không giao hàng)
                                </option>
                            </select>
                            <div class="invalid-feedback">
                                Vui lòng chọn danh mục sản phẩm
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Multi-language Information Section -->
                <div class="form-section">
                    <h4 class="fw-bold mb-4">
                        <i class="fas fa-language me-2 text-success"></i>
                        Thông tin đa ngôn ngữ
                    </h4>
                    
                    <!-- Language Tabs -->
                    <ul class="nav nav-pills language-tabs" id="languageTabs" role="tablist">
                        <c:forEach var="lang" items="${languages}" varStatus="status">
                            <li class="nav-item" role="presentation">
                                <button class="nav-link <c:if test='${status.first}'>active</c:if>" 
                                        id="<c:out value='${lang.languageId}' />-tab" 
                                        data-bs-toggle="pill" 
                                        data-bs-target="#<c:out value='${lang.languageId}' />-content" 
                                        type="button" 
                                        role="tab">
                                    <c:out value="${lang.language}" />
                                </button>
                            </li>
                        </c:forEach>
                    </ul>

                    <!-- Language Tab Content -->
                    <div class="tab-content" id="languageTabsContent">
                        <c:forEach var="lang" items="${languages}" varStatus="status">
                            <div class="tab-pane fade <c:if test='${status.first}'>show active</c:if>" 
                                 id="<c:out value='${lang.languageId}' />-content" 
                                 role="tabpanel">
                                 
                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label for="productName_<c:out value='${lang.languageId}' />" class="form-label">
                                            Tên sản phẩm (<c:out value="${lang.language}" />) 
                                            <c:if test="${status.first}"><span class="required">*</span></c:if>
                                        </label>
                                        <input type="text" 
                                               class="form-control" 
                                               id="productName_<c:out value='${lang.languageId}' />" 
                                               name="productName_<c:out value='${lang.languageId}' />" 
                                               value="<c:out value='${isEdit && currentLang == lang.languageId ? product.productName : \"\"}' />"
                                               <c:if test="${status.first}">required</c:if>>
                                        <div class="invalid-feedback">
                                            Vui lòng nhập tên sản phẩm
                                        </div>
                                    </div>
                                    
                                    <div class="col-md-6 mb-3">
                                        <label for="productDescription_<c:out value='${lang.languageId}' />" class="form-label">
                                            Mô tả sản phẩm (<c:out value="${lang.language}" />)
                                        </label>
                                        <textarea class="form-control" 
                                                  id="productDescription_<c:out value='${lang.languageId}' />" 
                                                  name="productDescription_<c:out value='${lang.languageId}' />" 
                                                  rows="3"><c:out value="${isEdit && currentLang == lang.languageId ? product.productDescription : ''}" /></textarea>
                                    </div>
                                </div>
                                
                                <div class="alert alert-info">
                                    <small>
                                        <i class="fas fa-info-circle me-1"></i>
                                        <strong>Ghi chú:</strong> Thông tin này sẽ được hiển thị khi người dùng chọn ngôn ngữ <c:out value="${lang.language}" />
                                    </small>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>

                <!-- Form Actions -->
                <div class="d-flex justify-content-between align-items-center mt-4">
                    <div>
                        <a href="<c:out value='${pageContext.request.contextPath}' />/products" 
                           class="btn btn-outline-secondary btn-lg">
                            <i class="fas fa-times me-2"></i>Hủy bỏ
                        </a>
                    </div>
                    
                    <div>
                        <button type="submit" class="btn-submit btn-lg" id="submitBtn">
                            <i class="fas fa-<c:out value='${isEdit ? \"save\" : \"plus\"}' /> me-2"></i>
                            <c:out value="${isEdit ? 'Cập nhật sản phẩm' : 'Tạo sản phẩm'}" />
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <!-- Scripts -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        // Form validation
        (function() {
            'use strict';
            const form = document.getElementById('productForm');
            
            form.addEventListener('submit', function(event) {
                if (!form.checkValidity()) {
                    event.preventDefault();
                    event.stopPropagation();
                    
                    // Show first invalid field
                    const firstInvalid = form.querySelector('.form-control:invalid, .form-select:invalid');
                    if (firstInvalid) {
                        firstInvalid.focus();
                        
                        // Show alert
                        const alert = document.createElement('div');
                        alert.className = 'alert alert-warning alert-dismissible fade show';
                        alert.style.cssText = 'position: fixed; top: 20px; right: 20px; z-index: 10000; min-width: 300px;';
                        alert.innerHTML = `
                            <i class="fas fa-exclamation-triangle me-2"></i>
                            <strong>Cảnh báo!</strong> Vui lòng kiểm tra các trường bắt buộc!
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        `;
                        document.body.appendChild(alert);
                        
                        // Auto remove after 3 seconds
                        setTimeout(() => {
                            if (alert && alert.parentNode) {
                                alert.remove();
                            }
                        }, 3000);
                    }
                } else {
                    // Show loading state
                    const submitBtn = document.getElementById('submitBtn');
                    const originalText = submitBtn.innerHTML;
                    submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Đang xử lý...';
                    submitBtn.disabled = true;
                }
                
                form.classList.add('was-validated');
            });
        })();

        // Category selection handler
        document.getElementById('categoryId').addEventListener('change', function() {
            const categoryId = this.value;
            
            if (categoryId) {
                let categoryInfo = '';
                switch(categoryId) {
                    case '1':
                        categoryInfo = 'Danh mục: Điện tử - Có thể giao hàng';
                        break;
                    case '2':
                        categoryInfo = 'Danh mục: Quần áo - Có thể giao hàng';
                        break;
                    case '3':
                        categoryInfo = 'Danh mục: Dịch vụ số - Không thể giao hàng';
                        break;
                }
                
                console.log(categoryInfo);
            }
        });

        // Auto-hide alerts after 5 seconds
        setTimeout(function() {
            const alerts = document.querySelectorAll('.alert:not(.alert-permanent)');
            alerts.forEach(function(alert) {
                if (alert && alert.parentNode && alert.classList.contains('alert-danger')) {
                    const bsAlert = new bootstrap.Alert(alert);
                    bsAlert.close();
                }
            });
        }, 5000);
    </script>
</body>
</html>