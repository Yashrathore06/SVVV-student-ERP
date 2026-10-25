// Base API Configuration
const API_BASE_URL = window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1'
  ? 'http://localhost:8080/api'
  : '/api';

// DOM Elements
const roleStudentBtn = document.getElementById('roleStudent');
const roleAdminBtn = document.getElementById('roleAdmin');
const usernameLabel = document.getElementById('usernameLabel');
const usernameInput = document.getElementById('usernameInput');
const passwordInput = document.getElementById('passwordInput');
const togglePasswordBtn = document.getElementById('togglePasswordBtn');
const eyeIcon = document.getElementById('eyeIcon');
const loginForm = document.getElementById('loginForm');
const submitBtn = document.getElementById('submitBtn');
const btnText = document.getElementById('btnText');
const alertBox = document.getElementById('alertBox');
const alertMessage = document.getElementById('alertMessage');

// Modals
const infoModal = document.getElementById('infoModal');
const modalTitle = document.getElementById('modalTitle');
const modalBody = document.getElementById('modalBody');
const modalClose = document.getElementById('modalClose');
const modalOkBtn = document.getElementById('modalOkBtn');
const forgotPasswordLink = document.getElementById('forgotPasswordLink');
const supportLink = document.getElementById('supportLink');

// State
let currentRole = 'STUDENT';

// ==========================================================================
// Role Switching Logic
// ==========================================================================
function setRole(role) {
    currentRole = role;
    hideAlert();

    if (role === 'STUDENT') {
        roleStudentBtn.classList.add('active');
        roleAdminBtn.classList.remove('active');
        usernameLabel.textContent = 'Student ID / Official Email';
        usernameInput.placeholder = 'Enter your enrollment number or email';
    } else {
        roleAdminBtn.classList.add('active');
        roleStudentBtn.classList.remove('active');
        usernameLabel.textContent = 'Admin Username / Email';
        usernameInput.placeholder = 'Enter your admin username or email';
    }
    usernameInput.focus();
}

roleStudentBtn.addEventListener('click', () => setRole('STUDENT'));
roleAdminBtn.addEventListener('click', () => setRole('ADMIN'));

// ==========================================================================
// Password Show / Hide Toggle
// ==========================================================================
let isPasswordVisible = false;

togglePasswordBtn.addEventListener('click', () => {
    isPasswordVisible = !isPasswordVisible;
    passwordInput.type = isPasswordVisible ? 'text' : 'password';

    if (isPasswordVisible) {
        eyeIcon.innerHTML = `
            <path stroke-linecap="round" stroke-linejoin="round" d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.88 9.88l-3.29-3.29m7.532 7.532l3.29 3.29M3 3l18 18" />
        `;
    } else {
        eyeIcon.innerHTML = `
            <path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"/>
            <path stroke-linecap="round" stroke-linejoin="round" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"/>
        `;
    }
});

// ==========================================================================
// Alerts Handling
// ==========================================================================
function showAlert(message, type = 'error') {
    alertBox.className = `alert-box ${type}`;
    alertMessage.textContent = message;
    alertBox.style.display = 'flex';
}

function hideAlert() {
    alertBox.style.display = 'none';
}

// ==========================================================================
// Form Submission & Authentication
// ==========================================================================
loginForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    hideAlert();

    const username = usernameInput.value.trim();
    const password = passwordInput.value.trim();

    if (!username) {
        showAlert('Please enter your Student ID or Username.', 'error');
        usernameInput.focus();
        return;
    }

    if (!password) {
        showAlert('Please enter your password.', 'error');
        passwordInput.focus();
        return;
    }

    // Set Loading State
    submitBtn.disabled = true;
    btnText.textContent = 'Verifying credentials...';

    try {
        const response = await fetch(`${API_BASE_URL}/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username: username,
                password: password,
                role: currentRole
            })
        });

        const data = await response.json();

        if (response.ok && data.success) {
            const userRole = (data.role || '').toUpperCase();

            // Strict Role Check Guard
            if (currentRole === 'STUDENT' && userRole.includes('ADMIN')) {
                showAlert('Invalid credentials or role mismatch. Please select Admin Login.', 'error');
                return;
            }
            if (currentRole === 'ADMIN' && !userRole.includes('ADMIN')) {
                showAlert('Access Denied: Student credentials cannot access Admin Console.', 'error');
                return;
            }

            showAlert(data.message || 'Login successful! Redirecting...', 'success');
            
            // Clean User Session Object
            const sessionUser = {
                id: data.userId || data.id,
                userId: data.userId || data.id,
                studentId: data.studentId || data.userId || data.id,
                username: data.username || data.enrollmentNo || username,
                role: data.role || (currentRole === 'ADMIN' ? 'ROLE_ADMIN' : 'ROLE_STUDENT'),
                name: data.name,
                email: data.email,
                enrollmentNo: data.enrollmentNo || data.username || username,
                branch: data.branch,
                semester: data.semester,
                cgpa: data.cgpa,
                backlogs: data.backlogs
            };

            // Store cleanly in localStorage as requested: { id: <user_id>, username: <enrollment_no>, role: <ROLE_STUDENT or ROLE_ADMIN> }
            localStorage.setItem('user', JSON.stringify(sessionUser));
            localStorage.setItem('loggedInUser', JSON.stringify(sessionUser));
            localStorage.setItem('currentUser', JSON.stringify(sessionUser));
            localStorage.setItem('svvv_user', JSON.stringify(sessionUser));
            sessionStorage.setItem('user', JSON.stringify(sessionUser));
            sessionStorage.setItem('loggedInUser', JSON.stringify(sessionUser));
            sessionStorage.setItem('svvv_user', JSON.stringify(sessionUser));

            // Short delay for smooth feedback
            setTimeout(() => {
                const finalRole = (sessionUser.role || '').toUpperCase();
                if (finalRole.includes('ADMIN')) {
                    window.location.href = 'admin-dashboard.html';
                } else {
                    window.location.href = 'student-dashboard.html';
                }
            }, 600);
        } else {
            const errMsg = data.message || 'Invalid Enrollment Number / Username or Password.';
            showAlert(errMsg, 'error');
        }
    } catch (err) {
        showAlert('Cannot connect to ERP Server. Please ensure the backend is running.', 'error');
        console.error('Login error:', err);
    } finally {
        submitBtn.disabled = false;
        btnText.textContent = 'Sign in to ERP Portal';
    }
});

// ==========================================================================
// Help & Forgot Password Modals
// ==========================================================================
function openModal(title, content) {
    modalTitle.textContent = title;
    modalBody.innerHTML = content;
    infoModal.classList.add('open');
}

function closeModal() {
    infoModal.classList.remove('open');
}

modalClose.addEventListener('click', closeModal);
modalOkBtn.addEventListener('click', closeModal);
infoModal.addEventListener('click', (e) => {
    if (e.target === infoModal) closeModal();
});

forgotPasswordLink.addEventListener('click', () => {
    openModal(
        'Password Reset Support',
        `To reset your ERP portal password, please contact the University Examination & IT Cell with your SVVV Student ID Card.<br><br>
        📧 <strong>helpdesk@svvv.edu.in</strong><br>
        📍 <strong>IT Center, Ground Floor, Admin Block</strong>`
    );
});

supportLink.addEventListener('click', () => {
    openModal(
        'SVVV IT & ERP Support',
        `For technical assistance regarding ERP login, portal access, or profile updates:<br><br>
        📧 <strong>erp.support@svvv.edu.in</strong><br>
        📞 <strong>+91 731 2729071 / Ext. 104</strong><br>
        ⏰ <strong>Working Hours: Monday to Friday, 9:00 AM - 5:00 PM</strong>`
    );
});
