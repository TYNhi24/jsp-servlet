<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết sản phẩm - ${product.productName}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .product-hero {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 20px;
            color: white;
            padding: 40px;
            margin-bottom: 30px;
        }
        .info-card {
            background: white;
            border-radius: 15px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.08);
            padding: 25px;
            margin-bottom: 20px;
            transition: transform 0.3s ease;
        }
        .info-card:hover {
            transform: translateY(-2px);
        }
        .price-display {
            background: linear-gradient(45deg, #ff6b6b, #ee5a24);
            color: white;
            padding: 20px;
            border-radius: 15px;
            text-align: center;
        }
        .category-info {
            background: linear-gradient(45deg, #4834d4, #686de0);
            color: white;
            padding: 20px;
            border-radius: 15px;
        }
        .shipping-info {
            padding: 15px;
            border-radius: 15px;
        }
        .can-ship {
            background: linear-gradient(45deg, #00d2d3, #54a0ff);
            color: white;
        }
        .no-ship {
            background: linear-gradient(45deg, #ff4757, #ff3838);
            color: white;
        }
        .language-tabs {
            background: white;
            border-radius: 15px;
            padding: 20px;
            margin-bottom: 20px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.08);
        }
    </style>
</head>
<body class="bg-light">
    <div class="container py-4">
        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb" class="mb-4">
            <ol class="breadcrumb">
                <li class="breadcrumb-item">
                    <a href="products?lang=${currentLang}" class="text-decoration-none">
                        <i class="fas fa-home me-1"></i>Danh sách sản phẩm
                    </a>
                </li>
                <li class="breadcrumb-item active">${product.productName}</li>
            </ol>
        </nav>

        <!-- Product Hero Section -->
        <div class="product-hero">
            <div class="row align-items-center">
                <div class="col-md-8">
                    <h1 class="display-4 fw-bold mb-3">
                        <i class="fas fa-cube me-3"></i>${product.productName}
                    </h1>
                    <p class="lead mb-0">${product.productDescription}</p>
                </div>
                <div class="col-md-4 text-end">
                    <div class="d-flex flex-column align-items-end">
                        <span class="badge bg-light text-dark fs-6 mb-2">ID: #${product.productId}</span>
                        <div class="btn-group">
                            <a href="products?action=edit&id=${product.productId}" class="btn btn-warning btn-lg">
                                <i class="fas fa-edit me-2"></i>Chỉnh sửa
                            </a>
                            <button onclick="deleteProduct(${product.productId})" class="btn btn-danger btn-lg">
                                <i class="fas fa-trash me-2"></i>Xóa
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Language Selection -->
        <div class="language-tabs">
            <h5 class="mb-3">
                <i class="fas fa-language me-2"></i>Xem sản phẩm bằng ngôn ngữ khác:
            </h5>
            <div class="btn-group" role="group">
                <c:forEach var="lang" items="${languages}">
                    <a href="products?action=detail&id=${product.productId}&lang=${lang.languageId}" 
                       class="btn ${currentLang == lang.languageId ? 'btn-primary' : 'btn-outline-primary'}">
                        ${lang.language}
                    </a>
                </c:forEach>
            </div>
        </div>

        <!-- Product Information Grid -->
        <div class="row">
            <!-- Price Information -->
            <div class="col-lg-4 col-md-6 mb-4">
                <div class="price-display">
                    <i class="fas fa-dollar-sign fa-3x mb-3"></i>
                    <h3 class="fw-bold mb-2">Giá bán</h3>
                    <h2 class="fw-bold">
                        <fmt:formatNumber value="${product.price}" pattern="#,##0.00"/> VNĐ
                    </h2>
                </div>
            </div>

            <!-- Category Information -->
            <div class="col-lg-4 col-md-6 mb-4">
                <div class="category-info">
                    <i class="fas fa-tags fa-3x mb-3"></i>
                    <h3 class="fw-bold mb-2">Danh mục</h3>
                    <h4 class="mb-2">${product.productCategory.categoryName}</h4>
                    <small>ID: #${product.productCategory.productCategoryId}</small>
                </div>
            </div>

            <!-- Shipping Information -->
            <div class="col-lg-4 col-md-12 mb-4">
                <div class="shipping-info ${product.productCategory.canBeShipped ? 'can-ship' : 'no-ship'}">
                    <i class="fas fa-${product.productCategory.canBeShipped ? 'truck' : 'times-circle'} fa-3x mb-3"></i>
                    <h3 class="fw-bold mb-2">Giao hàng</h3>
                    <h4>
                        <c:choose>
                            <c:when test="${product.productCategory.canBeShipped}">
                                <i class="fas fa-check-circle me-2"></i>Có thể giao hàng
                            </c:when>
                            <c:otherwise>
                                <i class="fas fa-times-circle me-2"></i>Không thể giao hàng
                            </c:otherwise>
                        </c:choose>
                    </h4>
                </div>
            </div>
        </div>

        <!-- Additional Information -->
        <div class="row">
            <div class="col-md-6">
                <div class="info-card">
                    <h5 class="fw-bold mb-3">
                        <i class="fas fa-weight-hanging me-2 text-primary"></i>Thông tin trọng lượng
                    </h5>
                    <div class="d-flex justify-content-between align-items-center">
                        <span class="text-muted">Trọng lượng sản phẩm:</span>
                        <span class="fw-bold fs-5">
                            <c:choose>
                                <c:when test="${product.weight != null}">
                                    <fmt:formatNumber value="${product.weight}" pattern="#,##0.##"/> kg
                                </c:when>
                                <c:otherwise>
                                    <span class="text-muted">Không xác định</span>
                                </c:otherwise>
                            </c:choose>
                        </span>
                    </div>
                </div>
            </div>

            <div class="col-md-6">
                <div class="info-card">
                    <h5 class="fw-bold mb-3">
                        <i class="fas fa-info-circle me-2 text-primary"></i>Thông tin bổ sung
                    </h5>
                    <div class="row text-center">
                        <div class="col-6 border-end">
                            <i class="fas fa-calendar-alt fa-2x text-muted mb-2"></i>
                            <p class="mb-0 text-muted small">Ngày tạo</p>
                            <span class="fw-bold">Không có dữ liệu</span>
                        </div>
                        <div class="col-6">
                            <i class="fas fa-edit fa-2x text-muted mb-2"></i>
                            <p class="mb-0 text-muted small">Lần cập nhật cuối</p>
                            <span class="fw-bold">Không có dữ liệu</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Action Buttons -->
        <div class="row mt-4">
            <div class="col-12 text-center">
                <div class="btn-group btn-group-lg" role="group">
                    <a href="products?lang=${currentLang}" class="btn btn-secondary">
                        <i class="fas fa-arrow-left me-2"></i>Quay lại danh sách
                    </a>
                    <a href="products?action=edit&id=${product.productId}" class="btn btn-warning">
                        <i class="fas fa-edit me-2"></i>Chỉnh sửa sản phẩm
                    </a>
                    <button onclick="deleteProduct(${product.productId})" class="btn btn-danger">
                        <i class="fas fa-trash me-2"></i>Xóa sản phẩm
                    </button>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        function deleteProduct(productId) {
            if (confirm('Bạn có chắc chắn muốn xóa sản phẩm này?\nHành động này không thể hoàn tác!')) {
                var form = document.createElement('form');
                form.method = 'POST';
                form.action = 'products';
                
                var actionInput = document.createElement('input');
                actionInput.type = 'hidden';
                actionInput.name = 'action';
                actionInput.value = 'delete';
                
                var idInput = document.createElement('input');
                idInput.type = 'hidden';
                idInput.name = 'id';
                idInput.value = productId;
                
                form.appendChild(actionInput);
                form.appendChild(idInput);
                document.body.appendChild(form);
                form.submit();
            }
        }
    </script>
</body>
</html>