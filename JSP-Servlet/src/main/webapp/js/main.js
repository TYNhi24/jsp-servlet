/** 
 * Multi-Language Product System - Main JavaScript File
 * 
 * JavaScript phía client (frontend).
 */

// Global variables
let isLoading = false;

// DOM Content Loaded Event
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();//khởi tạo giao diện, hiệu ứng.
    setupEventListeners();//gắn sự kiện cho nút bấm, form, tìm kiếm.
    addAnimations();// (chưa viết, để trống).
});

/**
 * Initialize the application
 */
function initializeApp() {
    console.log('🚀 Multi-Language Product System initialized');
    
    // Check if we're on the product list page
    if (window.location.pathname.includes('products') || window.location.search.includes('products')) {
        initializeProductList();//gọi hàm khởi tạo danh sách sản phẩm.
    }
    
    // Thêm hiệu ứng fade-in cho từng thẻ .card.
    const cards = document.querySelectorAll('.card, .feature-card, .stat-card');
    cards.forEach((card, index) => {
        setTimeout(() => {
            card.classList.add('fade-in');
        }, index * 100);
    });
}

/**
 * Setup event listeners
 */
function setupEventListeners() {
    // Language selection buttons
    const languageButtons = document.querySelectorAll('.language-selector .btn');
    languageButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            showLoading(e.target);
        });
    });
    
    // Product action buttons
    setupProductActionListeners();
    
    // Form validation
    setupFormValidation();
    
    // Search functionality
    setupSearchFunctionality();
    
    // Smooth scrolling for anchor links
    setupSmoothScrolling();
}

/**
 * Setup product action listeners
 * chuẩn hóa cách xử lý click, tránh lỗi JS inline.
 */
function setupProductActionListeners() {
  const deleteButtons = document.querySelectorAll('[onclick*="deleteProduct"]');
  deleteButtons.forEach(button => {
    button.removeAttribute('onclick');
    button.addEventListener('click', function (e) {
      e.preventDefault();

      const productId =
        this.getAttribute('data-product-id') ||
        (this.onclick && this.onclick.toString().match(/\d+/) && this.onclick.toString().match(/\d+/)[0]);

      if (productId) {
        deleteProduct(productId);
      }
    });
  });
   
    // View details buttons
    const detailButtons = document.querySelectorAll('[href*="action=detail"]');
    detailButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            showLoading(e.target);
        });
    });
}

/**
 * Setup form validation
 * Giúp tránh người dùng gửi dữ liệu sai, giống validate bên server
 */
function setupFormValidation() {
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            if (!validateForm(this)) {
                e.preventDefault();
                showAlert('Vui lòng kiểm tra lại thông tin đã nhập!', 'warning');
            } else {
                showLoading(form.querySelector('[type="submit"]'));
            }
        });
    });
}

/**
 * Setup search functionality
 * Tối ưu hiệu suất tìm kiếm realtime.??
 */
function setupSearchFunctionality() {
    const searchInput = document.querySelector('#searchInput');
    if (searchInput) {
        searchInput.addEventListener('input', debounce(handleSearch, 300));
    }
}

/**
 * Setup smooth scrolling
 */
function setupSmoothScrolling() {
    const anchors = document.querySelectorAll('a[href^="#"]');
    anchors.forEach(anchor => {
        anchor.addEventListener('click', function(e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
}

/**
 * Initialize product list functionality
 * Thêm hiệu ứng “nổi lên” khi rê chuột lên card sản phẩm.
 */
function initializeProductList() {
    // Add hover effects to product cards
    const productCards = document.querySelectorAll('.card-product');
    productCards.forEach(card => {
        card.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-5px)';
        });
        
        card.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0)';
        });
    });
    
    // Initialize tooltips
    initializeTooltips();//chưa viết
}

/**
 * Delete product with confirmation
 */
function deleteProduct(productId) {
    if (!productId) {
        showAlert('ID sản phẩm không hợp lệ!', 'error');
        return;
    }
    
    // Show custom confirmation dialog
    showConfirmDialog(
        'Xác nhận xóa sản phẩm',
        'Bạn có chắc chắn muốn xóa sản phẩm này? Hành động này không thể hoàn tác!',
        function() {
            // Create form and submit
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = 'products';
            
            const actionInput = document.createElement('input');
            actionInput.type = 'hidden';
            actionInput.name = 'action';
            actionInput.value = 'delete';
            
            const idInput = document.createElement('input');
            idInput.type = 'hidden';
            idInput.name = 'id';
            idInput.value = productId;
            
            form.appendChild(actionInput);
            form.appendChild(idInput);
            document.body.appendChild(form);
            
            showLoading();
            form.submit();
        }
    );
}

/**
 * Show loading animation
 * 
 * Giúp người dùng biết thao tác đang chạy.
 */
function showLoading(element) {
    if (isLoading) return;
    
    isLoading = true;
    
    if (element) {
        const originalText = element.innerHTML;
        element.innerHTML = '<span class="loading"></span> Đang xử lý...';
        element.disabled = true;
        
        // Reset after 10 seconds (fallback)
        setTimeout(() => {
            if (element && element.innerHTML.includes('Đang xử lý')) {
                element.innerHTML = originalText;
                element.disabled = false;
                isLoading = false;
            }
        }, 10000);
    } else {
        // Show global loading overlay
        showLoadingOverlay();
    }
}

/**
 * Show loading overlay
 */
function showLoadingOverlay() {
    const overlay = document.createElement('div');
    overlay.id = 'loading-overlay';
    overlay.innerHTML = '<div class="spinner"></div>';
    document.body.appendChild(overlay);
}

// Debounce helper
function debounce(func, delay) {
    let timeout;
    return function(...args) {
        clearTimeout(timeout);
        timeout = setTimeout(() => func.apply(this, args), delay);
    };
}
/**
 * 
 * debounce(func, delay) → hạn chế tần suất gọi hàm.

showAlert(msg, type) → hiển thị thông báo.

showConfirmDialog(title, msg, onConfirm) → hộp thoại xác nhận.

validateForm() → tạm trả về true (chưa viết thật).

handleSearch() → placeholder (chưa viết).
 */
// Dummy implementations for missing functions (avoid JS errors)
function addAnimations() {}
function initializeTooltips() {}
function validateForm() { return true; }
function showAlert(msg, type) {
  if (type === 'error') alert('❌ ' + msg);
  else if (type === 'success') alert('✅ ' + msg);
  else alert(msg);
}

function showConfirmDialog(title, msg, onConfirm) {
  console.log("Confirm Dialog:", title); 
  const userConfirmed = confirm(msg);

  if (userConfirmed && typeof onConfirm === "function") {
    onConfirm(); 
  }
}

function handleSearch() {}
