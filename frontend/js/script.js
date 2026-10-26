// ==========================================================================
// SVVV ERP — Mobile Sliding Drawer Navigation Controller
// ==========================================================================
document.addEventListener("DOMContentLoaded", function () {
    // Student Sidebar Toggle
    const mobileMenuBtn = document.getElementById("mobile-menu-btn");
    const sidebar = document.querySelector(".sidebar");
    const sidebarOverlay = document.getElementById("sidebar-overlay");

    if (mobileMenuBtn && sidebar) {
        mobileMenuBtn.addEventListener("click", function (e) {
            e.stopPropagation();
            sidebar.classList.toggle("active");
            if (sidebarOverlay) sidebarOverlay.classList.toggle("active");
        });

        if (sidebarOverlay) {
            sidebarOverlay.addEventListener("click", function () {
                sidebar.classList.remove("active");
                sidebarOverlay.classList.remove("active");
            });
        }
    }

    // Admin Sidebar Toggle
    const adminMenuBtn = document.getElementById("admin-mobile-menu-btn");
    const adminSidebar = document.querySelector(".admin-sidebar") || document.querySelector("#admin-tab-sidebar");
    const adminOverlay = document.getElementById("admin-sidebar-overlay");

    if (adminMenuBtn && adminSidebar) {
        adminMenuBtn.addEventListener("click", function (e) {
            e.stopPropagation();
            adminSidebar.classList.toggle("active");
            if (adminOverlay) adminOverlay.classList.toggle("active");
        });

        if (adminOverlay) {
            adminOverlay.addEventListener("click", function () {
                adminSidebar.classList.remove("active");
                adminOverlay.classList.remove("active");
            });
        }
    }

    // Auto-close sidebar on mobile when any sidebar navigation link is clicked
    const allSidebarLinks = document.querySelectorAll(".sidebar-link, .nav-link, .nav-item a, .nav-item");
    allSidebarLinks.forEach(link => {
        link.addEventListener("click", () => {
            if (window.innerWidth <= 992) {
                if (sidebar) sidebar.classList.remove("active");
                if (sidebarOverlay) sidebarOverlay.classList.remove("active");
                if (adminSidebar) adminSidebar.classList.remove("active");
                if (adminOverlay) adminOverlay.classList.remove("active");
            }
        });
    });
});

// ==========================================================================
// Global Module View Switcher
// ==========================================================================
function switchView(viewName) {
    if (!viewName) return;
    const path = window.location.pathname.toLowerCase();
    if (viewName === 'hostel') {
        if (!path.endsWith('hostel.html')) window.location.href = 'hostel.html';
    } else if (viewName === 'fees') {
        if (!path.endsWith('fees.html')) window.location.href = 'fees.html';
    } else if (viewName === 'examination') {
        if (!path.endsWith('examination.html')) window.location.href = 'examination.html';
    } else {
        window.location.href = 'student_dashboard.html#' + viewName;
    }
}
