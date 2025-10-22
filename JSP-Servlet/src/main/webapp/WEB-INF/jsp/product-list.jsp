<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh sách sản phẩm - Multi-language Product System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .language-selector {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border-radius: 10px;
            padding: 15px;
            margin-bottom: 20px;
        }
        .card-product {
            transition: transform 0.3s ease;
            border-radius: 15px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }
        .card-product:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 25px rgba(0,0,0,0.15);
        }
        .price-tag {
            background: linear-gradient(45deg, #ff6b6b, #ee5a24);
            color: white;
            padding: 8px 15px;
            border-radius: 25px;
            font-weight: bold;
        }
        .category-badge {
            background: linear-gradient(45deg, #4834d4, #686de0);
            color: white;
            padding: 5px 12px;
            border-radius: 15px;
            font-size: 0.85em;
        }
        .shipping-status {
            padding: 3px 8px;
            border-radius: 10px;
            font-size: 0.8em;
        }
        .can-ship {
            background-color: #d4edda;
            color: #155724;
        }
        .no-ship {
            background-color: #f8d7da;
            color: #721c24;
        }
    </style>
</head>
<body class="bg-light">
    <div class="container-fluid py-4">
        <!-- Header -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <h1 class="display-4 fw-bold text-primary">
                            <i class="fas fa-boxes me-3"></i>Hệ thống sản phẩm đa ngôn ngữ
                        </h1>
                         <p class="lead text-muted">Quản lý sản phẩm với hỗ trợ đa ngôn ngữ</p>
                        <div>
						    <form action="products" method="get" class="d-flex mt-3">
						        <input type="hidden" name="action" value="search">
						        <input type="text" name="keyword" class="form-control me-2" 
						               placeholder="Tìm kiếm sản phẩm..." 
						               value="${param.keyword != null ? param.keyword : ''}">
						        <button type="submit" class="btn btn-outline-primary">
						            <i class="fas fa-search"></i> Tìm kiếm
						        </button>
						    </form>
						</div>                  
                    </div>
                    <div>
                        <a href="products?action=add" class="btn btn-success btn-lg">
                            <i class="fas fa-plus me-2"></i>Thêm sản phẩm
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Language Selector -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="language-selector">
                    <div class="d-flex align-items-center">
                        <h5 class="text-white mb-0 me-3">
                            <i class="fas fa-language me-2"></i>Chọn ngôn ngữ:
                        </h5>
                        <div class="btn-group" role="group">
                            <c:forEach var="lang" items="${languages}">
                                <a href="products?lang=${lang.languageId}" 
                                   class="btn ${currentLang == lang.languageId ? 'btn-light' : 'btn-outline-light'} btn-sm">
                                    ${lang.language}
                                </a>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Statistics -->
        <div class="row mb-4">
            <div class="col-md-4">
                <div class="card border-0 bg-primary text-white">
                    <div class="card-body">
                        <div class="d-flex align-items-center">
                            <i class="fas fa-box fa-2x me-3"></i>
                            <div>
                                <h3 class="mb-0">${products.size()}</h3>
                                <p class="mb-0">Tổng sản phẩm</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card border-0 bg-success text-white">
                    <div class="card-body">
                        <div class="d-flex align-items-center">
                            <i class="fas fa-shipping-fast fa-2x me-3"></i>
                            <div>
                                <h3 class="mb-0">
                                    <c:set var="shippableCount" value="0"/>
                                    <c:forEach var="product" items="${products}">
                                        <c:if test="${product.productCategory.canBeShipped}">
                                            <c:set var="shippableCount" value="${shippableCount + 1}"/>
                                        </c:if>
                                    </c:forEach>
                                    ${shippableCount}
                                </h3>
                                <p class="mb-0">Có thể giao hàng</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card border-0 bg-info text-white">
                    <div class="card-body">
                        <div class="d-flex align-items-center">
                            <i class="fas fa-globe fa-2x me-3"></i>
                            <div>
                                <h3 class="mb-0">${languages.size()}</h3>
                                <p class="mb-0">Ngôn ngữ hỗ trợ</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Products Grid -->
        <div class="row">
            <c:forEach var="product" items="${products}">
                <div class="col-lg-4 col-md-6 mb-4">
                    <div class="card card-product h-100 border-0">
                        <div class="card-header bg-transparent border-0 d-flex justify-content-between align-items-center">
                            <span class="category-badge">
                                <i class="fas fa-tag me-1"></i>
                                ${product.productCategory.categoryName}
                            </span>
                            <span class="shipping-status ${product.productCategory.canBeShipped ? 'can-ship' : 'no-ship'}">
                                <i class="fas fa-${product.productCategory.canBeShipped ? 'truck' : 'times'} me-1"></i>
                                ${product.productCategory.canBeShipped ? 'Có thể giao' : 'Không giao'}
                            </span>
                        </div>
                        
                        <div class="card-body">
                            <h5 class="card-title fw-bold text-dark">
                                <i class="fas fa-cube me-2 text-primary"></i>
                                ${product.productName}
                            </h5>
                            
                            <p class="card-text text-muted mb-3">
                                ${product.productDescription}
                            </p>
                            
                            <div class="row text-center mb-3">
                                <div class="col-6">
                                    <div class="border-end">
                                        <h6 class="text-muted mb-1">ID Sản phẩm</h6>
                                        <span class="fw-bold">#${product.productId}</span>
                                    </div>
                                </div>
                                <div class="col-6">
                                    <h6 class="text-muted mb-1">Trọng lượng</h6>
                                    <span class="fw-bold">
                                        <c:choose>
                                            <c:when test="${product.weight != null}">
                                                <fmt:formatNumber value="${product.weight}" pattern="#,##0.##"/> kg
                                            </c:when>
                                            <c:otherwise>N/A</c:otherwise>
                                        </c:choose>
                                    </span>
                                </div>
                            </div>
                            
                            <div class="text-center mb-3">
                                <span class="price-tag">
                                    <i class="fas fa-dollar-sign me-1"></i>
                                    <fmt:formatNumber value="${product.price}" pattern="#,##0.00"/> VNĐ
                                </span>
                            </div>
                        </div>
                        
                        <div class="card-footer bg-transparent border-0">
                            <div class="d-grid gap-2 d-md-block text-center">
                                <a href="products?action=detail&id=${product.productId}&lang=${currentLang}" 
                                   class="btn btn-primary btn-sm">
                                    <i class="fas fa-eye me-1"></i>Chi tiết
                                </a>
                                <a href="products?action=edit&id=${product.productId}" 
                                   class="btn btn-warning btn-sm">
                                    <i class="fas fa-edit me-1"></i>Sửa
                                </a>
                                <button onclick="deleteProduct(${product.productId})" 
                                        class="btn btn-danger btn-sm">
                                    <i class="fas fa-trash me-1"></i>Xóa
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <c:if test="${empty products}">
            <div class="row">
                <div class="col-12 text-center py-5">
                    <i class="fas fa-box-open fa-5x text-muted mb-3"></i>
                    <h3 class="text-muted">Không có sản phẩm nào</h3>
                    <p class="text-muted">Hãy thêm sản phẩm đầu tiên của bạn!</p>
                    <a href="products?action=add" class="btn btn-primary btn-lg">
                        <i class="fas fa-plus me-2"></i>Thêm sản phẩm đầu tiên
                    </a>
                </div>
            </div>
        </c:if>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        function deleteProduct(productId) {
            if (confirm('Bạn có chắc chắn muốn xóa sản phẩm này?')) {
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