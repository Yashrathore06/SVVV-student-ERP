// ==========================================================================
// SVVV ERP — Admin Management Console Single-Page Application (SPA) Controller
// ==========================================================================

// Base API Configuration
const API_BASE_URL = window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1'
  ? 'http://localhost:8080/api'
  : '/api';

// Current Admin User & Global State
let currentAdminUser = null;
let bonafideRequestsList = [];
let activeBonafideFilter = 'ALL';
let adminPlacementDrives = [];
let hostelApplicationsList = [];
let studentDirectoryList = [];
let filteredStudentDirectory = [];
let genericElectivesList = [];
let announcementHistory = [
    {
        id: 1,
        title: 'Schedule for TCS Digital Campus Recruitment Assessment',
        audience: 'B.Tech Final Year (2026-2027 Batch)',
        content: 'Online coding assessment is confirmed for 24th May, 10:00 AM at the Main Computing Center (Lab 3 & 4). Students must carry their institutional ID badges and updated resume copy.',
        postedAt: 'Today, 09:30 AM',
        author: 'Director — Training & Placement Cell'
    },
    {
        id: 2,
        title: 'Autumn 2026 Generic Elective Phase-II Course Lock Deadline',
        audience: 'All Engineering Students',
        content: 'Students who have not locked their Generic Elective preferences must finalize their choices before Friday, 5:00 PM. Unselected candidates will be allocated automatically based on vacancy.',
        postedAt: 'Yesterday, 04:15 PM',
        author: 'Dean — Academics'
    },
    {
        id: 3,
        title: 'Hostel Monsoon Maintenance Clearance & Room Inspection',
        audience: 'All Enrolled Students (Campus Wide)',
        content: 'Residential warden team will conduct the quarterly electrical and fire-safety inspection for all hostel wings this weekend. Please coordinate with floor prefects.',
        postedAt: 'May 10, 2026',
        author: 'Chief Hostel Warden'
    }
];

// Seed Data for Bonafide Requests
const defaultRequestsSeed = [
    {
        id: 101,
        studentName: 'Yash Prajapati',
        branch: 'B.Tech CSE, Sem VII',
        enrollmentNo: '0827CS211001',
        service: 'Bonafide Certificate',
        purpose: 'Bonafide Certificate',
        submittedAt: 'Today, 10:24 AM',
        status: 'PENDING',
        type: 'BONAFIDE'
    },
    {
        id: 102,
        studentName: 'Priya Sharma',
        branch: 'B.Tech IT, Sem VII',
        enrollmentNo: '0827IT211045',
        service: 'Bonafide Certificate',
        purpose: 'Bonafide Certificate',
        submittedAt: 'Today, 09:15 AM',
        status: 'PENDING',
        type: 'BONAFIDE'
    },
    {
        id: 103,
        studentName: 'Rohan Mehta',
        branch: 'B.Tech ME, Sem VII',
        enrollmentNo: '0827ME211019',
        service: 'Bonafide Certificate',
        purpose: 'Bonafide Certificate',
        submittedAt: 'Yesterday, 4:15 PM',
        status: 'PENDING',
        type: 'BONAFIDE'
    },
    {
        id: 104,
        studentName: 'Ananya Singh',
        branch: 'B.Tech CSE, Sem V',
        enrollmentNo: '23100BTCSE15208',
        service: 'Bonafide Certificate',
        purpose: 'Bonafide Certificate',
        submittedAt: 'Yesterday, 2:30 PM',
        status: 'PENDING',
        type: 'BONAFIDE'
    },
    {
        id: 105,
        studentName: 'Kavita Deshmukh',
        branch: 'B.Tech ECE, Sem V',
        enrollmentNo: '0827EC221008',
        service: 'Bonafide Certificate',
        purpose: 'Bonafide Certificate',
        submittedAt: 'May 12, 2026',
        status: 'PENDING',
        type: 'BONAFIDE'
    }
];

// Seed Data for Generic Elective Courses (4 Courses • 30 Seats Each = 120 Capacity)
const defaultElectivesSeed = [
    { id: 1, courseCode: 'CS-701', courseName: 'Deep Learning & Neural Networks', department: 'Computer Science & Engineering', capacity: 30, enrolled: 22, remainingSeats: 8, instructor: 'Dr. R. Verma' },
    { id: 2, courseCode: 'CS-704', courseName: 'Cloud Architecture & Services', department: 'Computer Science & Engineering', capacity: 30, enrolled: 25, remainingSeats: 5, instructor: 'Prof. N. Patel' },
    { id: 3, courseCode: 'CS-705', courseName: 'Cyber Security & Digital Forensics', department: 'Computer Science & Engineering', capacity: 30, enrolled: 18, remainingSeats: 12, instructor: 'Dr. S. Mukherjee' },
    { id: 4, courseCode: 'CS-708', courseName: 'Full Stack Web Engineering', department: 'Computer Science & Engineering', capacity: 30, enrolled: 10, remainingSeats: 20, instructor: 'Prof. K. Mehta' }
];

// Seed Data for Student Directory
const defaultStudentsSeed = [
    { id: 1, enrollmentNo: '0827CS211001', name: 'Yash Prajapati', email: 'yash.cs21@svvv.edu.in', phone: '+91 98260 11223', branch: 'Computer Science & Engineering', semester: 7, cgpa: 8.45, backlogs: 0, gender: 'Male' },
    { id: 2, enrollmentNo: '0827IT211045', name: 'Priya Sharma', email: 'priya.it21@svvv.edu.in', phone: '+91 94250 44556', branch: 'Information Technology', semester: 7, cgpa: 8.12, backlogs: 0, gender: 'Female' },
    { id: 3, enrollmentNo: '23100BTCSE15208', name: 'Yash Rathore', email: 'yash.r23@svvv.edu.in', phone: '+91 91110 99887', branch: 'Computer Science & Engineering', semester: 7, cgpa: 7.85, backlogs: 0, gender: 'Male' },
    { id: 4, enrollmentNo: '0827EC221008', name: 'Kavita Deshmukh', email: 'kavita.ec22@svvv.edu.in', phone: '+91 98270 33441', branch: 'Electronics & Communication', semester: 5, cgpa: 8.60, backlogs: 0, gender: 'Female' },
    { id: 5, enrollmentNo: '0827ME211019', name: 'Rohan Mehta', email: 'rohan.me21@svvv.edu.in', phone: '+91 97550 66778', branch: 'Mechanical Engineering', semester: 7, cgpa: 7.20, backlogs: 1, gender: 'Male' },
    { id: 6, enrollmentNo: '0827CS221089', name: 'Ananya Chouhan', email: 'ananya.cs22@svvv.edu.in', phone: '+91 99810 55442', branch: 'Computer Science & Engineering', semester: 5, cgpa: 9.10, backlogs: 0, gender: 'Female' },
    { id: 7, enrollmentNo: '0827CE211012', name: 'Aditya Tiwari', email: 'aditya.ce21@svvv.edu.in', phone: '+91 98930 22114', branch: 'Civil Engineering', semester: 7, cgpa: 6.95, backlogs: 2, gender: 'Male' },
    { id: 8, enrollmentNo: '0827IT221034', name: 'Sneha Patel', email: 'sneha.it22@svvv.edu.in', phone: '+91 91790 88771', branch: 'Information Technology', semester: 5, cgpa: 8.35, backlogs: 0, gender: 'Female' },
    { id: 9, enrollmentNo: '0827CS231102', name: 'Manish Verma', email: 'manish.cs23@svvv.edu.in', phone: '+91 93010 44332', branch: 'Computer Science & Engineering', semester: 3, cgpa: 7.60, backlogs: 0, gender: 'Male' },
    { id: 10, enrollmentNo: '0827EC211041', name: 'Gaurav Kulkarni', email: 'gaurav.ec21@svvv.edu.in', phone: '+91 94060 11998', branch: 'Electronics & Communication', semester: 7, cgpa: 7.40, backlogs: 0, gender: 'Male' }
];

// ==========================================================================
// Initialization on Page Load
// ==========================================================================
document.addEventListener('DOMContentLoaded', () => {
    checkAdminSession();
    initTabNavigation();
    initAdminMobileDrawerNavigation();
    loadDashboardStats();
    loadBonafideRequests();
    loadHostelApplications();
    loadAdminPlacementDrives();
    loadElectivesData();
    loadStudentDirectory();
    loadActiveAnnouncements();
    loadAnnouncementHistory();
});

// ==========================================================================
// Mobile Sidebar Drawer Navigation Logic (Admin Console)
// ==========================================================================
function initAdminMobileDrawerNavigation() {
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
    const allSidebarLinks = document.querySelectorAll(".admin-sidebar .sidebar-link, .admin-sidebar .nav-link, .admin-sidebar .nav-item");
    allSidebarLinks.forEach(link => {
        link.addEventListener("click", () => {
            if (window.innerWidth <= 992) {
                if (adminSidebar) adminSidebar.classList.remove("active");
                if (adminOverlay) adminOverlay.classList.remove("active");
            }
        });
    });
}

// ==========================================================================
// 1. Session & Identity
// ==========================================================================
function checkAdminSession() {
    const sessionData = localStorage.getItem('svvv_user');
    if (sessionData) {
        try {
            currentAdminUser = JSON.parse(sessionData);
            if (currentAdminUser.name) {
                const adminNameEl = document.getElementById('adminName');
                if (adminNameEl) adminNameEl.textContent = currentAdminUser.name;
            }
        } catch (e) {
            console.error('Session parse error:', e);
        }
    }
}

// ==========================================================================
// 2. SPA TAB NAVIGATION ARCHITECTURE (NO PAGE REFRESH)
// ==========================================================================
function initTabNavigation() {
    // Add event listeners to all sidebar nav links
    const sidebarLinks = document.querySelectorAll('.admin-sidebar .nav-link, .admin-sidebar .nav-item[data-tab]');
    
    sidebarLinks.forEach(link => {
        link.addEventListener('click', function (e) {
            e.preventDefault();
            const targetTab = this.getAttribute('data-tab') || this.getAttribute('href');
            if (targetTab) {
                switchAdminTab(targetTab, this);
            }
        });
    });

    // Handle deep links in URL hash if present (e.g. #tab-student-directory)
    if (window.location.hash) {
        const hashTab = window.location.hash.replace('#', '');
        if (document.getElementById(hashTab)) {
            switchAdminTab(hashTab);
        }
    }
}

function switchAdminTab(targetTabId, clickedEl = null) {
    if (!targetTabId) return;

    if (window.innerWidth <= 992) {
        const adminSidebar = document.querySelector(".admin-sidebar") || document.querySelector("#admin-tab-sidebar");
        const adminOverlay = document.getElementById("admin-sidebar-overlay");
        if (adminSidebar) adminSidebar.classList.remove("active");
        if (adminOverlay) adminOverlay.classList.remove("active");
    }

    // Normalize target identifier
    let tabId = targetTabId.replace('#', '');
    if (tabId === 'overview') tabId = 'tab-dashboard-overview';
    else if (tabId === 'directory') tabId = 'tab-student-directory';
    else if (tabId === 'placements') tabId = 'tab-placement-drives';
    else if (tabId === 'electives') tabId = 'tab-elective-seats';
    else if (tabId === 'bonafide') tabId = 'tab-bonafide-approvals';
    else if (tabId === 'hostel') tabId = 'tab-hostel-warden';
    else if (tabId === 'announcements') tabId = 'tab-announcements';

    if (!tabId.startsWith('tab-')) {
        tabId = 'tab-' + tabId;
    }

    // 1. Update Sidebar Active Links (Navy Blue #1E40AF background)
    const allLinks = document.querySelectorAll('.admin-sidebar .nav-item, .admin-sidebar .nav-link');
    allLinks.forEach(link => {
        link.classList.remove('active');
        const linkTab = link.getAttribute('data-tab') || (link.getAttribute('href') ? link.getAttribute('href').replace('#', '') : '');
        if (linkTab === tabId) {
            link.classList.add('active');
        }
    });

    if (clickedEl && clickedEl.classList) {
        clickedEl.classList.add('active');
    }

    // 2. Hide all tab containers (display: none)
    const tabPanes = document.querySelectorAll('.admin-tab-pane');
    tabPanes.forEach(pane => {
        pane.style.display = 'none';
        pane.classList.remove('active');
    });

    // 3. Show ONLY the selected tab container (display: block)
    const activePane = document.getElementById(tabId);
    if (activePane) {
        activePane.style.display = 'block';
        activePane.classList.add('active');
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }

    // 4. Update window URL hash without refreshing
    try {
        history.replaceState(null, null, `#${tabId}`);
    } catch (e) {
        // Safe fallback
    }

    // 5. Trigger sub-module specific renderers
    if (tabId === 'tab-dashboard-overview') {
        loadDashboardStats();
        renderOverviewWidgets();
    } else if (tabId === 'tab-student-directory') {
        renderStudentDirectoryTable();
    } else if (tabId === 'tab-placement-drives') {
        loadAdminPlacementDrives();
    } else if (tabId === 'tab-elective-seats') {
        renderElectivesGrid();
    } else if (tabId === 'tab-bonafide-approvals') {
        renderBonafideTable();
    } else if (tabId === 'tab-hostel-warden') {
        loadHostelApplications();
    } else if (tabId === 'tab-announcements') {
        loadAnnouncementHistory();
    }
}

// ==========================================================================
// 3. Top Metrics & Executive Dashboard Overview
// ==========================================================================
async function loadDashboardStats() {
    fetchStudentCount();
    try {
        const res = await fetch(`${API_BASE_URL}/admin/dashboard-stats`);
        if (res.ok) {
            const data = await res.json();
            if (data.totalStudents !== undefined) {
                updateStudentCounts(data.totalStudents);
            }
            if (data.pendingBonafide) {
                document.getElementById('kpiPendingBonafide').textContent = data.pendingBonafide;
                const badge = document.getElementById('sidebarPendingCount');
                if (badge) badge.textContent = data.pendingBonafide;
                const pill = document.getElementById('kpiBonafidePillText');
                if (pill) pill.textContent = `${data.pendingBonafide} In Review - Requires Seal`;
                const deskBadge = document.getElementById('tableActionBadge');
                if (deskBadge) deskBadge.textContent = `${data.pendingBonafide} Requiring Action`;
            }
            if (data.activeDrives) {
                document.getElementById('kpiActiveDrives').textContent = data.activeDrives;
                const tabDriveCount = document.getElementById('drivesTabActiveCount');
                if (tabDriveCount) tabDriveCount.textContent = data.activeDrives;
                const sideDrive = document.getElementById('sidebarDrivesBadge');
                if (sideDrive) sideDrive.textContent = data.activeDrives;
            }
            if (data.electiveEnrollmentRatio || data.openElectives) {
                const ratio = data.electiveEnrollmentRatio || data.openElectives;
                const kpiEl = document.getElementById('kpiOpenElectives');
                if (kpiEl) kpiEl.textContent = ratio;
                const pillEl = document.getElementById('kpiElectivePillText');
                if (pillEl) pillEl.textContent = `Total Enrolled: ${ratio}`;
            }
        }
    } catch (err) {
        console.warn('Using local dashboard stats fallback:', err);
    }

    renderOverviewWidgets();
}

function renderOverviewWidgets() {
    // 1. Overview Bonafide Preview Table
    const tbody = document.getElementById('overviewBonafideTableBody');
    if (tbody) {
        const list = bonafideRequestsList.length > 0 ? bonafideRequestsList.slice(0, 4) : defaultRequestsSeed.slice(0, 4);
        tbody.innerHTML = list.map(item => `
            <tr>
                <td>
                    <div class="student-meta-name">${escapeHtml(item.studentName)}</div>
                    <div class="student-meta-sub">${escapeHtml(item.branch)}</div>
                </td>
                <td>
                    <span style="font-weight:600;color:#334155;font-family:monospace;font-size:0.84rem;">
                        ${escapeHtml(item.enrollmentNo)}
                    </span>
                </td>
                <td>
                    <span style="font-size:0.8rem;color:#1e3a8a;font-weight:600;">${escapeHtml(item.service)}</span>
                </td>
                <td>
                    <span style="color:#64748b;font-size:0.78rem;">${escapeHtml(item.submittedAt)}</span>
                </td>
                <td>
                    ${item.status === 'APPROVED' 
                        ? `<span class="status-badge-approved">&#10003; Approved</span>`
                        : `<button class="btn-approve-sm" onclick="handleAdminApprove(${item.id})">Approve &rarr;</button>`
                    }
                </td>
            </tr>
        `).join('');
    }

    // 2. Overview Electives Preview
    const electivesBox = document.getElementById('overviewElectivesList');
    if (electivesBox) {
        const electives = genericElectivesList.length > 0 ? genericElectivesList.slice(0, 3) : defaultElectivesSeed.slice(0, 3);
        electivesBox.innerHTML = electives.map(c => {
            const pct = Math.min(100, Math.round((c.enrolled / c.capacity) * 100));
            const colorClass = pct >= 95 ? 'red' : 'blue';
            const left = c.capacity - c.enrolled;
            return `
                <div class="elective-quota-card">
                    <div class="quota-header-row">
                        <span class="quota-course-name">${escapeHtml(c.courseName)}</span>
                        <span class="quota-status-pill ${colorClass}">${left <= 0 ? 'Waitlist' : left + ' Seats Left'}</span>
                    </div>
                    <div class="quota-progress-track">
                        <div class="quota-progress-fill ${colorClass}" style="width: ${pct}%;"></div>
                    </div>
                    <div class="quota-footer-row">
                        <span>${c.enrolled} / ${c.capacity} filled</span>
                        <span>${pct}% Full</span>
                    </div>
                </div>
            `;
        }).join('');
    }
}

// ==========================================================================
// 4. Student Directory Management
// ==========================================================================
async function loadStudentDirectory() {
    try {
        const res = await fetch(`${API_BASE_URL}/admin/students`);
        if (res.ok) {
            const data = await res.json();
            if (Array.isArray(data) && data.length > 0) {
                studentDirectoryList = data;
                filteredStudentDirectory = [...studentDirectoryList];
                renderStudentDirectoryTable();
                return;
            }
        }
    } catch (err) {
        console.warn('Loading seed student directory fallback:', err);
    }

    studentDirectoryList = [...defaultStudentsSeed];
    filteredStudentDirectory = [...studentDirectoryList];
    renderStudentDirectoryTable();
}

function renderStudentDirectoryTable() {
    const tbody = document.getElementById('studentDirectoryTableBody');
    if (!tbody) return;

    const list = filteredStudentDirectory;
    if (list.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="8" style="text-align:center;padding:2.5rem;color:var(--admin-text-muted);">
                    No students matching the current search filters.
                </td>
            </tr>
        `;
        return;
    }

    tbody.innerHTML = list.map(student => {
        const backlogBadge = (student.backlogs === 0)
            ? `<span style="background:#dcfce7;color:#15803d;padding:2px 8px;border-radius:4px;font-size:0.75rem;font-weight:700;">0 Backlog</span>`
            : `<span style="background:#fee2e2;color:#dc2626;padding:2px 8px;border-radius:4px;font-size:0.75rem;font-weight:700;">${student.backlogs} Backlog(s)</span>`;

        return `
            <tr>
                <td>
                    <span style="font-weight:700;color:#1e3a8a;font-family:monospace;font-size:0.86rem;">
                        ${escapeHtml(student.enrollmentNo)}
                    </span>
                </td>
                <td>
                    <div style="font-weight:700;color:var(--admin-text-main);">${escapeHtml(student.name)}</div>
                    <div style="font-size:0.75rem;color:var(--admin-text-muted);">${escapeHtml(student.email)} ${student.phone ? '• ' + escapeHtml(student.phone) : ''}</div>
                </td>
                <td>
                    <div style="font-weight:600;color:#334155;font-size:0.82rem;">${escapeHtml(student.branch)}</div>
                </td>
                <td>
                    <span style="font-weight:700;color:#475569;font-size:0.82rem;">Semester ${student.semester}</span>
                </td>
                <td>
                    <span style="font-weight:800;color:#1e40af;font-size:0.88rem;">${student.cgpa ? Number(student.cgpa).toFixed(2) : 'N/A'}</span>
                </td>
                <td>
                    ${backlogBadge}
                </td>
                <td>
                    <span style="font-size:0.78rem;color:#64748b;">${escapeHtml(student.gender || 'Male')}</span>
                </td>
                <td>
                    <button class="btn-seat-adjust" onclick="showToast('Viewing Academic Transcript for ${escapeHtml(student.name)} (${escapeHtml(student.enrollmentNo)})')">
                        View Record
                    </button>
                </td>
            </tr>
        `;
    }).join('');

    const textEl = document.getElementById('directoryPaginationText');
    if (textEl) textEl.textContent = `Showing ${list.length} of ${studentDirectoryList.length} enrolled students`;
}

function filterStudentDirectory() {
    const q = (document.getElementById('studentSearchInput')?.value || '').toLowerCase().trim();
    const branch = document.getElementById('studentBranchFilter')?.value || 'ALL';
    const sem = document.getElementById('studentSemFilter')?.value || 'ALL';
    const backlog = document.getElementById('studentBacklogFilter')?.value || 'ALL';

    filteredStudentDirectory = studentDirectoryList.filter(s => {
        const matchesQ = !q || (
            (s.name || '').toLowerCase().includes(q) ||
            (s.enrollmentNo || '').toLowerCase().includes(q) ||
            (s.email || '').toLowerCase().includes(q)
        );

        const matchesBranch = (branch === 'ALL') || (s.branch === branch);
        const matchesSem = (sem === 'ALL') || (String(s.semester) === sem);
        const matchesBacklog = (backlog === 'ALL') || 
            (backlog === 'CLEAR' && s.backlogs === 0) ||
            (backlog === 'HAS_BACKLOG' && s.backlogs > 0);

        return matchesQ && matchesBranch && matchesSem && matchesBacklog;
    });

    renderStudentDirectoryTable();
}

function exportStudentDirectoryCsv() {
    if (filteredStudentDirectory.length === 0) {
        showToast('No student data to export.');
        return;
    }

    let csv = 'Enrollment No,Student Name,Email,Phone,Branch,Semester,CGPA,Backlogs,Gender\n';
    filteredStudentDirectory.forEach(s => {
        csv += `"${s.enrollmentNo}","${s.name}","${s.email}","${s.phone || ''}","${s.branch}","${s.semester}","${s.cgpa}","${s.backlogs}","${s.gender || 'Male'}"\n`;
    });

    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `SVVV_Student_Directory_${new Date().toISOString().split('T')[0]}.csv`;
    a.click();
    URL.revokeObjectURL(url);
    showToast('Student Directory CSV exported successfully!');
}

// ==========================================================================
// 5. Generic Elective Seats Management
// ==========================================================================
async function loadElectivesData() {
    try {
        const [adminRes, statsRes] = await Promise.all([
            fetch(`${API_BASE_URL}/admin/electives`).catch(() => fetch(`${API_BASE_URL}/electives`)),
            fetch(`${API_BASE_URL}/electives/stats`)
        ]);

        let data = null;
        if (adminRes && adminRes.ok) {
            data = await adminRes.json();
        } else {
            const fallbackRes = await fetch(`${API_BASE_URL}/electives`);
            if (fallbackRes.ok) data = await fallbackRes.json();
        }

        if (Array.isArray(data) && data.length > 0) {
            genericElectivesList = data.map(c => {
                const total = c.totalSeats || c.capacity || 30;
                const filled = c.filled_seats != null ? Number(c.filled_seats) : 
                              (c.filledSeats != null ? Number(c.filledSeats) : 
                              (c.enrolledSeats != null ? Number(c.enrolledSeats) : 
                              (c.enrolled != null ? Number(c.enrolled) : 
                              (c.enrolledCount != null ? Number(c.enrolledCount) : 0))));
                const remaining = c.remaining_seats != null ? Number(c.remaining_seats) : 
                                 (c.remainingSeats != null ? Number(c.remainingSeats) : Math.max(0, total - filled));
                const pct = c.capacity_percentage != null ? Math.round(Number(c.capacity_percentage)) : Math.round((filled / total) * 100);

                return {
                    id: c.id || c.courseId,
                    courseCode: c.courseCode,
                    courseName: c.courseName,
                    department: c.department || 'Computer Science & Engineering',
                    capacity: total,
                    enrolled: filled,
                    filled_seats: filled,
                    remainingSeats: remaining,
                    remaining_seats: remaining,
                    capacity_percentage: pct,
                    instructor: c.instructor || c.facultyName || 'Faculty Lead'
                };
            });
        }

        if (statsRes && statsRes.ok) {
            const stats = await statsRes.json();
            const countEl = document.getElementById('electivesTotalCount');
            if (countEl) countEl.textContent = `${stats.totalCourses || genericElectivesList.length || 4} Courses`;
            const intakeEl = document.getElementById('electivesTotalIntake');
            if (intakeEl) intakeEl.textContent = `${stats.totalCapacity || 120} Seats`;
            const occEl = document.getElementById('electivesTotalOccupied');
            if (occEl) occEl.textContent = `${stats.totalEnrolled || 0} Enrolled`;
            const vacEl = document.getElementById('electivesTotalVacant');
            if (vacEl) vacEl.textContent = `${stats.totalVacant != null ? stats.totalVacant : Math.max(0, 120 - stats.totalEnrolled)} Remaining`;

            const kpiEl = document.getElementById('kpiOpenElectives');
            if (kpiEl && stats.enrollmentRatio) kpiEl.textContent = stats.enrollmentRatio;
            const pillEl = document.getElementById('kpiElectivePillText');
            if (pillEl && stats.enrollmentRatio) pillEl.textContent = `Total Enrolled: ${stats.enrollmentRatio}`;
        }
    } catch (err) {
        console.warn('Loading live electives error, using fallback:', err);
    }

    if (!genericElectivesList || genericElectivesList.length === 0) {
        genericElectivesList = [...defaultElectivesSeed];
    }
    renderElectivesGrid();
    renderOverviewWidgets();
}

function renderElectivesGrid() {
    const grid = document.getElementById('electivesAdminGrid');
    if (!grid) return;

    let totalCapacity = 0;
    let totalEnrolled = 0;

    grid.innerHTML = genericElectivesList.map(c => {
        const capacity = c.capacity || 30;
        const enrolled = c.enrolled != null ? c.enrolled : (c.filled_seats || 0);
        totalCapacity += capacity;
        totalEnrolled += enrolled;

        const pct = c.capacity_percentage != null ? c.capacity_percentage : Math.min(100, Math.round((enrolled / capacity) * 100));
        const vacant = c.remaining_seats != null ? c.remaining_seats : Math.max(0, capacity - enrolled);
        const colorClass = pct >= 90 ? 'red' : 'blue';

        // Badge Top Right: Display exact remaining seats (e.g., "8 Seats Left")
        const statusText = `${vacant} Seats Left`;

        return `
            <div class="elective-admin-card" id="elective-card-${c.id}">
                <div class="elective-admin-top">
                    <div>
                        <span class="elective-code-pill">${escapeHtml(c.courseCode)}</span>
                        <div class="elective-title-text">${escapeHtml(c.courseName)}</div>
                        <div class="elective-dept-text">${escapeHtml(c.department || 'Computer Science & Engineering')} • ${escapeHtml(c.instructor || 'Faculty Lead')}</div>
                    </div>
                    <span class="quota-status-pill ${colorClass}">${statusText}</span>
                </div>

                <div class="quota-progress-track">
                    <div class="quota-progress-fill ${colorClass}" style="width: ${pct}%;"></div>
                </div>

                <div class="quota-footer-row">
                    <span style="font-weight:700;">${enrolled} / ${capacity} filled</span>
                    <span style="color:#64748b;">${pct}% capacity (${vacant} vacant)</span>
                </div>
            </div>
        `;
    }).join('');

    // Ensure Top Stat Cards reflect exact live numbers
    const countEl = document.getElementById('electivesTotalCount');
    if (countEl) countEl.textContent = `${genericElectivesList.length} Courses`;
    const intakeEl = document.getElementById('electivesTotalIntake');
    if (intakeEl) intakeEl.textContent = `${totalCapacity} Seats`;
    const occEl = document.getElementById('electivesTotalOccupied');
    if (occEl) occEl.textContent = `${totalEnrolled} Enrolled`;
    const vacEl = document.getElementById('electivesTotalVacant');
    if (vacEl) vacEl.textContent = `${Math.max(0, totalCapacity - totalEnrolled)} Remaining`;
}

// ==========================================================================
// 6. Bonafide Approvals Desk
// ==========================================================================
async function loadBonafideRequests() {
    try {
        const res = await fetch(`${API_BASE_URL}/bonafide/all`);
        if (res.ok) {
            const apiData = await res.json();
            if (Array.isArray(apiData) && apiData.length > 0) {
                bonafideRequestsList = apiData.map(item => ({
                    id: item.id,
                    studentName: item.studentName || 'Student',
                    branch: `${item.branch || 'B.Tech CSE'}, Sem ${item.semester || 'VII'}`,
                    enrollmentNo: item.enrollmentNo || '0827CS211001',
                    service: 'Bonafide Certificate',
                    purpose: 'Bonafide Certificate',
                    submittedAt: item.requestedAt || 'Today',
                    status: item.status || 'PENDING',
                    type: 'BONAFIDE'
                }));
            } else {
                bonafideRequestsList = [...defaultRequestsSeed];
            }
        } else {
            bonafideRequestsList = [...defaultRequestsSeed];
        }
    } catch (err) {
        console.warn('Using seed bonafide list fallback:', err);
        bonafideRequestsList = [...defaultRequestsSeed];
    }

    renderBonafideTable();
    renderOverviewWidgets();
}

function renderBonafideTable(filteredData = null) {
    const tbody = document.getElementById('bonafideTableBody');
    if (!tbody) return;

    const list = filteredData || bonafideRequestsList;
    tbody.innerHTML = '';

    if (list.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="4" style="text-align:center;padding:2.5rem;color:var(--admin-text-muted);">
                    No applications found.
                </td>
            </tr>
        `;
        return;
    }

    list.forEach(item => {
        const tr = document.createElement('tr');
        const isApproved = item.status === 'APPROVED';
        const isRejected = item.status === 'REJECTED';

        let statusBadge = `<span class="status-badge-pending">Verification Pending</span>`;
        let actionButtons = `
            <div style="display:flex;align-items:center;gap:0.4rem;">
                <button class="btn-approve-sm" onclick="handleAdminApprove(${item.id})">Approve &amp; Sign</button>
                <button class="btn-reject-sm" onclick="handleAdminReject(${item.id})">Reject</button>
            </div>
        `;

        if (isApproved) {
            statusBadge = `<span class="status-badge-approved">&#10003; Approved &amp; Signed</span>`;
            actionButtons = `<span style="font-size:0.75rem;color:#059669;font-weight:700;">Certificate Ready</span>`;
        } else if (isRejected) {
            statusBadge = `<span class="status-badge-rejected">&#10007; Rejected</span>`;
            actionButtons = `<span style="font-size:0.75rem;color:#dc2626;font-weight:700;">Archived</span>`;
        }

        tr.innerHTML = `
            <td>
                <div class="student-meta-name">${escapeHtml(item.studentName)}</div>
                <div class="student-meta-sub">${escapeHtml(item.branch)}</div>
            </td>
            <td>
                <span style="font-weight:600;color:#334155;font-family:monospace;font-size:0.86rem;">
                    ${escapeHtml(item.enrollmentNo)}
                </span>
            </td>
            <td>
                <span style="color:#64748b;font-size:0.8rem;">${escapeHtml(item.submittedAt)}</span>
            </td>
            <td>
                <div style="display:flex;align-items:center;gap:0.6rem;">
                    ${statusBadge}
                    ${actionButtons}
                </div>
            </td>
        `;

        tbody.appendChild(tr);
    });

    // Update Counts
    const countAllEl = document.getElementById('countFilterAll');
    if (countAllEl) countAllEl.textContent = bonafideRequestsList.length;

    const pendingCount = bonafideRequestsList.filter(i => i.status === 'PENDING').length;
    const tableBadge = document.getElementById('tableActionBadge');
    if (tableBadge) tableBadge.textContent = `${pendingCount} Requiring Action`;
    const sidebarBadge = document.getElementById('sidebarPendingCount');
    if (sidebarBadge) sidebarBadge.textContent = pendingCount;
    const kpiCount = document.getElementById('kpiPendingBonafide');
    if (kpiCount) kpiCount.textContent = pendingCount;
}

function getFilteredRequests() {
    return bonafideRequestsList;
}

function filterBonafideTable(filter, buttonEl) {
    renderBonafideTable();
}

function searchBonafideTable(query) {
    const q = (query || '').toLowerCase().trim();
    if (!q) {
        renderBonafideTable();
        return;
    }

    const filtered = bonafideRequestsList.filter(item => 
        (item.studentName || '').toLowerCase().includes(q) ||
        (item.enrollmentNo || '').toLowerCase().includes(q) ||
        (item.branch || '').toLowerCase().includes(q)
    );

    renderBonafideTable(filtered);
}

async function handleAdminApprove(requestId) {
    try {
        const res = await fetch(`${API_BASE_URL}/bonafide/${requestId}/approve`, {
            method: 'POST'
        });
        if (res.ok) {
            showToast(`Bonafide Request #${requestId} Approved & Digitally Signed!`);
        } else {
            showToast(`Request #${requestId} Marked as Approved.`);
        }
    } catch (err) {
        showToast(`Request #${requestId} Marked as Approved.`);
    }

    // Update local state
    const req = bonafideRequestsList.find(r => r.id === requestId);
    if (req) req.status = 'APPROVED';
    renderBonafideTable();
    renderOverviewWidgets();
}

async function handleAdminReject(requestId) {
    if (!confirm(`Are you sure you want to reject application #${requestId}?`)) return;

    try {
        await fetch(`${API_BASE_URL}/bonafide/${requestId}/reject`, { method: 'POST' });
    } catch (err) {
        // Handled locally
    }

    const req = bonafideRequestsList.find(r => r.id === requestId);
    if (req) req.status = 'REJECTED';
    showToast(`Application #${requestId} rejected.`);
    renderBonafideTable();
    renderOverviewWidgets();
}

function batchApprovePendingBonafides() {
    const pendings = bonafideRequestsList.filter(r => r.status === 'PENDING');
    if (pendings.length === 0) {
        showToast('No pending bonafide applications to approve.');
        return;
    }

    if (!confirm(`Approve and digitally seal all ${pendings.length} pending applications?`)) return;

    pendings.forEach(p => {
        p.status = 'APPROVED';
        fetch(`${API_BASE_URL}/bonafide/${p.id}/approve`, { method: 'POST' }).catch(() => {});
    });

    showToast(`Batch approved ${pendings.length} certificates!`);
    renderBonafideTable();
    renderOverviewWidgets();
}

// ==========================================================================
// 7. Campus Placement Drives Management
// ==========================================================================
let currentRosterCompanyId = null;
let currentRosterDrive = null;
let currentRosterApplicants = [];

async function loadAdminPlacementDrives() {
    try {
        const res = await fetch(`${API_BASE_URL}/admin/placements/drives`);
        if (res.ok) {
            const data = await res.json();
            if (Array.isArray(data)) {
                adminPlacementDrives = data;
                renderAdminPlacementDrives(adminPlacementDrives);
                
                const activeCount = adminPlacementDrives.filter(d => (d.status || 'ACTIVE').toUpperCase() === 'ACTIVE').length;
                const totalApps = adminPlacementDrives.reduce((acc, d) => acc + (d.applicantsCount || 0), 0);
                
                const kpiActive = document.getElementById('kpiActiveDrives');
                if (kpiActive) kpiActive.textContent = activeCount;
                const tabDriveCount = document.getElementById('drivesTabActiveCount');
                if (tabDriveCount) tabDriveCount.textContent = activeCount;
                const sideDrive = document.getElementById('sidebarDrivesBadge');
                if (sideDrive) sideDrive.textContent = activeCount;
                const tabAppCount = document.getElementById('drivesTabApplicantsCount');
                if (tabAppCount) tabAppCount.textContent = `${totalApps} Registered`;
                return;
            }
        }
    } catch (err) {
        console.warn('Error fetching live placement drives:', err);
    }
}

function renderAdminPlacementDrives(drives) {
    const gridOverview = document.getElementById('adminDrivesGrid');
    const gridFull = document.getElementById('adminDrivesGridFull');

    const cardsHtml = (!drives || drives.length === 0) ? `
        <div style="grid-column: 1 / -1; text-align: center; padding: 2rem; color: var(--admin-text-muted); font-size: 0.85rem; background: #f8fafc; border-radius: 8px; border: 1px dashed var(--admin-border);">
            No campus recruitment drives currently active. Click below to add a new drive.
        </div>
    ` : drives.map(drive => {
        const applicantsCount = drive.applicantsCount || 0;
        const ctc = drive.packageCtc ? `${drive.packageCtc} LPA` : 'Best in Industry';
        const deadlineStr = drive.deadline ? formatShortDate(drive.deadline) : 'Rolling';

        return `
            <div class="placement-mini-card" id="drive-card-${drive.id}">
                <div class="placement-card-top">
                    <div>
                        <div class="placement-mini-name">${escapeHtml(drive.companyName)}</div>
                        <div class="placement-mini-role">${escapeHtml(drive.jobRole || 'Campus Recruitment')}</div>
                    </div>
                    <span class="placement-mini-ctc">₹${escapeHtml(ctc)}</span>
                </div>
                
                <div class="placement-meta-chips">
                    <span class="placement-chip" title="Minimum CGPA required">Min CGPA: ${drive.minCgpa ?? '6.0'}</span>
                    <span class="placement-chip" title="Max Backlogs allowed">Max Backlogs: ${drive.maxBacklogs ?? 0}</span>
                    <span class="placement-chip" title="Eligible Branches">${escapeHtml(drive.allowedBranch || 'ALL')}</span>
                    <span class="placement-chip deadline" title="Registration Deadline">Ends: ${escapeHtml(deadlineStr)}</span>
                </div>

                <div class="placement-card-footer">
                    <button type="button" class="btn-view-roster" onclick="openApplicantRosterModal(${drive.id})">
                        <svg width="13" height="13" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z"/>
                        </svg>
                        <span>${applicantsCount} Registered</span> &rarr;
                    </button>
                    <span style="font-size: 0.7rem; font-weight: 700; color: ${drive.status === 'ACTIVE' ? '#059669' : '#64748b'};">
                        ● ${escapeHtml(drive.status || 'ACTIVE')}
                    </span>
                </div>
            </div>
        `;
    }).join('');

    if (gridOverview) gridOverview.innerHTML = cardsHtml;
    if (gridFull) gridFull.innerHTML = cardsHtml;
}

function formatShortDate(dateStr) {
    if (!dateStr) return '';
    try {
        const d = new Date(dateStr);
        if (isNaN(d.getTime())) return dateStr;
        return d.toLocaleDateString('en-IN', { month: 'short', day: 'numeric', year: 'numeric' });
    } catch (e) {
        return dateStr;
    }
}

function openAddDriveModal() {
    const modal = document.getElementById('addDriveModal');
    const form = document.getElementById('createDriveForm');
    if (!modal) return;

    if (form) form.reset();
    const deadlineInput = document.getElementById('driveDeadline');
    if (deadlineInput) {
        const future = new Date();
        future.setDate(future.getDate() + 14);
        deadlineInput.value = future.toISOString().split('T')[0];
    }
    modal.classList.add('active');
}

function closeAddDriveModal() {
    const modal = document.getElementById('addDriveModal');
    if (modal) modal.classList.remove('active');
}

async function handleCreateDriveSubmit(event) {
    event.preventDefault();

    const companyName = document.getElementById('driveCompanyName')?.value.trim();
    const jobRole = document.getElementById('driveJobRole')?.value.trim();
    const packageCtc = parseFloat(document.getElementById('drivePackageCtc')?.value);
    const minCgpa = parseFloat(document.getElementById('driveMinCgpa')?.value);
    const maxBacklogs = parseInt(document.getElementById('driveMaxBacklogs')?.value, 10);
    const allowedBranch = document.getElementById('driveAllowedBranch')?.value.trim();
    const deadline = document.getElementById('driveDeadline')?.value;

    if (!companyName || !jobRole || isNaN(packageCtc) || !deadline) {
        showToast('Please fill in all required placement drive fields.');
        return;
    }

    const submitBtn = document.getElementById('btnSubmitDrive');
    const originalText = submitBtn ? submitBtn.innerHTML : '';
    if (submitBtn) {
        submitBtn.disabled = true;
        submitBtn.innerHTML = 'Publishing Drive...';
    }

    try {
        const payload = {
            companyName,
            jobRole,
            packageCtc,
            minCgpa: isNaN(minCgpa) ? 6.0 : minCgpa,
            maxBacklogs: isNaN(maxBacklogs) ? 0 : maxBacklogs,
            allowedBranch: allowedBranch || 'ALL',
            deadline
        };

        const res = await fetch(`${API_BASE_URL}/admin/placements/drives`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        const data = await res.json();
        if (res.ok && data.success) {
            showToast(`Drive for "${companyName}" published successfully!`);
            closeAddDriveModal();
            loadAdminPlacementDrives();
            loadDashboardStats();
        } else {
            showToast(data.message || 'Failed to create placement drive.');
        }
    } catch (err) {
        console.error('Error creating placement drive:', err);
        showToast('Network error while publishing drive.');
    } finally {
        if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.innerHTML = originalText;
        }
    }
}

async function openApplicantRosterModal(companyId) {
    const modal = document.getElementById('applicantRosterModal');
    if (!modal) return;

    currentRosterCompanyId = companyId;
    currentRosterDrive = adminPlacementDrives.find(d => d.id === companyId) || null;

    const searchInput = document.getElementById('rosterSearchInput');
    if (searchInput) searchInput.value = '';

    const companyEl = document.getElementById('rosterCompanyName');
    const roleEl = document.getElementById('rosterJobRole');
    const packagePill = document.getElementById('rosterPackagePill');
    const countPill = document.getElementById('rosterApplicantsCountPill');

    if (currentRosterDrive) {
        if (companyEl) companyEl.textContent = currentRosterDrive.companyName;
        if (roleEl) roleEl.textContent = `${currentRosterDrive.jobRole || 'Campus Recruitment'} • Min CGPA: ${currentRosterDrive.minCgpa ?? '6.0'}`;
        if (packagePill) packagePill.textContent = `₹${currentRosterDrive.packageCtc} LPA`;
    } else {
        if (companyEl) companyEl.textContent = `Drive #${companyId}`;
    }
    if (countPill) countPill.textContent = 'Loading applicants...';

    const tbody = document.getElementById('rosterTableBody');
    if (tbody) {
        tbody.innerHTML = `
            <tr>
                <td colspan="8" style="text-align:center;padding:2rem;color:var(--admin-text-muted);">
                    Fetching live applicant roster and verified resumes from PostgreSQL...
                </td>
            </tr>
        `;
    }

    modal.classList.add('active');

    try {
        const res = await fetch(`${API_BASE_URL}/admin/placements/drives/${companyId}/applicants`);
        if (res.ok) {
            const data = await res.json();
            if (data.success && Array.isArray(data.applicants)) {
                currentRosterApplicants = data.applicants;
                if (countPill) countPill.textContent = `${currentRosterApplicants.length} Applied`;
                renderApplicantRosterTable(currentRosterApplicants);
                return;
            }
        }
        throw new Error('Failed to load roster data');
    } catch (err) {
        console.error('Error fetching roster:', err);
        if (countPill) countPill.textContent = '0 Applied';
        if (tbody) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="8" style="text-align:center;padding:2rem;color:#ef4444;">
                        Failed to retrieve applicants for this placement drive.
                    </td>
                </tr>
            `;
        }
    }
}

function closeApplicantRosterModal() {
    const modal = document.getElementById('applicantRosterModal');
    if (modal) modal.classList.remove('active');
    currentRosterCompanyId = null;
    currentRosterApplicants = [];
}

function renderApplicantRosterTable(applicants) {
    const tbody = document.getElementById('rosterTableBody');
    if (!tbody) return;

    if (!applicants || applicants.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="8" style="text-align:center;padding:2.5rem;color:var(--admin-text-muted);">
                    No students have registered for this placement drive yet.
                </td>
            </tr>
        `;
        return;
    }

    tbody.innerHTML = applicants.map(app => {
        const resumeBtn = app.resumeUrl
            ? `<a href="${escapeHtml(app.resumeUrl)}" target="_blank" class="btn-resume-link" title="${escapeHtml(app.resumeFileName || 'Resume')}">
                📄 ${escapeHtml(app.resumeFileName ? (app.resumeFileName.length > 15 ? app.resumeFileName.substring(0, 15) + '...' : app.resumeFileName) : 'View Resume')}
               </a>`
            : `<span style="font-size:0.75rem;color:var(--admin-text-muted);font-style:italic;">No Resume</span>`;

        const backlogBadge = (app.backlogs === 0)
            ? `<span style="background:#dcfce7;color:#15803d;padding:2px 7px;border-radius:4px;font-size:0.75rem;font-weight:700;">0 Backlog</span>`
            : `<span style="background:#fee2e2;color:#dc2626;padding:2px 7px;border-radius:4px;font-size:0.75rem;font-weight:700;">${app.backlogs} Backlogs</span>`;

        const isShortlisted = (app.status || '').toUpperCase() === 'SHORTLISTED';
        const statusBadge = isShortlisted
            ? `<span class="status-badge-approved">Shortlisted</span>`
            : `<span class="status-badge-pending">${escapeHtml(app.status || 'APPLIED')}</span>`;

        return `
            <tr>
                <td>
                    <span style="font-weight:700;color:#1e3a8a;font-family:monospace;font-size:0.86rem;">
                        ${escapeHtml(app.rollNo)}
                    </span>
                </td>
                <td>
                    <div style="font-weight:700;color:var(--admin-text-main);">${escapeHtml(app.name)}</div>
                    <div style="font-size:0.75rem;color:var(--admin-text-muted);">${escapeHtml(app.email || '')} ${app.phone ? '• ' + escapeHtml(app.phone) : ''}</div>
                </td>
                <td>
                    <div style="font-weight:600;color:#334155;font-size:0.82rem;">${escapeHtml(app.branch)}</div>
                    <div style="font-size:0.72rem;color:var(--admin-text-muted);">Semester ${escapeHtml(app.semester ?? '')}</div>
                </td>
                <td>
                    <span style="font-weight:800;color:#1e40af;font-size:0.88rem;">${app.cgpa ? Number(app.cgpa).toFixed(2) : 'N/A'}</span>
                </td>
                <td>${backlogBadge}</td>
                <td><span style="font-size:0.78rem;color:var(--admin-text-muted);">${escapeHtml(app.appliedAt || 'N/A')}</span></td>
                <td>${resumeBtn}</td>
                <td>${statusBadge}</td>
            </tr>
        `;
    }).join('');
}

function handleRosterSearch(query) {
    const q = (query || '').toLowerCase().trim();
    if (!q) {
        renderApplicantRosterTable(currentRosterApplicants);
        return;
    }

    const filtered = currentRosterApplicants.filter(app => {
        return (app.name || '').toLowerCase().includes(q) ||
               (app.rollNo || '').toLowerCase().includes(q) ||
               (app.branch || '').toLowerCase().includes(q) ||
               (app.email || '').toLowerCase().includes(q);
    });

    renderApplicantRosterTable(filtered);
}

async function exportCurrentRosterCsv() {
    if (!currentRosterCompanyId) {
        showToast('Please open an applicant roster to export CSV.');
        return;
    }

    const exportBtn = document.getElementById('btnExportRosterCsv');
    const originalContent = exportBtn ? exportBtn.innerHTML : '';
    if (exportBtn) {
        exportBtn.disabled = true;
        exportBtn.innerHTML = `Generating CSV...`;
    }

    try {
        const downloadUrl = `${API_BASE_URL}/admin/placements/drives/${currentRosterCompanyId}/export-csv`;
        const res = await fetch(downloadUrl);
        if (!res.ok) throw new Error('CSV download failed');

        const blob = await res.blob();
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.style.display = 'none';
        a.href = url;
        const compName = currentRosterDrive ? currentRosterDrive.companyName.replace(/[^a-zA-Z0-9]/g, '_') : 'Drive_' + currentRosterCompanyId;
        a.download = `SVVV_Applicants_${compName}_${new Date().toISOString().split('T')[0]}.csv`;
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
        document.body.removeChild(a);

        showToast('Applicant CSV roster exported successfully!');
    } catch (err) {
        console.error('CSV Export Error:', err);
        showToast('Failed to export CSV. Please try again.');
    } finally {
        if (exportBtn) {
            exportBtn.disabled = false;
            exportBtn.innerHTML = originalContent;
        }
    }
}

// ==========================================================================
// 8. Hostel Warden Desk API Workflow
// ==========================================================================
async function loadHostelApplications() {
    try {
        const res = await fetch(`${API_BASE_URL}/hostel/admin/applications`);
        if (!res.ok) throw new Error('API failed');
        const data = await res.json();
        hostelApplicationsList = data;
        renderHostelTable(data);

        const pendingCount = data.filter(a => a.status === 'APPLIED').length;
        const badge = document.getElementById('sidebarHostelPendingCount');
        if (badge) badge.textContent = pendingCount;
    } catch (err) {
        console.warn('Failed to load hostel applications:', err);
    }
}

function renderHostelTable(apps) {
    const tbody = document.getElementById('hostelTableBody');
    if (!tbody) return;

    if (!apps || apps.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="8" style="text-align:center;padding:24px;color:var(--admin-text-muted);">
                    No hostel applications submitted yet.
                </td>
            </tr>
        `;
        return;
    }

    tbody.innerHTML = apps.map(app => {
        const student = app.student || {};
        const sName = escapeHtml(student.name || 'Student');
        const sRoll = escapeHtml(student.enrollmentNo || 'N/A');
        const wing = escapeHtml(app.hostelWing || 'Main Wing');
        const occ = escapeHtml(app.roomOccupancyType || '2-Sharing');
        const climate = escapeHtml(app.climateFacility || 'AC');
        const fee = '₹' + (app.totalPayableFee || 0).toLocaleString('en-IN');
        const isApproved = app.status === 'APPROVED';
        const isRejected = app.status === 'REJECTED';

        let statusBadge = `<span class="badge-status pending" style="background:#fef3c7;color:#92400e;padding:4px 8px;border-radius:4px;font-weight:700;font-size:11px;">PENDING</span>`;
        if (isApproved) {
            statusBadge = `<span class="badge-status approved" style="background:#dcfce7;color:#166534;padding:4px 8px;border-radius:4px;font-weight:700;font-size:11px;">APPROVED</span>`;
        } else if (isRejected) {
            statusBadge = `<span class="badge-status rejected" style="background:#fee2e2;color:#991b1b;padding:4px 8px;border-radius:4px;font-weight:700;font-size:11px;">REJECTED</span>`;
        }

        const roomVal = escapeHtml(app.allottedRoomNo || 'Block-A Room 302');
        const wardenVal = escapeHtml(app.wardenName || 'Mr. S. K. Sharma');

        return `
            <tr>
                <td style="font-weight:700;">#HOSTEL-${app.applicationId}</td>
                <td>
                    <div style="font-weight:700;color:var(--admin-text-main);">${sName}</div>
                    <div style="font-size:0.75rem;color:var(--admin-text-muted);">${sRoll} • ${escapeHtml(student.gender || 'Male')}</div>
                </td>
                <td>
                    <div>${wing}</div>
                    <div style="font-size:0.75rem;color:var(--admin-text-muted);">${occ}</div>
                </td>
                <td>${climate}</td>
                <td style="font-weight:700;color:#1d4ed8;">${fee}</td>
                <td>${statusBadge}</td>
                <td>
                    ${isApproved ? `
                        <div style="font-weight:700;color:#166534;">${roomVal}</div>
                        <div style="font-size:0.72rem;color:var(--admin-text-muted);">${wardenVal}</div>
                    ` : `
                        <input type="text" id="roomInput_${app.applicationId}" value="${roomVal}" class="admin-form-input" style="padding:4px 8px;font-size:12px;width:140px;margin-bottom:4px;" placeholder="Room No">
                        <input type="text" id="wardenInput_${app.applicationId}" value="${wardenVal}" class="admin-form-input" style="padding:4px 8px;font-size:12px;width:140px;" placeholder="Warden Name">
                    `}
                </td>
                <td>
                    ${isApproved ? `
                        <span style="font-size:12px;color:#166534;font-weight:700;">Allotted</span>
                    ` : `
                        <div style="display:flex;gap:6px;">
                            <button onclick="approveHostelApp(${app.applicationId})" class="btn-admin-action primary" style="padding:5px 10px;font-size:11.5px;">
                                Approve &amp; Allot
                            </button>
                            <button onclick="rejectHostelApp(${app.applicationId})" class="btn-admin-action" style="padding:5px 8px;font-size:11.5px;color:#dc2626;border-color:#fca5a5;">
                                Reject
                            </button>
                        </div>
                    `}
                </td>
            </tr>
        `;
    }).join('');
}

async function approveHostelApp(appId) {
    const roomInput = document.getElementById(`roomInput_${appId}`);
    const wardenInput = document.getElementById(`wardenInput_${appId}`);
    const room = roomInput ? roomInput.value.trim() : 'Block-A Room 302';
    const warden = wardenInput ? wardenInput.value.trim() : 'Mr. S. K. Sharma';

    try {
        const res = await fetch(`${API_BASE_URL}/hostel/admin/approve/${appId}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                allottedRoomNo: room,
                wardenName: warden,
                status: 'APPROVED'
            })
        });

        const data = await res.json();
        if (res.ok && data.success) {
            showToast(`Approved! Allotted ${data.application.allottedRoomNo} to student.`);
            loadHostelApplications();
        } else {
            showToast(data.message || 'Approval completed');
            loadHostelApplications();
        }
    } catch (err) {
        showToast(`Allotted ${room} successfully!`);
        loadHostelApplications();
    }
}

async function rejectHostelApp(appId) {
    if (!confirm('Are you sure you want to reject this hostel application?')) return;
    try {
        await fetch(`${API_BASE_URL}/hostel/admin/reject/${appId}`, { method: 'POST' });
    } catch (err) {}
    showToast('Application marked as REJECTED');
    loadHostelApplications();
}

// ==========================================================================
// 9. Post Announcements & Broadcast Desk
// ==========================================================================
function loadAnnouncementHistory() {
    const listEl = document.getElementById('announcementHistoryList');
    if (!listEl) return;

    listEl.innerHTML = announcementHistory.map(ann => `
        <div class="announcement-history-card">
            <div class="announcement-history-top">
                <div class="announcement-history-title">${escapeHtml(ann.title)}</div>
                <span class="announcement-history-date">${escapeHtml(ann.postedAt)}</span>
            </div>
            <div style="display:flex;align-items:center;gap:0.5rem;">
                <span style="font-size:0.72rem;background:#e0f2fe;color:#0369a1;padding:2px 7px;border-radius:4px;font-weight:700;">
                    ${escapeHtml(ann.audience)}
                </span>
                <span style="font-size:0.72rem;color:#64748b;">By: ${escapeHtml(ann.author || 'Admin Desk')}</span>
            </div>
            <p class="announcement-history-content">${escapeHtml(ann.content)}</p>
        </div>
    `).join('');
}

async function handlePostAnnouncement(event) {
    event.preventDefault();
    const title = document.getElementById('annTitle')?.value.trim();
    const audience = document.getElementById('annAudience')?.value;
    const content = document.getElementById('annContent')?.value.trim();

    if (!title || !content) {
        showToast('Please enter both title and announcement content.');
        return;
    }

    const btn = document.getElementById('btnPublishAnn');
    if (btn) {
        btn.disabled = true;
        btn.innerHTML = 'Broadcasting...';
    }

    try {
        await fetch(`${API_BASE_URL}/admin/announcements`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ title, targetAudience: audience, content })
        });
    } catch (err) {}

    announcementHistory.unshift({
        id: Date.now(),
        title,
        audience,
        content,
        postedAt: 'Just now',
        author: 'Super Admin SS'
    });

    loadAnnouncementHistory();
    showToast('Announcement successfully published to all student portals!');
    if (btn) {
        btn.disabled = false;
        btn.innerHTML = 'Publish Announcement';
    }
}

// ==========================================================================
// Student Record Management (Add, Delete & Directory Ledger)
// ==========================================================================
function openAddStudentModal() {
    const modal = document.getElementById('addStudentModal');
    if (modal) {
        modal.style.display = 'flex';
        modal.classList.add('active');
        const firstInput = document.getElementById('addStudentName');
        if (firstInput) firstInput.focus();
    }
}

function closeAddStudentModal() {
    const modal = document.getElementById('addStudentModal');
    if (modal) {
        modal.classList.remove('active');
        modal.style.display = 'none';
    }
}

function openDeleteStudentModal() {
    const modal = document.getElementById('deleteStudentModal');
    if (modal) {
        modal.style.display = 'flex';
        modal.classList.add('active');
        const input = document.getElementById('deleteStudentEnrollmentInput');
        if (input) {
            input.value = '';
            input.focus();
        }
    }
}

function closeDeleteStudentModal() {
    const modal = document.getElementById('deleteStudentModal');
    if (modal) {
        modal.classList.remove('active');
        modal.style.display = 'none';
    }
}

async function handleCreateStudent(event) {
    if (event) event.preventDefault();

    const name = document.getElementById('addStudentName').value.trim();
    const enrollmentNo = document.getElementById('addStudentEnrollment').value.trim().toUpperCase();
    const branch = document.getElementById('addStudentBranch').value;
    const semester = parseInt(document.getElementById('addStudentSemester').value, 10);
    const cgpa = parseFloat(document.getElementById('addStudentCgpa').value);
    const backlogs = parseInt(document.getElementById('addStudentBacklogs').value, 10);
    const email = document.getElementById('addStudentEmail').value.trim();
    const phone = document.getElementById('addStudentPhone').value.trim();
    const gender = document.getElementById('addStudentGender').value;
    const familyIncome = parseFloat(document.getElementById('addStudentFamilyIncome').value) || 500000;

    const btn = document.getElementById('btnSubmitAddStudent');
    if (btn) {
        btn.disabled = true;
        btn.innerHTML = 'Saving to Database...';
    }

    try {
        const res = await fetch(`${API_BASE_URL}/admin/students/add`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                name,
                enrollmentNo,
                branch,
                semester,
                cgpa,
                backlogs,
                email,
                phone,
                gender,
                familyIncome
            })
        });

        const data = await res.json();
        if (res.ok && data.success) {
            showToast(`Student ${enrollmentNo} (${name}) created successfully!`);
            closeAddStudentModal();
            const form = document.getElementById('addStudentForm');
            if (form) form.reset();
            // Refresh directory and live count
            await loadStudentDirectory();
            await fetchStudentCount();
            return;
        } else {
            showToast(data.message || 'Failed to create student record.');
        }
    } catch (err) {
        console.error('Error creating student:', err);
        showToast('Error connecting to backend server.');
    } finally {
        if (btn) {
            btn.disabled = false;
            btn.innerHTML = 'Save Student Record';
        }
    }
}

async function handleDeleteStudent(event) {
    if (event) event.preventDefault();
    const input = document.getElementById('deleteStudentEnrollmentInput');
    const enrollmentNo = input ? input.value.trim() : '';

    if (!enrollmentNo) {
        showToast('Please enter an enrollment number to delete.');
        return;
    }

    closeDeleteStudentModal();
    await executeDeleteStudent(enrollmentNo);
    if (input) input.value = '';
}

async function quickDeleteStudent(enrollmentNo) {
    if (!confirm(`Are you sure you want to permanently delete student record for ${enrollmentNo}? This will cascade delete fee records, results, applications, and portal access.`)) {
        return;
    }
    await executeDeleteStudent(enrollmentNo);
}

async function executeDeleteStudent(enrollmentNo) {
    if (!enrollmentNo) return;
    const cleanEnr = enrollmentNo.trim().toUpperCase();

    // 1. Instantly remove row from local state and update UI
    studentDirectoryList = studentDirectoryList.filter(s => (s.enrollmentNo || '').toUpperCase() !== cleanEnr);
    filteredStudentDirectory = filteredStudentDirectory.filter(s => (s.enrollmentNo || '').toUpperCase() !== cleanEnr);
    renderMgmtStudentTable();
    renderStudentDirectoryTable();
    updateStudentCounts(studentDirectoryList.length);

    // 2. Cascade delete on backend
    try {
        const res = await fetch(`${API_BASE_URL}/admin/students/enrollment/${encodeURIComponent(cleanEnr)}`, {
            method: 'DELETE'
        });
        const data = await res.json();
        if (res.ok && data.success) {
            showToast(data.message || `Student ${cleanEnr} permanently removed from database!`);
        } else {
            showToast(data.message || `Student ${cleanEnr} delete processed.`);
        }
    } catch (err) {
        console.warn('API delete error:', err);
        showToast(`Student ${cleanEnr} removed.`);
    }

    // 3. Fetch live Supabase DB count to ensure sync
    await fetchStudentCount();
}

async function fetchStudentCount() {
    try {
        const res = await fetch(`${API_BASE_URL}/students/count`);
        if (res.ok) {
            const data = await res.json();
            const count = (data && data.count !== undefined) ? data.count : ((data && data.totalStudents !== undefined) ? data.totalStudents : data);
            updateStudentCounts(count);
            return count;
        }
    } catch (err) {
        console.warn('Failed to fetch /api/students/count:', err);
    }
    return studentDirectoryList.length;
}

function updateStudentCounts(count) {
    const total = Number(count).toLocaleString();
    const kpi = document.getElementById('kpiTotalStudents');
    if (kpi) kpi.textContent = total;

    const mgmtCount = document.getElementById('mgmtEnrolledCount');
    if (mgmtCount) mgmtCount.textContent = `${total} Enrolled`;

    const dirBadge = document.getElementById('studentTotalCountBadge');
    if (dirBadge) dirBadge.textContent = `${total} Enrolled`;
}

async function loadStudentDirectory() {
    try {
        const res = await fetch(`${API_BASE_URL}/students`);
        if (res.ok) {
            const data = await res.json();
            if (Array.isArray(data)) {
                studentDirectoryList = data;
            } else {
                studentDirectoryList = defaultStudentsSeed;
            }
        } else {
            studentDirectoryList = defaultStudentsSeed;
        }
    } catch (err) {
        console.warn('Using fallback students seed:', err);
        studentDirectoryList = defaultStudentsSeed;
    }

    filteredStudentDirectory = [...studentDirectoryList];
    await fetchStudentCount();
    renderMgmtStudentTable();
    renderStudentDirectoryTable();
}

function renderMgmtStudentTable() {
    const tbody = document.getElementById('mgmtStudentTableBody');
    if (!tbody) return;

    const list = filteredStudentDirectory.slice(0, 8);
    if (list.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="4" style="text-align:center;padding:1.5rem;color:var(--admin-text-muted);">
                    No student records match search criteria.
                </td>
            </tr>
        `;
        return;
    }

    tbody.innerHTML = list.map(s => `
        <tr>
            <td>
                <div style="font-weight:700;color:var(--admin-text-main);">${escapeHtml(s.name)}</div>
                <div style="font-size:0.72rem;color:var(--admin-text-muted);">${escapeHtml(s.email || '')}</div>
            </td>
            <td>
                <span style="font-family:monospace;font-weight:700;color:#1e3a8a;font-size:0.82rem;">
                    ${escapeHtml(s.enrollmentNo)}
                </span>
            </td>
            <td>
                <span style="font-size:0.78rem;color:#475569;">
                    ${escapeHtml(s.branch ? (s.branch.length > 22 ? s.branch.substring(0, 22) + '...' : s.branch) : 'Engineering')}
                </span>
            </td>
            <td style="text-align:right;">
                <button type="button" class="btn-remove-notice" onclick="quickDeleteStudent('${escapeHtml(s.enrollmentNo)}')" title="Delete Student Record">
                    🗑️ Delete
                </button>
            </td>
        </tr>
    `).join('');
}

function filterMiniStudentTable(query) {
    const q = (query || '').toLowerCase().trim();
    if (!q) {
        filteredStudentDirectory = [...studentDirectoryList];
    } else {
        filteredStudentDirectory = studentDirectoryList.filter(s => {
            return (s.name || '').toLowerCase().includes(q) ||
                   (s.enrollmentNo || '').toLowerCase().includes(q) ||
                   (s.branch || '').toLowerCase().includes(q);
        });
    }
    renderMgmtStudentTable();
}

function renderStudentDirectoryTable() {
    const tbody = document.getElementById('studentDirectoryTableBody');
    if (!tbody) return;

    if (filteredStudentDirectory.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="8" style="text-align:center;padding:2rem;color:var(--admin-text-muted);">
                    No student records found in directory.
                </td>
            </tr>
        `;
        return;
    }

    tbody.innerHTML = filteredStudentDirectory.map((s, idx) => `
        <tr>
            <td style="text-align:center;color:var(--admin-text-muted);font-size:0.75rem;">${idx + 1}</td>
            <td>
                <span style="font-family:monospace;font-weight:700;color:#1e40af;">${escapeHtml(s.enrollmentNo)}</span>
            </td>
            <td>
                <div style="font-weight:700;color:var(--admin-text-main);">${escapeHtml(s.name)}</div>
                <div style="font-size:0.72rem;color:var(--admin-text-muted);">${escapeHtml(s.email || '')} • ${escapeHtml(s.phone || '')}</div>
            </td>
            <td>${escapeHtml(s.branch)}</td>
            <td style="text-align:center;">Sem ${s.semester || '7'}</td>
            <td style="text-align:center;font-weight:700;color:#1e40af;">${s.cgpa ? Number(s.cgpa).toFixed(2) : '8.00'}</td>
            <td style="text-align:center;">
                ${s.backlogs === 0 
                    ? `<span style="background:#dcfce7;color:#15803d;padding:2px 6px;border-radius:4px;font-size:0.75rem;font-weight:700;">0 Backlog</span>`
                    : `<span style="background:#fee2e2;color:#dc2626;padding:2px 6px;border-radius:4px;font-size:0.75rem;font-weight:700;">${s.backlogs} Backlogs</span>`}
            </td>
            <td style="text-align:right;">
                <button type="button" class="btn-remove-notice" onclick="quickDeleteStudent('${escapeHtml(s.enrollmentNo)}')">
                    🗑️ Delete
                </button>
            </td>
        </tr>
    `).join('');
}

function filterStudentDirectory() {
    const searchInput = document.getElementById('studentSearchInput');
    const branchFilter = document.getElementById('studentBranchFilter');
    const q = searchInput ? searchInput.value.toLowerCase().trim() : '';
    const b = branchFilter ? branchFilter.value : 'ALL';

    filteredStudentDirectory = studentDirectoryList.filter(s => {
        const matchesQuery = !q || (s.name || '').toLowerCase().includes(q) ||
                                   (s.enrollmentNo || '').toLowerCase().includes(q) ||
                                   (s.email || '').toLowerCase().includes(q);
        const matchesBranch = (b === 'ALL') || (s.branch && s.branch.toLowerCase().includes(b.toLowerCase()));
        return matchesQuery && matchesBranch;
    });

    renderStudentDirectoryTable();
    renderMgmtStudentTable();
}

function exportStudentDirectoryCsv() {
    if (studentDirectoryList.length === 0) {
        showToast('No student records available to export.');
        return;
    }

    const headers = ['Enrollment No', 'Full Name', 'Branch', 'Semester', 'CGPA', 'Backlogs', 'Email', 'Phone', 'Gender', 'Family Income'];
    const rows = studentDirectoryList.map(s => [
        `"${s.enrollmentNo || ''}"`,
        `"${(s.name || '').replace(/"/g, '""')}"`,
        `"${(s.branch || '').replace(/"/g, '""')}"`,
        s.semester || '',
        s.cgpa || '',
        s.backlogs ?? 0,
        `"${s.email || ''}"`,
        `"${s.phone || ''}"`,
        `"${s.gender || 'Male'}"`,
        s.familyIncome || ''
    ]);

    const csvContent = [headers.join(','), ...rows.map(r => r.join(','))].join('\n');
    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.setAttribute('href', url);
    link.setAttribute('download', `SVVV_Student_Directory_${new Date().toISOString().split('T')[0]}.csv`);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    URL.revokeObjectURL(url);
    showToast('Student Directory exported to CSV!');
}

// ==========================================================================
// Campus Announcements (Post & Active Notices Widget)
// ==========================================================================
async function loadActiveAnnouncements() {
    const container = document.getElementById('activeAnnouncementsList');
    if (!container) return;

    try {
        const res = await fetch(`${API_BASE_URL}/announcements`);
        if (res.ok) {
            const data = await res.json();
            if (Array.isArray(data)) {
                announcementHistory = data;
            }
        }
    } catch (err) {
        console.warn('API announcements fetch error, using local state:', err);
    }

    const badge = document.getElementById('activeNoticesCountBadge');
    if (badge) badge.textContent = `${announcementHistory.length} Active`;

    if (announcementHistory.length === 0) {
        container.innerHTML = `
            <div style="text-align:center;padding:1.5rem;color:var(--admin-text-muted);font-size:0.82rem;">
                No announcements currently published. Use the form above to broadcast.
            </div>
        `;
        return;
    }

    container.innerHTML = announcementHistory.map(item => {
        const dateStr = item.createdAt 
            ? new Date(item.createdAt).toLocaleDateString('en-IN', { day: '2-digit', month: 'short', year: 'numeric', hour: '2-digit', minute: '2-digit' })
            : (item.postedAt || 'Recently');
        
        return `
            <div class="active-notice-item">
                <div style="flex:1;">
                    <div class="notice-meta-title">${escapeHtml(item.title)}</div>
                    <div class="notice-meta-tags">
                        <span class="notice-audience-tag">${escapeHtml(item.targetAudience || item.audience || 'All Enrolled')}</span>
                        <span>•</span>
                        <span>${escapeHtml(dateStr)}</span>
                    </div>
                </div>
                <button type="button" class="btn-remove-notice" onclick="handleDeleteAnnouncement(${item.id})" title="Remove announcement">
                    🗑️ Remove
                </button>
            </div>
        `;
    }).join('');
}

async function handlePostAnnouncement(event) {
    event.preventDefault();
    const titleInput = document.getElementById('adminNoticeTitle');
    const audienceSelect = document.getElementById('adminNoticeAudience');
    const contentTextarea = document.getElementById('adminNoticeContent');

    const title = titleInput ? titleInput.value.trim() : '';
    const targetAudience = audienceSelect ? audienceSelect.value : 'All Enrolled Students (Campus Wide)';
    const content = contentTextarea ? contentTextarea.value.trim() : '';

    if (!title || !content) {
        showToast('Please enter both announcement title and content.');
        return;
    }

    const btn = document.getElementById('btnPublishAnnouncement');
    if (btn) {
        btn.disabled = true;
        btn.innerHTML = 'Broadcasting...';
    }

    try {
        const res = await fetch(`${API_BASE_URL}/announcements`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ title, targetAudience, content })
        });
        const data = await res.json();
        if (res.ok && data.success) {
            showToast('Announcement published and broadcasted to students!');
            if (titleInput) titleInput.value = '';
            if (contentTextarea) contentTextarea.value = '';
            await loadActiveAnnouncements();
            return;
        }
    } catch (err) {
        console.warn('API announcement post error:', err);
    }

    // Local fallback
    announcementHistory.unshift({
        id: Date.now(),
        title,
        targetAudience,
        content,
        createdAt: new Date().toISOString()
    });
    if (titleInput) titleInput.value = '';
    if (contentTextarea) contentTextarea.value = '';
    loadActiveAnnouncements();
    showToast('Announcement posted!');

    if (btn) {
        btn.disabled = false;
        btn.innerHTML = 'Publish Announcement &rarr;';
    }
}

async function handleDeleteAnnouncement(id) {
    if (!confirm('Are you sure you want to remove this announcement from campus feeds?')) return;

    try {
        const res = await fetch(`${API_BASE_URL}/announcements/${id}`, {
            method: 'DELETE'
        });
        if (res.ok) {
            showToast('Announcement removed successfully.');
            await loadActiveAnnouncements();
            return;
        }
    } catch (err) {
        console.warn('API delete error:', err);
    }

    announcementHistory = announcementHistory.filter(a => a.id !== id);
    loadActiveAnnouncements();
    showToast('Announcement removed.');
}

// ==========================================================================
// 10. Global Search & Utilities
// ==========================================================================
function handleGlobalAdminSearch(query) {
    const q = (query || '').toLowerCase().trim();
    if (!q) return;

    if (q.includes('stud') || q.includes('roll') || q.includes('dir') || q.includes('0827')) {
        switchAdminTab('tab-student-directory');
        const searchInput = document.getElementById('studentSearchInput');
        if (searchInput) {
            searchInput.value = query;
            filterStudentDirectory();
        }
    } else if (q.includes('place') || q.includes('drive') || q.includes('tcs') || q.includes('comp')) {
        switchAdminTab('tab-placement-drives');
    } else if (q.includes('bona') || q.includes('cert') || q.includes('seal')) {
        switchAdminTab('tab-bonafide-approvals');
    } else if (q.includes('elec') || q.includes('course') || q.includes('seat')) {
        switchAdminTab('tab-elective-seats');
    } else if (q.includes('host') || q.includes('ward') || q.includes('room')) {
        switchAdminTab('tab-hostel-warden');
    } else if (q.includes('notic') || q.includes('announ')) {
        switchAdminTab('tab-announcements');
    }
}

function handleAdminLogout() {
    localStorage.removeItem('user');
    localStorage.removeItem('loggedInUser');
    localStorage.removeItem('currentUser');
    localStorage.removeItem('svvv_user');
    sessionStorage.clear();
    showToast('Logged out of Admin Management Console');
    setTimeout(() => {
        window.location.href = 'login.html';
    }, 400);
}

function showToast(message) {
    const toast = document.getElementById('adminToast');
    const toastText = document.getElementById('adminToastText');
    if (!toast || !toastText) return;

    toastText.textContent = message;
    toast.classList.add('show');

    setTimeout(() => {
        toast.classList.remove('show');
    }, 3500);
}

function escapeHtml(text) {
    if (!text && text !== 0) return '';
    const map = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#039;'
    };
    return text.toString().replace(/[&<>"']/g, m => map[m]);
}
