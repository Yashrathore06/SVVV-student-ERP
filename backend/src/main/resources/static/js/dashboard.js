// API Base URL
const API_BASE_URL = window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1'
  ? 'http://localhost:8080/api'
  : '/api';

// Current session
let currentUser = null;
let currentProfile = null;

// DOM Elements
const topStudentName = document.getElementById('topStudentName');
const topRollNo = document.getElementById('topRollNo');
const userAvatar = document.getElementById('userAvatar');
const welcomeTitle = document.getElementById('welcomeTitle');
const bannerSemester = document.getElementById('bannerSemester');

// Views & Nav Items
const dashboardView = document.getElementById('dashboardView');
const academicsView = document.getElementById('academicsView');
const profileView = document.getElementById('profileView');
const placementView = document.getElementById('placementView');
const electivesView = document.getElementById('electivesView');
const bonafideView = document.getElementById('bonafideView');
const feesView = document.getElementById('feesView');
const examinationView = document.getElementById('examinationView');
const navDashboard = document.getElementById('navDashboard');
const navAcademics = document.getElementById('navAcademics');
const navProfile = document.getElementById('navProfile');
const navPlacement = document.getElementById('navPlacement');
const navElectives = document.getElementById('navElectives');
const navBonafide = document.getElementById('navBonafide');
const navFees = document.getElementById('navFees');
const navExamination = document.getElementById('navExamination');
const logoutBtn = document.getElementById('logoutBtn');

// Academics Elements
const academicCgpaVal = document.getElementById('academicCgpaVal');
const academicBacklogEnroll = document.getElementById('academicBacklogEnroll');

// Placement Elements
const placementCriteriaSnapshot = document.getElementById('placementCriteriaSnapshot');
const placementResumeName = document.getElementById('placementResumeName');
const atsScoreText = document.getElementById('atsScoreText');
const resumeUpdatedText = document.getElementById('resumeUpdatedText');
const drivesCountBadge = document.getElementById('drivesCountBadge');
const drivesGridContainer = document.getElementById('drivesGridContainer');
const appliedDrivesTableBody = document.getElementById('appliedDrivesTableBody');

// Electives Elements
const electiveStatusPill = document.getElementById('electiveStatusPill');
const electiveStatusPillText = document.getElementById('electiveStatusPillText');
const kpiTotalOptions = document.getElementById('kpiTotalOptions');
const kpiAggregatedCapacity = document.getElementById('kpiAggregatedCapacity');
const kpiOverallVacancy = document.getElementById('kpiOverallVacancy');
const kpiWindowCloses = document.getElementById('kpiWindowCloses');
const electivePoolContainer = document.getElementById('electivePoolContainer');
const electiveCardsList = document.getElementById('electiveCardsList');
const enrolledElectiveContainer = document.getElementById('enrolledElectiveContainer');
const allocCourseCode = document.getElementById('allocCourseCode');
const allocCourseTitle = document.getElementById('allocCourseTitle');
const allocCredits = document.getElementById('allocCredits');
const allocDomain = document.getElementById('allocDomain');
const allocFaculty = document.getElementById('allocFaculty');
const allocVenue = document.getElementById('allocVenue');
const allocVenueSpan = document.getElementById('allocVenueSpan');
const allocSchedule = document.getElementById('allocSchedule');
const allocStudentName = document.getElementById('allocStudentName');
const allocRollNo = document.getElementById('allocRollNo');
const allocTimestamp = document.getElementById('allocTimestamp');
const allocToken = document.getElementById('allocToken');

// Bonafide Certificate Elements
const bonafideStatusPill = document.getElementById('bonafideStatusPill');
const bonafideStatusDot = document.getElementById('bonafideStatusDot');
const bonafideStatusText = document.getElementById('bonafideStatusText');
const stepCard1 = document.getElementById('stepCard1');
const stepCard2 = document.getElementById('stepCard2');
const stepCard3 = document.getElementById('stepCard3');
const bonafideActionContainer = document.getElementById('bonafideActionContainer');

// Bonafide Modals Elements
const bonafideApplyModal = document.getElementById('bonafideApplyModal');
const bonafideApplyForm = document.getElementById('bonafideApplyForm');
const bonaFormStudentName = document.getElementById('bonaFormStudentName');
const bonaFormRollNo = document.getElementById('bonaFormRollNo');
const bonaFormBranch = document.getElementById('bonaFormBranch');
const bonaInputFatherName = document.getElementById('bonaInputFatherName');
const bonaSelectPurpose = document.getElementById('bonaSelectPurpose');
const bonaInputRemarks = document.getElementById('bonaInputRemarks');
const btnSubmitBonaForm = document.getElementById('btnSubmitBonaForm');

const bonafideCertModal = document.getElementById('bonafideCertModal');
const certRefNo = document.getElementById('certRefNo');
const certIssueDate = document.getElementById('certIssueDate');
const certStudentName = document.getElementById('certStudentName');
const certFatherName = document.getElementById('certFatherName');
const certRollNo = document.getElementById('certRollNo');
const certBranch = document.getElementById('certBranch');
const certSemester = document.getElementById('certSemester');
const certSession = document.getElementById('certSession');
const certPurpose = document.getElementById('certPurpose');
const certQrToken = document.getElementById('certQrToken');

// Call Letter Modal Elements
const callLetterModal = document.getElementById('callLetterModal');
const modalCandidateName = document.getElementById('modalCandidateName');
const modalEnrollment = document.getElementById('modalEnrollment');
const modalCompanyName = document.getElementById('modalCompanyName');
const modalJobRole = document.getElementById('modalJobRole');
const modalDateTime = document.getElementById('modalDateTime');
const modalVenue = document.getElementById('modalVenue');
const modalToken = document.getElementById('modalToken');
const helpdeskModal = document.getElementById('helpdeskModal');

// Data Cache
let activePlacementDrives = [];
let myAppliedDrivesList = [];
let currentBonafideData = null;

// ==========================================================================
// Check Session on Page Load
// ==========================================================================
document.addEventListener('DOMContentLoaded', () => {
    let user = null;
    try {
        const rawUser = 
            localStorage.getItem('user') || 
            sessionStorage.getItem('user') || 
            localStorage.getItem('loggedInUser') || 
            sessionStorage.getItem('loggedInUser') || 
            localStorage.getItem('currentUser') || 
            sessionStorage.getItem('currentUser') || 
            localStorage.getItem('svvv_user') || 
            sessionStorage.getItem('svvv_user');

        user = rawUser ? JSON.parse(rawUser) : null;
    } catch (e) {
        console.error('Session error:', e);
        user = null;
    }

    const userRole = (user && user.role) ? user.role.toUpperCase() : '';

    if (!user || !userRole || (userRole !== 'ROLE_STUDENT' && userRole !== 'STUDENT')) {
        alert("This dashboard is for students. Please login as a student.");
        window.location.href = "login.html";
        return;
    }

    currentUser = user;
    if (!currentUser.studentId && currentUser.id) {
        currentUser.studentId = currentUser.id;
    }

    initializeDashboardUI(currentUser);
    loadFullProfile(currentUser.studentId);
    loadPlacementData(currentUser.studentId);
    loadElectivesData(currentUser.studentId);
    loadAcademicSubjects(currentUser.branch, currentUser.semester);
    loadAcademicCalendar();
    loadStudentCampusAnnouncements();

    // Check URL Hash for direct view navigation (e.g. #profile, #placement, #electives, #bonafide)
    if (window.location.hash) {
        const targetView = window.location.hash.replace('#', '').toLowerCase();
        if (['academics', 'profile', 'placement', 'electives', 'bonafide', 'fees', 'examination', 'dashboard'].includes(targetView)) {
            switchView(targetView);
        }
    }

    // Bind Sidebar Logout Button
    document.getElementById('logoutBtn')?.addEventListener('click', (e) => {
        e.preventDefault();
        handleLogout();
    });

    // Mobile Navigation Drawer Toggle
    initMobileSidebarNavigation();
});

// ==========================================================================
// Mobile Sidebar Drawer Navigation Logic
// ==========================================================================
function initMobileSidebarNavigation() {
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

    // Auto-close sidebar on mobile when any sidebar navigation link is clicked
    const allSidebarLinks = document.querySelectorAll(".sidebar-link, .nav-link, .nav-item a, .nav-item");
    allSidebarLinks.forEach(link => {
        link.addEventListener("click", () => {
            if (window.innerWidth <= 992) {
                if (sidebar) sidebar.classList.remove("active");
                if (sidebarOverlay) sidebarOverlay.classList.remove("active");
            }
        });
    });
}

// ==========================================================================
// Global Logout Handler
// ==========================================================================
function handleLogout() {
    // Clear user session from storage
    localStorage.removeItem('user');
    localStorage.removeItem('loggedInUser');
    localStorage.removeItem('currentUser');
    localStorage.removeItem('svvv_user');
    sessionStorage.clear();
    
    // Redirect to login page
    window.location.href = 'login.html';
}
window.handleLogout = handleLogout;

// ==========================================================================
// Initialize UI with Session Data
// ==========================================================================
function initializeDashboardUI(user) {
    const fullName = user.name || 'Student';
    const firstName = fullName.split(' ')[0];
    const roll = user.enrollmentNo || user.username || '2301826';
    const branch = user.branch || 'B.Tech CSE';
    const sem = user.semester || 7;

    // Top Header
    topStudentName.textContent = fullName;
    topRollNo.textContent = `Roll No: ${roll}`;
    
    // Avatar Initials
    const initials = fullName.split(' ').map(n => n[0]).join('').substring(0, 2).toUpperCase();
    userAvatar.textContent = initials || 'ST';

    // Banner
    welcomeTitle.textContent = `Welcome back, ${firstName}!`;
    bannerSemester.textContent = `SEMESTER ${toRoman(sem)} ACTIVE | ${branch} • Sec G`;
}

function toRoman(num) {
    const roman = { 1: 'I', 2: 'II', 3: 'III', 4: 'IV', 5: 'V', 6: 'VI', 7: 'VII', 8: 'VIII' };
    return roman[num] || num;
}

// ==========================================================================
// Load Live Profile from Backend
// ==========================================================================
async function loadFullProfile(studentId) {
    const sId = studentId || (currentUser && (currentUser.studentId || currentUser.id));
    const uId = (currentUser && (currentUser.userId || currentUser.id)) || sId;
    if (!sId && !uId) return;

    try {
        let response = null;
        if (uId) {
            response = await fetch(`${API_BASE_URL}/students/user/${uId}`);
        }
        if (!response || !response.ok) {
            response = await fetch(`${API_BASE_URL}/students/me?studentId=${sId || uId}`);
        }
        if (!response || !response.ok) {
            response = await fetch(`${API_BASE_URL}/students/${sId}/profile`);
        }

        if (response && response.ok) {
            currentProfile = await response.json();
            if (currentProfile.id) {
                currentUser.studentId = currentProfile.id;
            }
            renderProfile(currentProfile);
            initializeDashboardUI(currentProfile);
            updatePlacementProfileSnapshot(currentProfile);
            loadAcademicSubjects(currentProfile.branch, currentProfile.semester);
            loadStudentCampusAnnouncements();
        }
    } catch (err) {
        console.error('Error fetching student profile:', err);
    }
}

function renderProfile(profile) {
    // Academic (Read-Only)
    profName.textContent = profile.name || '--';
    profEnrollment.textContent = profile.enrollmentNo || '--';
    profBranch.textContent = profile.branch || '--';
    profSemester.textContent = `Semester ${profile.semester || '--'}`;
    profCgpa.textContent = profile.cgpa ? `${profile.cgpa} / 10.0` : 'N/A';
    profBacklogs.textContent = profile.backlogs != null ? profile.backlogs : '0';

    // Editable Inputs
    editEmail.value = profile.email || '';
    editPhone.value = profile.phone || '';
    editIncome.value = profile.familyIncome || '';
    editAddress.value = profile.address || '';

    // Render Resumes
    renderResumes(profile.resumes || []);

    // Update Academics Module KPI & Backlog Info
    if (academicCgpaVal) academicCgpaVal.textContent = profile.cgpa ? profile.cgpa.toFixed(2) : '7.98';
    if (academicBacklogEnroll) {
        const roll = profile.enrollmentNo || (currentUser && currentUser.enrollmentNo) || '23100BTCSE15208';
        const name = profile.name || (currentUser && currentUser.name) || 'Yash Rathore';
        academicBacklogEnroll.textContent = `${roll} (${name})`;
    }
}

function updatePlacementProfileSnapshot(profile) {
    if (!profile) return;

    const cgpa = profile.cgpa ? profile.cgpa.toFixed(2) : '8.45';
    const branch = profile.branch ? profile.branch : 'B.Tech CSE';
    const backlogs = profile.backlogs != null ? profile.backlogs : 0;

    if (placementCriteriaSnapshot) {
        placementCriteriaSnapshot.innerHTML = `<strong>(CGPA: ${cgpa} | Branch: ${escapeHtml(branch)} | Active Backlogs: ${backlogs})</strong>`;
    }

    // Update central stored resume card in placement view
    if (profile.resumes && profile.resumes.length > 0) {
        const latestResume = profile.resumes[0];
        if (placementResumeName) placementResumeName.textContent = latestResume.fileName;
        const daysAgo = Math.max(1, Math.floor((new Date() - new Date(latestResume.uploadedAt)) / (1000 * 60 * 60 * 24)));
        if (resumeUpdatedText) resumeUpdatedText.textContent = `Updated ${daysAgo}d ago`;
    } else {
        if (placementResumeName) placementResumeName.textContent = 'No Resume Uploaded';
        if (resumeUpdatedText) resumeUpdatedText.textContent = '';
    }
}

function renderResumes(resumes) {
    if (!resumes || resumes.length === 0) {
        resumesListContainer.innerHTML = `
            <div style="text-align:center;padding:1.5rem;color:var(--text-muted);font-size:0.85rem">
                No resume uploaded yet. Upload your PDF/DOCX resume to enable 1-click placement applications.
            </div>
        `;
        return;
    }

    resumesListContainer.innerHTML = resumes.map((r, idx) => `
        <div class="resume-item-card">
            <div style="display:flex;align-items:center;gap:0.75rem">
                <svg width="24" height="24" fill="none" stroke="#2563eb" stroke-width="2" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"/>
                </svg>
                <div>
                    <div style="font-weight:600;font-size:0.88rem;color:var(--text-main)">
                        ${escapeHtml(r.fileName)}
                        ${idx === 0 ? '<span style="font-size:0.68rem;background:#dbeafe;color:#1e40af;padding:2px 6px;border-radius:4px;margin-left:6px;font-weight:700">LATEST / ACTIVE</span>' : ''}
                    </div>
                    <div style="font-size:0.75rem;color:var(--text-muted)">Uploaded on ${new Date(r.uploadedAt).toLocaleDateString()}</div>
                </div>
            </div>
            <div style="display:flex;align-items:center;gap:0.5rem">
                <a href="${API_BASE_URL}/resumes/${r.id}/download" target="_blank" class="btn-service-action secondary" style="padding:0.35rem 0.65rem;font-size:0.78rem">
                    Download
                </a>
                <button onclick="deleteResume(${r.id})" class="btn-service-action" style="padding:0.35rem 0.65rem;font-size:0.78rem;background:#fee2e2;color:#991b1b;border:none">
                    Delete
                </button>
            </div>
        </div>
    `).join('');
}

function escapeHtml(text) {
    if (!text) return '';
    return text.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
}

// ==========================================================================
// Profile Update Submission
// ==========================================================================
profileEditForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    if (!currentUser || !currentUser.studentId) return;

    saveProfileBtn.disabled = true;
    saveProfileBtn.textContent = 'Saving changes...';

    try {
        const payload = {
            email: editEmail.value.trim(),
            phone: editPhone.value.trim(),
            familyIncome: parseFloat(editIncome.value) || null,
            address: editAddress.value.trim()
        };

        const response = await fetch(`${API_BASE_URL}/students/${currentUser.studentId}/profile`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (response.ok) {
            const updated = await response.json();
            currentProfile = updated;
            renderProfile(updated);
            updatePlacementProfileSnapshot(updated);
            showToast('Student profile updated successfully in Supabase PostgreSQL!');
        } else {
            showToast('Failed to update profile. Please try again.');
        }
    } catch (err) {
        showToast('Error connecting to server.');
        console.error(err);
    } finally {
        saveProfileBtn.disabled = false;
        saveProfileBtn.textContent = 'Save Profile Changes';
    }
});

// ==========================================================================
// Resume Upload Handling
// ==========================================================================
resumeFileInput.addEventListener('change', async () => {
    const file = resumeFileInput.files[0];
    if (!file || !currentUser || !currentUser.studentId) return;

    if (file.size > 5 * 1024 * 1024) {
        alert('File size exceeds 5MB limit.');
        return;
    }

    const formData = new FormData();
    formData.append('file', file);

    showToast('Uploading resume...');

    try {
        const response = await fetch(`${API_BASE_URL}/students/${currentUser.studentId}/resumes/upload`, {
            method: 'POST',
            body: formData
        });

        if (response.ok) {
            showToast('Resume uploaded and saved for 1-click placement reuse!');
            resumeFileInput.value = '';
            loadFullProfile(currentUser.studentId);
            loadPlacementData(currentUser.studentId);
        } else {
            const err = await response.json();
            showToast(err.error || 'Failed to upload resume.');
        }
    } catch (err) {
        console.error(err);
        showToast('Server error while uploading resume.');
    }
});

// ==========================================================================
// Resume Delete Handling
// ==========================================================================
async function deleteResume(resumeId) {
    if (!confirm('Are you sure you want to delete this resume?')) return;

    try {
        const response = await fetch(`${API_BASE_URL}/resumes/${resumeId}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            showToast('Resume removed.');
            loadFullProfile(currentUser.studentId);
            loadPlacementData(currentUser.studentId);
        } else {
            showToast('Could not delete resume.');
        }
    } catch (err) {
        showToast('Server error while deleting resume.');
    }
}

// ==========================================================================
// Placement Module: Load Drives & Applications
// ==========================================================================
async function loadPlacementData(studentId) {
    if (!studentId) return;

    try {
        const studentBranch = (currentProfile && currentProfile.branch) || (currentUser && currentUser.branch) || '';
        const branchParam = studentBranch ? `&branch=${encodeURIComponent(studentBranch)}` : '';

        // 1. Fetch available drives evaluated for this student
        const drivesRes = await fetch(`${API_BASE_URL}/placements/drives?studentId=${studentId}${branchParam}`);
        if (drivesRes.ok) {
            activePlacementDrives = await drivesRes.json();
            renderPlacementDrives(activePlacementDrives);
        }

        // 2. Fetch applied drives status history
        const appliedRes = await fetch(`${API_BASE_URL}/placements/my-applications?studentId=${studentId}`);
        if (appliedRes.ok) {
            myAppliedDrivesList = await appliedRes.json();
            renderAppliedDrivesTable(myAppliedDrivesList);
        }
    } catch (err) {
        console.error('Error loading placement data:', err);
    }
}

function isStudentBranchEligible(studentBranch, allowedBranch) {
    if (!allowedBranch || allowedBranch.trim().toUpperCase() === 'ALL' || allowedBranch.trim() === '') {
        return true;
    }
    if (!studentBranch || studentBranch.trim() === '') {
        return false;
    }

    const sUpper = studentBranch.trim().toUpperCase();
    const aUpper = allowedBranch.trim().toUpperCase();

    // Direct match
    if (aUpper === sUpper || aUpper.includes('ALL')) {
        return true;
    }

    const isBTech = sUpper.includes('B.TECH') || sUpper.includes('BTECH');
    const isBSc = sUpper.includes('B.SC') || sUpper.includes('BSC');
    const isBca = sUpper.includes('BCA');
    const isMba = sUpper.includes('MBA');

    const allowedTokens = aUpper.split(/[,;/|]+/).map(t => t.trim()).filter(Boolean);

    for (const token of allowedTokens) {
        if (token === 'ALL') return true;

        if (isBTech) {
            if (token.includes('B.SC') || token.includes('BSC') || token.includes('MBA') || token.includes('BCA')) {
                continue;
            }
            if (token.includes('B.TECH') || token.includes('BTECH') || token.includes('CSE') || 
                token.includes('COMPUTER SCIENCE & ENGINEERING') || token.includes('CS & ENG')) {
                return true;
            }
            if (token === 'CS' || token === 'IT' || token === 'CSE') {
                return true;
            }
        } else if (isBSc) {
            if (token.includes('B.TECH') || token.includes('BTECH') || token.includes('MBA') || token.includes('BCA')) {
                continue;
            }
            if (token.includes('B.SC') || token.includes('BSC') || token.includes('B.SC COMPUTER SCIENCE') || token.includes('B.SC CS')) {
                return true;
            }
            if (token === 'CS' && !token.includes('TECH')) {
                return true;
            }
        } else if (isBca) {
            if (token.includes('B.TECH') || token.includes('B.SC') || token.includes('MBA')) {
                continue;
            }
            if (token.includes('BCA') || token.includes('BACHELOR OF COMPUTER APPLICATIONS')) {
                return true;
            }
        } else if (isMba) {
            if (token.includes('B.TECH') || token.includes('B.SC') || token.includes('BCA')) {
                continue;
            }
            if (token.includes('MBA') || token.includes('MASTER OF BUSINESS ADMINISTRATION')) {
                return true;
            }
        }
    }
    return false;
}

function getBranchDisplayLabel(studentBranch) {
    if (!studentBranch) return 'B.Tech CSE';
    const s = studentBranch.toLowerCase().trim();
    if (s.includes('b.sc') || s.includes('bsc')) return 'B.Sc CS';
    if (s.includes('bca')) return 'BCA';
    if (s.includes('mba')) return 'MBA';
    if (s.includes('b.tech') || s.includes('btech') || s.includes('cse')) return 'B.Tech CSE';
    if (s.includes('computer science')) return 'CSE';
    return studentBranch.trim();
}

function renderPlacementDrives(drives) {
    if (!drivesGridContainer) return;

    const studentBranch = (currentProfile && currentProfile.branch) || 
                          (currentUser && currentUser.branch) || 
                          (drives && drives[0] && drives[0].studentBranch) || 'B.Tech Computer Science & Engineering';

    // Strictly filter out any drive whose allowed_branch does not match the student's branch
    const eligibleBranchDrives = (drives || []).filter(drive => {
        return isStudentBranchEligible(studentBranch, drive.allowedBranch);
    });

    if (eligibleBranchDrives.length === 0) {
        drivesGridContainer.innerHTML = `
            <div class="loading-state-box">No active campus recruitment drives available for your branch at this moment.</div>
        `;
        if (drivesCountBadge) drivesCountBadge.textContent = '0 Listed';
        return;
    }

    if (drivesCountBadge) drivesCountBadge.textContent = `${eligibleBranchDrives.length} Listed`;

    drivesGridContainer.innerHTML = eligibleBranchDrives.map(drive => {
        const logoClass = (drive.logoCode || 'tcs').toLowerCase();
        const studentCgpaStr = drive.studentCgpa ? drive.studentCgpa.toFixed(2) : '8.45';
        const minCgpaStr = drive.minCgpa ? drive.minCgpa.toFixed(1) : '7.0';
        const displayBranch = drive.studentBranch || getBranchDisplayLabel(studentBranch);
        const backlogsStr = drive.studentBacklogs != null ? `${drive.studentBacklogs} Clear` : '0 Clear';

        // Strict branch check on frontend
        const branchMatch = isStudentBranchEligible(studentBranch, drive.allowedBranch);
        const branchPassed = (drive.branchPassed !== false) && branchMatch;

        // Criteria checks
        const cgpaHtml = drive.cgpaPassed 
            ? `<span class="criteria-pill">Yours: ${studentCgpaStr} &#10003;</span>` 
            : `<span class="criteria-pill failed">Yours: ${studentCgpaStr} &#10005;</span>`;
        
        const branchHtml = branchPassed
            ? `<span class="criteria-pill">Yours: ${escapeHtml(displayBranch)} &#10003;</span>`
            : `<span class="criteria-pill failed">Yours: ${escapeHtml(displayBranch)} &#10005;</span>`;

        const backlogsHtml = drive.backlogsPassed
            ? `<span class="criteria-pill">Yours: ${backlogsStr} &#10003;</span>`
            : `<span class="criteria-pill failed">Yours: ${drive.studentBacklogs} Backlogs &#10005;</span>`;

        // Overall Eligibility
        const isEligible = Boolean(drive.eligible && branchPassed);

        // Action Button logic
        let actionBtnHtml = '';

        if (drive.alreadyApplied) {
            actionBtnHtml = `
                <button class="btn-1click-applied" disabled>
                    <svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2.5" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"/>
                    </svg>
                    Applied (${escapeHtml(drive.applicationStatus || 'Under Review')})
                </button>
            `;
        } else if (isEligible) {
            actionBtnHtml = `
                <button class="btn-1click-apply" onclick="handleOneClickApply(${drive.id}, '${escapeHtml(drive.companyName)}')">
                    Apply Now
                </button>
            `;
        } else {
            actionBtnHtml = `
                <button class="btn-ineligible-locked" disabled>
                    Not Eligible
                </button>
            `;
        }

        return `
            <div class="drive-card">
                <div>
                    <!-- Drive Header -->
                    <div class="drive-header">
                        <div class="company-title-group">
                            <div class="company-logo-box ${logoClass}">
                                ${escapeHtml(drive.logoCode || 'SVV')}
                            </div>
                            <div>
                                <h3 class="company-name">${escapeHtml(drive.companyName)}</h3>
                                <p class="company-role">${escapeHtml(drive.jobRole)}</p>
                            </div>
                        </div>
                        <span class="badge-eligibility ${isEligible ? 'eligible' : 'ineligible'}">
                            ${isEligible ? 'Eligible' : 'Not Eligible'}
                        </span>
                    </div>

                    <!-- Package & Bond Box -->
                    <div class="package-bond-box">
                        <div class="stat-group">
                            <span class="stat-label">CTC Package</span>
                            <span class="stat-val">${drive.packageCtc ? drive.packageCtc.toFixed(1) : '7.0'} LPA</span>
                        </div>
                        <div class="stat-group">
                            <span class="stat-label">Service Bond</span>
                            <span class="stat-val">${escapeHtml(drive.serviceBond || '1 Year')}</span>
                        </div>
                    </div>

                    <!-- Criteria Check -->
                    <div class="criteria-matrix-box">
                        <div class="criteria-title">Eligibility Criteria</div>
                        <div class="criteria-rows-list">
                            <div class="criteria-row ${!drive.cgpaPassed ? 'failed' : ''}">
                                <span class="criteria-label">Min CGPA: ${minCgpaStr}</span>
                                ${cgpaHtml}
                            </div>
                            <div class="criteria-row ${!branchPassed ? 'failed' : ''}">
                                <span class="criteria-label">Branches: ${escapeHtml(drive.allowedBranch)}</span>
                                ${branchHtml}
                            </div>
                            <div class="criteria-row ${!drive.backlogsPassed ? 'failed' : ''}">
                                <span class="criteria-label">Max Backlogs: ${drive.maxBacklogs}</span>
                                ${backlogsHtml}
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Action Button -->
                <div class="drive-action-container">
                    ${actionBtnHtml}
                </div>
            </div>
        `;
    }).join('');
}

function renderAppliedDrivesTable(appliedList) {
    if (!appliedDrivesTableBody) return;
    const thead = document.getElementById('appliedDrivesTableHead') || (appliedDrivesTableBody.closest('table') ? appliedDrivesTableBody.closest('table').querySelector('thead') : null);

    // If database has no applied records yet for this student
    if (!appliedList || appliedList.length === 0) {
        if (thead) thead.style.display = 'none';
        appliedDrivesTableBody.innerHTML = `
            <tr>
                <td colspan="5" style="text-align:center;padding:2.5rem 1rem;color:var(--text-muted);">
                    <div style="font-size:1.8rem;margin-bottom:0.5rem">📂</div>
                    <strong style="color:var(--text-main);font-size:0.95rem;display:block;">No Placement Applications Submitted Yet</strong>
                </td>
            </tr>
        `;
        return;
    }

    if (thead) thead.style.display = '';

    appliedDrivesTableBody.innerHTML = appliedList.map(app => {
        const logoClass = (app.logoCode || 'tcs').toLowerCase();
        return `
            <tr>
                <td>
                    <div class="company-table-cell">
                        <div class="table-logo-box ${logoClass}">
                            ${escapeHtml(app.logoCode || 'DRV')}
                        </div>
                        <div>
                            <strong style="color:var(--text-main);font-size:0.92rem">${escapeHtml(app.companyName)}</strong>
                            <div style="font-size:0.75rem;color:var(--text-muted)">${escapeHtml(app.driveRef || 'Drive Ref: #SVVV-2026')}</div>
                        </div>
                    </div>
                </td>
                <td><strong style="font-weight:600">${escapeHtml(app.roleApplied)}</strong></td>
                <td style="color:var(--text-muted)">${escapeHtml(app.appliedDate || 'Recent')}</td>
                <td>
                    <div class="status-badge-container">
                        <span class="applied-status-pill ${app.callLetterAvailable ? 'shortlisted' : 'review'}">
                            &bull; ${escapeHtml(app.statusBadgeText || 'Application Submitted')}
                        </span>
                        <div class="status-sub-note">${escapeHtml(app.statusNote || 'Verification in progress')}</div>
                    </div>
                </td>
                <td style="text-align:right">
                    ${app.callLetterAvailable 
                        ? `<button class="btn-service-action primary" onclick="openCallLetterModal('${escapeHtml(app.companyName)}', '${escapeHtml(app.roleApplied)}', '${escapeHtml(app.statusNote || 'Scheduled Soon')}')" style="padding:0.4rem 0.85rem;font-size:0.78rem">View Call Letter &nearr;</button>`
                        : `<button class="btn-service-action secondary" onclick="showToast('${escapeHtml(app.companyName)}: Application is under review by recruiter team.')" style="padding:0.4rem 0.85rem;font-size:0.78rem">View Details &gt;</button>`
                    }
                </td>
            </tr>
        `;
    }).join('');
}

// ==========================================================================
// 1-Click Apply Action Handler (Single-Entry Central Data Principle)
// ==========================================================================
async function handleOneClickApply(companyId, companyName) {
    if (!currentUser || !currentUser.studentId) return;

    // Check if resume is present in current profile
    if (!currentProfile || !currentProfile.resumes || currentProfile.resumes.length === 0) {
        showToast('Please upload your verified resume in "My Profile" first.');
        setTimeout(() => switchView('profile'), 1200);
        return;
    }

    const confirmApply = confirm(`Confirm 1-Click Application to ${companyName}?\n\nThis will automatically submit your verified academic record (CGPA, Branch) and stored resume '${currentProfile.resumes[0].fileName}'.`);
    if (!confirmApply) return;

    showToast(`Submitting 1-Click application to ${companyName}...`);

    try {
        const response = await fetch(`${API_BASE_URL}/placements/apply`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                studentId: currentUser.studentId,
                companyId: companyId
            })
        });

        const data = await response.json();

        if (response.ok && data.success) {
            showToast(`Success: 1-Click application submitted for ${companyName}!`);
            // Refresh placement data live
            loadPlacementData(currentUser.studentId);
        } else {
            showToast(data.message || 'Application failed.');
        }
    } catch (err) {
        console.error('Error applying to drive:', err);
        showToast('Server connection error. Please try again.');
    }
}

// ==========================================================================
// View / Download Stored Central Resume Quick Actions
// ==========================================================================
function viewCentralResume() {
    if (currentProfile && currentProfile.resumes && currentProfile.resumes.length > 0) {
        const resumeId = currentProfile.resumes[0].id;
        window.open(`${API_BASE_URL}/resumes/${resumeId}/download`, '_blank');
    } else {
        showToast('No resume uploaded. Upload in My Profile tab.');
    }
}

function downloadCentralResume() {
    viewCentralResume();
}

// ==========================================================================
// Generic Electives Module: Dynamic Real-time Seat & Lock Engine
// ==========================================================================
async function loadElectivesData(studentId) {
    if (!studentId) return;

    try {
        const res = await fetch(`${API_BASE_URL}/electives?studentId=${studentId}`);
        if (!res.ok) return;

        const data = await res.json();

        // 1. Check if student has already selected an elective
        if (data.hasSelectedElective && data.enrolledCourse) {
            // IF ALREADY ENROLLED:
            // Render ONLY the single card of their selected course with [ ENROLLED & LOCKED ] badge
            if (electivePoolContainer) electivePoolContainer.style.display = 'block';
            renderElectiveCardSingleLocked(data.enrolledCourse, data.enrolledAt);

            // Render official allotment pass
            if (enrolledElectiveContainer) {
                enrolledElectiveContainer.style.display = 'block';
                renderEnrolledPass(data.enrolledCourse, data.enrolledAt);
            }
        } else {
            // IF NOT ENROLLED:
            // Display the 4 available elective cards with exact live remaining seats and Select Course button
            if (enrolledElectiveContainer) enrolledElectiveContainer.style.display = 'none';
            if (electivePoolContainer) electivePoolContainer.style.display = 'block';
            renderElectivesPool(data.availableCourses || []);
        }
    } catch (err) {
        console.error('Error loading electives data:', err);
    }
}

function renderElectiveCardSingleLocked(c, enrolledAtStr) {
    if (!electiveCardsList) return;

    const total = c.totalSeats || 30;
    const remaining = c.remainingSeats != null ? c.remainingSeats : 0;
    const enrolled = c.enrolledSeats != null ? c.enrolledSeats : (total - remaining);
    const fillPercentage = Math.min(100, Math.round(((total - remaining) / total) * 100));

    let codeFirst = c.courseCode.substring(0, 2);
    let codeSecond = c.courseCode.substring(3);
    if (!c.courseCode.includes('-')) {
        codeFirst = c.courseCode.substring(0, 2);
        codeSecond = c.courseCode.substring(2);
    }

    electiveCardsList.innerHTML = `
        <div class="elective-card-row enrolled-locked-row" style="border:1.5px solid #10b981;background:linear-gradient(to right, #f0fdf4, #ffffff);box-shadow:0 4px 14px rgba(16,185,129,0.12);">
            <!-- Course Code Badge -->
            <div class="course-code-box" style="background:#dcfce7;color:#15803d;border-color:#86efac;">
                <span>${codeFirst}-</span>
                <span>${codeSecond}</span>
            </div>

            <!-- Course Title & Metadata (Clean: No category tags) -->
            <div class="course-info-col">
                <div class="course-title-row">
                    <h3 class="course-title" style="color:#065f46;font-size:1.1rem;font-weight:700;">${escapeHtml(c.courseName)}</h3>
                    <span class="badge-credits">${c.credits || 3} Credits</span>
                </div>
                <div class="course-venue-preview" style="color:#047857;font-weight:600;">
                    <svg width="14" height="14" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"/>
                        <path stroke-linecap="round" stroke-linejoin="round" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"/>
                    </svg>
                    ${escapeHtml(c.venue || 'Main Block')} &bull; ${escapeHtml(c.facultyName || 'Faculty In-Charge')}
                </div>
            </div>

            <!-- Remaining Seats Progress Bar -->
            <div class="seats-progress-col">
                <div class="seats-text-row">
                    <span class="label">Remaining Seats</span>
                    <span class="val" style="color:#059669;font-weight:700;">${remaining} / ${total}</span>
                </div>
                <div class="seats-progress-bar">
                    <div class="seats-progress-fill" style="width: ${fillPercentage}%;background:#10b981;"></div>
                </div>
                <span class="seats-capacity-note">Enrolled: ${enrolled} / ${total} Seats</span>
            </div>

            <!-- Green Pill Badge: [ ENROLLED & LOCKED ] (Select Course button HIDDEN) -->
            <div>
                <span class="badge-enrolled-locked" style="background:#ecfdf5;color:#059669;border:1.5px solid #10b981;padding:0.6rem 1.15rem;border-radius:9999px;font-weight:800;font-size:0.85rem;display:inline-flex;align-items:center;gap:0.45rem;letter-spacing:0.03em;">
                    <svg width="16" height="16" fill="none" stroke="currentColor" stroke-width="2.5" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"/>
                    </svg>
                    ENROLLED &amp; LOCKED
                </span>
            </div>
        </div>
    `;
}

function renderElectivesPool(courses) {
    if (!electiveCardsList) return;

    if (!courses || courses.length === 0) {
        electiveCardsList.innerHTML = `<div class="loading-state-box">No elective courses available at this moment.</div>`;
        return;
    }

    electiveCardsList.innerHTML = courses.map(c => {
        const total = c.totalSeats || 30;
        const remaining = c.remainingSeats != null ? c.remainingSeats : 30;
        const isFull = c.full || remaining <= 0;
        const fillPercentage = Math.min(100, Math.round(((total - remaining) / total) * 100));

        let codeFirst = c.courseCode.substring(0, 2);
        let codeSecond = c.courseCode.substring(3);
        if (!c.courseCode.includes('-')) {
            codeFirst = c.courseCode.substring(0, 2);
            codeSecond = c.courseCode.substring(2);
        }

        let actionBtnHtml = '';
        if (isFull) {
            actionBtnHtml = `
                <button class="btn-seats-full" disabled>
                    <svg width="16" height="16" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z"/>
                    </svg>
                    Seats Full
                </button>
            `;
        } else {
            actionBtnHtml = `
                <button class="btn-select-course" onclick="handleSelectElective(${c.id}, '${escapeHtml(c.courseCode)}', '${escapeHtml(c.courseName)}', '${escapeHtml(c.venue)}')">
                    <svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2.5" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"/>
                    </svg>
                    Select Course
                </button>
            `;
        }

        return `
            <div class="elective-card-row">
                <!-- Course Code Badge -->
                <div class="course-code-box ${isFull ? 'full' : ''}">
                    <span>${codeFirst}-</span>
                    <span>${codeSecond}</span>
                </div>

                <!-- Course Title & Metadata (Clean: No category tags) -->
                <div class="course-info-col">
                    <div class="course-title-row">
                        <h3 class="course-title">${escapeHtml(c.courseName)}</h3>
                        <span class="badge-credits">${c.credits || 3} Credits</span>
                        ${isFull ? '<span class="badge-full-tag">🚫 Seats Full</span>' : ''}
                    </div>
                    <div class="course-venue-preview">
                        <svg width="14" height="14" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"/>
                            <path stroke-linecap="round" stroke-linejoin="round" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"/>
                        </svg>
                        ${escapeHtml(c.venue || 'Main Block')} &bull; ${escapeHtml(c.facultyName || 'Faculty In-Charge')}
                    </div>
                </div>

                <!-- Remaining Seats Progress Bar (Exact: Remaining Seats: X / 30) -->
                <div class="seats-progress-col">
                    <div class="seats-text-row">
                        <span class="label">Remaining Seats</span>
                        <span class="val ${isFull ? 'full' : ''}">${isFull ? '0 / ' + total + ' (FULL)' : remaining + ' / ' + total}</span>
                    </div>
                    <div class="seats-progress-bar">
                        <div class="seats-progress-fill ${isFull ? 'full' : ''}" style="width: ${fillPercentage}%"></div>
                    </div>
                    <span class="seats-capacity-note">Capacity: ${total} Seats</span>
                </div>

                <!-- Action Button -->
                <div>
                    ${actionBtnHtml}
                </div>
            </div>
        `;
    }).join('');
}

function renderEnrolledPass(course, enrolledAtStr) {
    if (!course) return;

    if (allocCourseCode) allocCourseCode.textContent = course.courseCode || 'CS-701';
    if (allocCourseTitle) allocCourseTitle.textContent = course.courseName || 'Deep Learning & Neural Networks';
    if (allocCredits) allocCredits.textContent = `${course.credits || 3} Credits`;
    if (allocFaculty) allocFaculty.textContent = `Faculty: ${course.facultyName || 'Faculty In-Charge'}`;

    // SVVV Campus Classroom Venue
    if (allocVenue) allocVenue.textContent = course.venue || 'Main Block — Seminar Hall #104 (Ground Floor)';
    if (allocVenueSpan) allocVenueSpan.textContent = course.venue || 'Main Block Seminar Hall #104';
    if (allocSchedule) {
        allocSchedule.innerHTML = `
            <svg width="14" height="14" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"/>
            </svg>
            ${escapeHtml(course.schedule || 'Tue, Thu, Sat • 10:00 AM - 11:00 AM')}
        `;
    }

    // Metadata
    if (allocStudentName) allocStudentName.textContent = (currentProfile && currentProfile.name) || (currentUser && currentUser.name) || 'Student';
    if (allocRollNo) allocRollNo.textContent = (currentProfile && currentProfile.enrollmentNo) || (currentUser && currentUser.enrollmentNo) || '--';
    if (allocTimestamp) allocTimestamp.textContent = enrolledAtStr || 'Confirmed';
    if (allocToken) allocToken.textContent = `SVVV-ELECTIVE-${(course.courseCode || 'CS').replace('-', '')}-2026-${Math.floor(1000 + Math.random() * 9000)}`;
}

// ==========================================================================
// Handle Select Elective Submission (Single Choice Permanent Lock)
// ==========================================================================
async function handleSelectElective(courseId, courseCode, courseName, venue) {
    if (!currentUser || !currentUser.studentId) return;

    const confirmSelect = confirm(
        `Confirm Generic Elective Selection:\n\n` +
        `• Course: ${courseCode} - ${courseName}\n` +
        `• Classroom Venue: ${venue || 'Main Block'}\n\n` +
        `Are you sure you want to select this course? Once confirmed, your selection will be permanently locked.`
    );

    if (!confirmSelect) return;

    showToast(`Enrolling in ${courseCode}...`);

    try {
        const response = await fetch(`${API_BASE_URL}/electives/enroll`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                studentId: currentUser.studentId,
                courseId: courseId
            })
        });

        const data = await response.json();

        if (response.ok) {
            showToast(`Success! Enrolled in ${courseCode}`);
            // Instantly update UI to show only the locked course card
            loadElectivesData(currentUser.studentId);
        } else {
            showToast(data.message || 'Enrollment failed.');
        }
    } catch (err) {
        console.error('Error enrolling in elective:', err);
        showToast('Server connection error while enrolling.');
    }
}

// ==========================================================================
// Bonafide Certificate Module: Load Status & Dynamic State Handling
// ==========================================================================
async function loadBonafideData(studentId) {
    if (!studentId) return;

    try {
        let res = await fetch(`${API_BASE_URL}/bonafide/student/${studentId}`);
        if (!res.ok) {
            res = await fetch(`${API_BASE_URL}/bonafide/status?studentId=${studentId}`);
        }
        if (!res.ok) return;

        currentBonafideData = await res.json();
        renderBonafideState(currentBonafideData);
    } catch (err) {
        console.error('Error loading bonafide data:', err);
    }
}

function renderBonafideState(data) {
    if (!data) return;

    // Reset step card classes
    stepCard1.className = 'process-step-card';
    stepCard2.className = 'process-step-card';
    stepCard3.className = 'process-step-card';

    if (data.status === 'APPROVED') {
        // State 3: Approved & Digitally Signed
        bonafideStatusPill.className = 'bonafide-status-pill approved';
        bonafideStatusDot.className = 'status-dot-gray approved';
        bonafideStatusText.textContent = 'Current Status: Approved & Digitally Signed';

        stepCard1.classList.add('completed');
        stepCard2.classList.add('completed');
        stepCard3.classList.add('completed');

        const certLink = data.certificateUrl || data.downloadUrl || '/certificates/bonafide_certificate.pdf';

        bonafideActionContainer.innerHTML = `
            <div class="bonafide-approved-box">
                <div class="approved-info-row">
                    <span class="approved-badge">&#10003; APPROVED &amp; DIGITALLY SIGNED</span>
                </div>
                <div class="approved-actions-row">
                    <a href="${certLink}" target="_blank" download="bonafide_certificate.pdf" class="btn-bonafide-download" style="text-decoration:none;display:inline-flex;align-items:center;gap:0.5rem;">
                        <svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2.5" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4"/>
                        </svg>
                        Download Signed PDF
                    </a>
                    <button class="btn-bonafide-secondary" onclick="openCertificateModal()">
                        Preview Certificate &rarr;
                    </button>
                </div>
            </div>
        `;
    } else if (data.status === 'PENDING') {
        // State 2: Applied & Waiting for Verification (Pending)
        bonafideStatusPill.className = 'bonafide-status-pill pending';
        bonafideStatusDot.className = 'status-dot-gray pending';
        bonafideStatusText.textContent = 'Current Status: Pending Verification';

        stepCard1.classList.add('completed');
        stepCard2.classList.add('active');

        bonafideActionContainer.innerHTML = `
            <div class="bonafide-pending-box">
                <div class="pending-info-row">
                    <span class="ref-badge">Ref: ${escapeHtml(data.referenceNo || 'SVVV-BONA-2026')}</span>
                    <span class="purpose-badge">Remark: ${escapeHtml(data.remarks || 'Pending Verification')}</span>
                </div>
                <div class="pending-status-banner">
                    <span class="pulse-indicator"></span>
                    <span>Application Submitted on <strong>${escapeHtml(data.requestedAt || 'Recently')}</strong> &bull; Status: <strong>${escapeHtml(data.remarks || 'Pending Verification')}</strong></span>
                </div>
                <div class="pending-actions-row" style="display:flex;flex-direction:column;align-items:center;gap:0.5rem;width:100%">
                    <button class="btn-bonafide-primary disabled" disabled style="opacity:0.65;cursor:not-allowed;">
                        ⏳ Pending Verification (Under Review by Registrar)
                    </button>
                </div>
            </div>
        `;
    } else {
        // State 1: Not Applied (Initial Screen)
        bonafideStatusPill.className = 'bonafide-status-pill';
        bonafideStatusDot.className = 'status-dot-gray';
        bonafideStatusText.textContent = 'Current Status: Not Applied';

        stepCard1.classList.add('active');

        bonafideActionContainer.innerHTML = `
            <button class="btn-bonafide-primary" id="btnOpenApplyBona" onclick="openBonafideApplyModal()">
                <svg width="20" height="20" fill="none" stroke="currentColor" stroke-width="2.5" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M12 4v16m8-8H4"/>
                </svg>
                Apply Online for Bonafide Certificate
            </button>
        `;
    }
}

// ==========================================================================
// Bonafide Apply Modal Handlers (Single-Entry Data Reuse)
// ==========================================================================
function openBonafideApplyModal() {
    const studentName = (currentProfile && currentProfile.name) || (currentUser && currentUser.name) || 'Yash Rathore';
    const rollNo = (currentProfile && currentProfile.enrollmentNo) || (currentUser && currentUser.enrollmentNo) || '23100BTCSE15208';
    const branch = (currentProfile && currentProfile.branch) || (currentUser && currentUser.branch) || 'Computer Science & Engineering';

    bonaFormStudentName.textContent = studentName;
    bonaFormRollNo.textContent = rollNo;
    bonaFormBranch.textContent = branch;

    if (bonaInputFatherName) bonaInputFatherName.value = '';

    bonafideApplyModal.classList.add('open');
}

function closeBonafideApplyModal() {
    bonafideApplyModal.classList.remove('open');
}

// Form Submission
bonafideApplyForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    if (!currentUser || !currentUser.studentId) return;

    const fatherName = bonaInputFatherName ? bonaInputFatherName.value.trim() : '';

    if (!fatherName) {
        alert("Please enter Father's Name.");
        return;
    }

    btnSubmitBonaForm.disabled = true;
    btnSubmitBonaForm.textContent = 'Submitting...';

    try {
        const response = await fetch(`${API_BASE_URL}/bonafide/apply`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                studentId: currentUser.studentId,
                fatherName: fatherName,
                purpose: "Official Academic Verification & Record",
                remarks: ""
            })
        });

        if (response.ok) {
            currentBonafideData = await response.json();
            closeBonafideApplyModal();
            showToast('Bonafide Application submitted successfully!');
            renderBonafideState(currentBonafideData);
        } else {
            showToast('Failed to submit application. Please try again.');
        }
    } catch (err) {
        console.error('Error submitting bonafide:', err);
        showToast('Server error while submitting application.');
    } finally {
        btnSubmitBonaForm.disabled = false;
        btnSubmitBonaForm.textContent = 'Submit Application →';
    }
});

// Demo Instant Approval Handler
async function handleInstantApproveDemo(requestId) {
    const id = requestId || (currentBonafideData && currentBonafideData.id);
    if (!id) return;

    showToast('Simulating Registrar Office approval...');

    try {
        const res = await fetch(`${API_BASE_URL}/bonafide/${id}/approve`, {
            method: 'POST'
        });

        if (res.ok) {
            currentBonafideData = await res.json();
            showToast('Success: Bonafide Certificate approved and signed!');
            renderBonafideState(currentBonafideData);
        } else {
            showToast('Could not approve request.');
        }
    } catch (err) {
        console.error('Error approving request:', err);
        showToast('Server error while approving.');
    }
}

// ==========================================================================
// Printable Certificate Modal Handlers
// ==========================================================================
function openCertificateModal() {
    const data = currentBonafideData || {};
    const studentName = data.studentName || (currentProfile && currentProfile.name) || (currentUser && currentUser.name) || 'Yash Rathore';
    const fatherName = data.fatherName || 'Rajesh Rathore';
    const rollNo = data.enrollmentNo || (currentProfile && currentProfile.enrollmentNo) || (currentUser && currentUser.enrollmentNo) || '23100BTCSE15208';
    const branch = data.branch || (currentProfile && currentProfile.branch) || (currentUser && currentUser.branch) || 'Computer Science & Engineering';
    const semRoman = toRoman(data.semester || (currentProfile && currentProfile.semester) || 7);
    const purpose = data.purpose || 'Education Loan Application (Bank Submission)';
    const ref = data.referenceNo || `SVVV/RO/BONA/2026/${Math.floor(1000 + Math.random() * 9000)}`;
    const issueDate = data.approvedAt ? data.approvedAt.split(' at ')[0] : new Date().toLocaleDateString('en-US', { month: 'short', day: '2-digit', year: 'numeric' });

    certRefNo.textContent = ref;
    certIssueDate.textContent = issueDate;
    certStudentName.textContent = studentName;
    certFatherName.textContent = fatherName;
    certRollNo.textContent = rollNo;
    certBranch.textContent = `Bachelor of Technology in ${branch}`;
    certSemester.textContent = semRoman;
    if (certPurpose) certPurpose.textContent = purpose;
    certQrToken.innerHTML = `DIGITALLY VERIFIED<br>${ref}`;

    bonafideCertModal.classList.add('open');
}

function closeCertificateModal() {
    bonafideCertModal.classList.remove('open');
}

function printBonafideCertificate() {
    window.print();
}

// ==========================================================================
// Call Letter & Helpdesk Modal Handlers
// ==========================================================================
function openCallLetterModal(companyName, jobRole, interviewSchedule) {
    modalCandidateName.textContent = (currentProfile && currentProfile.name) || (currentUser && currentUser.name) || 'Yash Prajapati';
    modalEnrollment.textContent = (currentProfile && currentProfile.enrollmentNo) || (currentUser && currentUser.enrollmentNo) || '0827CS211001';
    modalCompanyName.textContent = companyName || 'TCS Digital';
    modalJobRole.textContent = jobRole || 'Systems Engineer';
    modalDateTime.textContent = interviewSchedule || 'Sep 05, 2026 at 10:30 AM IST';
    modalVenue.textContent = 'SVVV Placement Cell, Seminar Hall 2 (In-Person)';
    modalToken.textContent = `SVVV-${(companyName || 'DRV').substring(0, 3).toUpperCase()}-2026-${Math.floor(10000 + Math.random() * 90000)}`;

    callLetterModal.classList.add('open');
}

function closeCallLetterModal() {
    callLetterModal.classList.remove('open');
}

function openHelpdeskModal() {
    helpdeskModal.classList.add('open');
}

function closeHelpdeskModal() {
    helpdeskModal.classList.remove('open');
}

// Close modals when clicking on background backdrop
window.addEventListener('click', (e) => {
    if (e.target === callLetterModal) closeCallLetterModal();
    if (e.target === helpdeskModal) closeHelpdeskModal();
    if (e.target === bonafideApplyModal) closeBonafideApplyModal();
    if (e.target === bonafideCertModal) closeCertificateModal();
});

// ==========================================================================
// Navigation & View Switching
// ==========================================================================
function switchView(viewName) {
    if (window.innerWidth <= 992) {
        const sidebar = document.querySelector(".sidebar");
        const sidebarOverlay = document.getElementById("sidebar-overlay");
        if (sidebar) sidebar.classList.remove("active");
        if (sidebarOverlay) sidebarOverlay.classList.remove("active");
    }
    document.querySelectorAll('.view-section').forEach(el => el.classList.remove('active-view'));
    document.querySelectorAll('.nav-item').forEach(el => el.classList.remove('active'));

    // Update active state on mobile top nav pills
    const targetView = (viewName === 'overview') ? 'dashboard' : (viewName || 'dashboard');
    document.querySelectorAll('.mobile-top-nav-pills .nav-pill').forEach(pill => {
        pill.classList.remove('active');
        const pView = pill.getAttribute('data-view');
        if (pView === targetView || 
           ((targetView === 'placement' || targetView === 'placements') && (pView === 'placement' || pView === 'placements')) ||
           ((targetView === 'electives' || targetView === 'elective') && (pView === 'electives' || pView === 'elective'))) {
            pill.classList.add('active');
        }
    });

    if (viewName === 'hostel') {
        window.location.href = 'hostel.html';
        return;
    } else if (viewName === 'academics') {
        academicsView.classList.add('active-view');
        navAcademics.classList.add('active');
        if (currentProfile) {
            if (academicCgpaVal) academicCgpaVal.textContent = currentProfile.cgpa ? currentProfile.cgpa.toFixed(2) : '7.98';
            if (academicBacklogEnroll) {
                const roll = currentProfile.enrollmentNo || (currentUser && currentUser.enrollmentNo) || '23100BTCSE15208';
                const name = currentProfile.name || (currentUser && currentUser.name) || 'Yash Rathore';
                academicBacklogEnroll.textContent = `${roll} (${name})`;
            }
        }
        const b = (currentProfile && currentProfile.branch) || (currentUser && currentUser.branch) || 'B.Tech CSE';
        const s = (currentProfile && currentProfile.semester) || (currentUser && currentUser.semester) || 7;
        loadAcademicSubjects(b, s);
    } else if (viewName === 'profile') {
        profileView.classList.add('active-view');
        navProfile.classList.add('active');
    } else if (viewName === 'placement' || viewName === 'placements') {
        placementView.classList.add('active-view');
        navPlacement.classList.add('active');
        if (currentUser && currentUser.studentId) {
            loadPlacementData(currentUser.studentId);
        }
    } else if (viewName === 'electives' || viewName === 'elective') {
        electivesView.classList.add('active-view');
        navElectives.classList.add('active');
        if (currentUser && currentUser.studentId) {
            loadElectivesData(currentUser.studentId);
        }
    } else if (viewName === 'bonafide') {
        bonafideView.classList.add('active-view');
        navBonafide.classList.add('active');
        if (currentUser && currentUser.studentId) {
            loadBonafideData(currentUser.studentId);
        }
    } else if (viewName === 'fees') {
        const feesViewElem = document.getElementById('feesView');
        const navFeesElem = document.getElementById('navFees');
        if (feesViewElem) feesViewElem.classList.add('active-view');
        if (navFeesElem) navFeesElem.classList.add('active');
        if (currentUser && currentUser.studentId) {
            loadFeesData(currentUser.studentId);
        }
    } else if (viewName === 'examination') {
        const examViewElem = document.getElementById('examinationView');
        const navExamElem = document.getElementById('navExamination');
        if (examViewElem) examViewElem.classList.add('active-view');
        if (navExamElem) navExamElem.classList.add('active');
        if (currentUser && currentUser.studentId) {
            loadExaminationData(currentUser.studentId);
        }
    } else {
        dashboardView.classList.add('active-view');
        navDashboard.classList.add('active');
    }
}

// Assignments Filter Tab
function filterAssignments(filter, btn) {
    document.querySelectorAll('.assignment-tab-btn').forEach(b => b.classList.remove('active'));
    if (btn) btn.classList.add('active');

    const cards = document.querySelectorAll('#assignmentsListContainer .assignment-item-card');
    cards.forEach(card => {
        const status = card.getAttribute('data-status');
        if (filter === 'ALL' || status === filter) {
            card.style.display = 'flex';
        } else {
            card.style.display = 'none';
        }
    });
}

function downloadSyllabusScheme() {
    showToast('Downloading SVVV B.Tech CSE (Autonomous) 7th Semester Syllabus & Scheme (PDF)...');
}

function showServiceNotice(serviceName, phase) {
    showToast(`${serviceName} module will be connected in ${phase}.`);
}

// ==========================================================================
// Fees & Financial Accounting Module Controller
// ==========================================================================
let currentFeeData = null;
let currentReceiptsList = [];

async function loadFeesData(studentId) {
    if (!studentId) return;

    try {
        const res = await fetch(`${API_BASE_URL}/fees/student/${studentId}`);
        if (res.ok) {
            currentFeeData = await res.json();
            currentReceiptsList = currentFeeData.receipts || [];
            renderFeesUI(currentFeeData);
        } else {
            console.error('Failed to load fee details');
            showToast('Unable to synchronize live fee record from university ledger.');
        }
    } catch (err) {
        console.error('Error fetching fees:', err);
    }
}

function renderFeesUI(data) {
    if (!data) return;

    // Header Subtitle
    const feesHeaderSubtitle = document.getElementById('feesHeaderSubtitle');
    if (feesHeaderSubtitle) {
        feesHeaderSubtitle.textContent = `Academic Year ${data.academicYear || '2026-2027'} • ${data.branch || 'B.Tech Computer Science & Engineering'} (Semester ${data.semesterRoman || 'VII'})`;
    }

    // Header Status Pill
    const feesFinanceStatusPill = document.getElementById('feesFinanceStatusPill');
    const feesFinanceStatusText = document.getElementById('feesFinanceStatusText');
    if (feesFinanceStatusPill && feesFinanceStatusText) {
        feesFinanceStatusText.textContent = data.financeStatusText || 'FINANCE STATUS: ALL DUES CLEARED';
        feesFinanceStatusPill.className = `finance-status-pill ${data.duesBadgeClass || 'green'}`;
    }

    // Top Card 1: Current Semester
    const feesCurrentSemTitle = document.getElementById('feesCurrentSemTitle');
    const feesCurrentSemSub = document.getElementById('feesCurrentSemSub');
    const feesRegCycle = document.getElementById('feesRegCycle');
    if (feesCurrentSemTitle) feesCurrentSemTitle.textContent = data.currentSemesterTitle || `Semester ${data.semesterRoman} (${data.academicYear})`;
    if (feesCurrentSemSub) feesCurrentSemSub.textContent = data.currentSemesterSub || `Academic Term ${data.semesterRoman} • ${data.branch}`;
    if (feesRegCycle) feesRegCycle.textContent = data.registrationCycle || 'Autumn 2026';

    // Top Card 2: Semester Fee Amount
    const feesSemAmountVal = document.getElementById('feesSemAmountVal');
    if (feesSemAmountVal) {
        feesSemAmountVal.textContent = `₹${(data.semesterFeeAmount || 55000).toLocaleString('en-IN')}`;
    }

    // Top Card 3: Current Dues Status
    const feesDuesVal = document.getElementById('feesDuesVal');
    const feesDuesSub = document.getElementById('feesDuesSub');
    const feesDuesTag = document.getElementById('feesDuesTag');
    const feesDuesBadgeText = document.getElementById('feesDuesBadgeText');
    const feesHallTicketStatus = document.getElementById('feesHallTicketStatus');

    if (feesDuesVal) {
        feesDuesVal.textContent = data.duesTitle || '₹0 Dues';
        feesDuesVal.className = `fees-kpi-value ${data.isFeePaid ? 'green-dues' : 'red-dues'}`;
    }
    if (feesDuesSub) feesDuesSub.textContent = data.duesSubtitle || 'No pending dues • Exam Hall Ticket Cleared';
    if (feesDuesTag && feesDuesBadgeText) {
        feesDuesBadgeText.textContent = data.duesBadgeText || 'Semester VII Fee Paid';
        feesDuesTag.className = `fees-pill-tag ${data.duesBadgeClass || 'green'}`;
    }
    if (feesHallTicketStatus) {
        feesHallTicketStatus.textContent = data.hallTicketStatus || 'Cleared for Issue';
        feesHallTicketStatus.className = data.isFeePaid ? 'cleared-green' : 'blocked-red';
    }

    // Left Column: Fee Heads Breakdown
    const feesOptionCurrent = document.getElementById('feesOptionCurrent');
    if (feesOptionCurrent) feesOptionCurrent.textContent = `Semester ${data.semesterRoman || 'VII'} (Current Term)`;

    const feesTuitionVal = document.getElementById('feesTuitionVal');
    const feesExamVal = document.getElementById('feesExamVal');
    const feesLibraryVal = document.getElementById('feesLibraryVal');
    const feesTotalPayableVal = document.getElementById('feesTotalPayableVal');

    if (feesTuitionVal) feesTuitionVal.textContent = `₹${(data.tuitionFee || 50000).toLocaleString('en-IN')}`;
    if (feesExamVal) feesExamVal.textContent = `₹${(data.examFee || 5000).toLocaleString('en-IN')}`;
    if (feesLibraryVal) {
        feesLibraryVal.textContent = (data.libraryResourceFee && data.libraryResourceFee > 0)
            ? `₹${data.libraryResourceFee.toLocaleString('en-IN')}`
            : 'Included (₹0)';
    }
    if (feesTotalPayableVal) feesTotalPayableVal.textContent = `₹${(data.totalPayableAmount || 55000).toLocaleString('en-IN')}`;

    // Right Column: Reconciliation & Payment Action Box
    const feesPaymentRightPanel = document.getElementById('feesPaymentRightPanel');
    if (feesPaymentRightPanel) {
        if (data.isFeePaid) {
            feesPaymentRightPanel.innerHTML = `
                <div class="fee-reconciled-box">
                    <div class="fee-reconciled-icon">
                        <svg width="15" height="15" fill="none" stroke="currentColor" stroke-width="2.5" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"/>
                        </svg>
                    </div>
                    <div>
                        <div class="fee-reconciled-title">${escapeHtml((data.reconciliationTitle || 'Fee Completed').replace(/Reconciliation\s*/gi, ''))}</div>
                        <div class="fee-reconciled-desc">${escapeHtml(data.reconciliationMessage || 'You have already paid the fee for this semester. No pending dues.')}</div>
                    </div>
                </div>

                <div class="fee-guarantee-box">
                    <div class="fee-guarantee-header">
                        <svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z"/>
                        </svg>
                        <span>Institutional Payment Guarantee</span>
                    </div>
                    <button class="btn-fee-disabled-status" disabled>
                        <svg width="15" height="15" fill="none" stroke="currentColor" stroke-width="2.5" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"/>
                        </svg>
                        <span>${escapeHtml(data.paymentButtonText || 'Fee Paid for Sem VII')}</span>
                    </button>
                </div>
            `;
        } else {
            feesPaymentRightPanel.innerHTML = `
                <div class="fee-reconciled-box" style="background:#fffbeb; border-color:#fde68a;">
                    <div class="fee-reconciled-icon" style="background:#f59e0b;">
                        <svg width="15" height="15" fill="none" stroke="currentColor" stroke-width="2.5" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"/>
                        </svg>
                    </div>
                    <div>
                        <div class="fee-reconciled-title" style="color:#b45309;">${escapeHtml(data.reconciliationTitle || 'Pending Term Dues')}</div>
                        <div class="fee-reconciled-desc" style="color:#92400e;">${escapeHtml(data.reconciliationMessage || `Please clear the outstanding amount of ₹${(data.totalPayableAmount || 55000).toLocaleString('en-IN')} before the registration deadline.`)}</div>
                    </div>
                </div>

                <div class="fee-guarantee-box">
                    <div class="fee-guarantee-header">
                        <svg width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M3 10h18M7 15h1m4 0h1m-7 4h12a3 3 0 003-3V8a3 3 0 00-3-3H6a3 3 0 00-3 3v8a3 3 0 003 3z"/>
                        </svg>
                        <span>Secure University Gateway Checkout</span>
                    </div>
                    <button class="btn-fee-pay-action" onclick="handlePayOnline(${data.totalPayableAmount || 55000}, ${data.semester || 7})">
                        <span>Pay Online Now (₹${(data.totalPayableAmount || 55000).toLocaleString('en-IN')}) &rarr;</span>
                    </button>
                </div>
            `;
        }
    }

    // Receipts Table
    renderReceiptsTable(data.receipts || []);
}

function renderReceiptsTable(receipts) {
    const tableBody = document.getElementById('feesReceiptsTableBody');
    const receiptsCount = document.getElementById('receiptsCount');
    const receiptsFooterCount = document.getElementById('feesLedgerFooterCount');

    if (receiptsCount) receiptsCount.textContent = receipts.length;
    if (receiptsFooterCount) {
        const latestSem = receipts.length > 0 ? receipts[0].semester : 'Semester VII';
        const oldestSem = receipts.length > 0 ? receipts[receipts.length - 1].semester : 'Semester IV';
        receiptsFooterCount.innerHTML = `Showing ${receipts.length} of ${receipts.length} reconciled university ledger records (${latestSem} to ${oldestSem})`;
    }

    if (!tableBody) return;

    if (!receipts || receipts.length === 0) {
        tableBody.innerHTML = `
            <tr>
                <td colspan="7" style="text-align:center; padding:2rem; color:var(--text-muted);">
                    No previous fee receipts found for this enrollment record.
                </td>
            </tr>
        `;
        return;
    }

    tableBody.innerHTML = receipts.map((r, idx) => `
        <tr>
            <td style="font-weight:700; color:#0f172a;">${escapeHtml(r.semester || '')}</td>
            <td>
                <span class="receipt-txn-id" onclick="viewReceiptByIndex(${idx})">${escapeHtml(r.transactionId || '')}</span>
            </td>
            <td>${escapeHtml(r.paymentDate || '')}</td>
            <td style="font-weight:700; color:#0f172a;">₹${(r.amountPaid || 0).toLocaleString('en-IN')}</td>
            <td>${escapeHtml(r.paymentMode || 'Online / UPI')}</td>
            <td>
                <span class="receipt-status-badge ${r.status === 'PAID' ? '' : 'failed'}">
                    <span class="status-dot"></span>
                    <span>${escapeHtml(r.status || 'PAID')}</span>
                </span>
            </td>
            <td>
                ${(r.status === 'PAID') ? `
                <button class="btn-download-receipt" onclick="viewReceiptByIndex(${idx})">
                    <svg width="14" height="14" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4"/>
                    </svg>
                    <span>Download Receipt PDF</span>
                </button>
                ` : `
                <button class="btn-download-receipt disabled" disabled style="opacity:0.5;cursor:not-allowed;" title="Receipt unavailable for pending status">
                    <svg width="14" height="14" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"/>
                    </svg>
                    <span>Receipt Pending</span>
                </button>
                `}
            </td>
        </tr>
    `).join('');
}

function viewReceiptByIndex(index) {
    if (!currentReceiptsList || !currentReceiptsList[index]) return;
    const r = currentReceiptsList[index];
    if (r.status !== 'PAID') {
        showToast('Receipt download is active only for paid and reconciled transactions.');
        return;
    }
    openFeeReceiptModal(r);
}

function openFeeReceiptModal(receipt) {
    const modal = document.getElementById('feeReceiptModal');
    if (!modal) return;

    const studentName = (currentFeeData && currentFeeData.studentName) || (currentUser && currentUser.name) || 'Yash Rathore';
    const enrollmentNo = (currentFeeData && currentFeeData.enrollmentNo) || (currentUser && currentUser.enrollmentNo) || '23100BTCSE15208';
    const branch = (currentFeeData && currentFeeData.branch) || 'B.Tech CSE';

    document.getElementById('modalReceiptTxn').textContent = receipt.transactionId || '#REC-2026-8812';
    document.getElementById('modalReceiptStudentName').textContent = studentName;
    document.getElementById('modalReceiptDate').textContent = receipt.paymentDate || 'Aug 12, 2026';
    document.getElementById('modalReceiptEnrollment').textContent = enrollmentNo;
    document.getElementById('modalReceiptMode').textContent = receipt.paymentMode || 'Online / UPI';
    document.getElementById('modalReceiptBranch').textContent = branch;
    document.getElementById('modalReceiptSem').textContent = receipt.semester || 'Semester VII';

    const tuitionAmt = (receipt.amountPaid && receipt.amountPaid >= 50000) ? 50000 : (receipt.amountPaid || 50000);
    const examAmt = (receipt.amountPaid && receipt.amountPaid > 50000) ? (receipt.amountPaid - 50000) : 5000;

    document.getElementById('modalReceiptTuition').textContent = `₹${tuitionAmt.toLocaleString('en-IN')}.00`;
    document.getElementById('modalReceiptExam').textContent = `₹${examAmt.toLocaleString('en-IN')}.00`;
    document.getElementById('modalReceiptTotal').textContent = `₹${(receipt.amountPaid || 55000).toLocaleString('en-IN')}.00`;

    modal.style.display = 'flex';
}

function closeFeeReceiptModal() {
    const modal = document.getElementById('feeReceiptModal');
    if (modal) modal.style.display = 'none';
}

function openFeePolicyModal() {
    const modal = document.getElementById('feePolicyModal');
    if (modal) modal.style.display = 'flex';
}

function closeFeePolicyModal() {
    const modal = document.getElementById('feePolicyModal');
    if (modal) modal.style.display = 'none';
}

function onFeeTermChange(val) {
    showToast(`Displaying ledger and fee heads for ${val === 'current' ? 'Semester VII (Current Term)' : val}`);
}

async function handlePayOnline(amount, sem) {
    if (!currentUser || !currentUser.studentId) {
        showToast('Please log in again to proceed with payment.');
        return;
    }

    const confirmPay = confirm(`Proceed to pay ₹${amount.toLocaleString('en-IN')} for Semester ${sem} through SVVV SBI e-Pay Gateway?`);
    if (!confirmPay) return;

    showToast('Redirecting to University Payment Gateway...');

    try {
        const res = await fetch(`${API_BASE_URL}/fees/pay`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                studentId: currentUser.studentId,
                semester: sem,
                amount: amount,
                paymentMode: 'Online / UPI (SBI e-Pay)'
            })
        });

        if (res.ok) {
            const updatedData = await res.json();
            currentFeeData = updatedData;
            currentReceiptsList = updatedData.receipts || [];
            renderFeesUI(currentFeeData);
            showToast('Payment Successful! Fee ledger reconciled and receipt generated.');
        } else {
            showToast('Payment failed. Please try again.');
        }
    } catch (err) {
        console.error('Payment error:', err);
        showToast('Network error during transaction processing.');
    }
}

// ==========================================================================
// Examination, Results & Hall Ticket Module Controller
// ==========================================================================
let currentExamData = null;

async function loadExaminationData(studentId) {
    if (!studentId) return;

    try {
        const res = await fetch(`${API_BASE_URL}/examination/student/${studentId}`);
        if (res.ok) {
            currentExamData = await res.json();
            renderExaminationUI(currentExamData);
        } else {
            console.error('Failed to load examination details');
            showToast('Unable to synchronize examination records from academic registry.');
        }
    } catch (err) {
        console.error('Error fetching examination data:', err);
    }
}

function renderExaminationUI(data) {
    if (!data) return;

    // Subtitle
    const examHeaderSubtitle = document.getElementById('examHeaderSubtitle');
    if (examHeaderSubtitle) {
        examHeaderSubtitle.textContent = data.sessionTitle || `B.Tech ${data.branch || 'Computer Science & Engineering'} (Semester ${data.currentSemesterRoman || 'VII'}) • University Examination Session 2026–2027`;
    }

    // KPI 1: CGPA
    const examCgpaNumber = document.getElementById('examCgpaNumber');
    const examCgpaSubtext = document.getElementById('examCgpaSubtext');
    const examCgpaBar = document.getElementById('examCgpaBar');

    if (examCgpaNumber) examCgpaNumber.textContent = (data.cgpa || 8.42).toFixed(2);
    if (examCgpaSubtext) {
        examCgpaSubtext.innerHTML = `<span class="dot"></span> <span>${escapeHtml(data.cgpaDifferenceText || '+0.18 vs Sem V • First Class with Distinction Track')}</span>`;
    }
    if (examCgpaBar) examCgpaBar.style.width = `${data.cgpaProgressPercent || 84.2}%`;

    // KPI 2: SGPA Last Sem
    const examSgpaLabel = document.getElementById('examSgpaLabel');
    const examSgpaNumber = document.getElementById('examSgpaNumber');
    const examCreditsSecured = document.getElementById('examCreditsSecured');
    const examCreditsRequired = document.getElementById('examCreditsRequired');
    const examSgpaBar = document.getElementById('examSgpaBar');

    if (examSgpaLabel) examSgpaLabel.innerHTML = `Last Semester SGPA<br>(Semester VI)`;
    if (examSgpaNumber) examSgpaNumber.textContent = (data.sgpaLastSem || 8.50).toFixed(2);
    if (examCreditsSecured) examCreditsSecured.textContent = data.totalCreditsSecured || 24;
    if (examCreditsRequired) examCreditsRequired.textContent = data.totalCreditsRequired || 24;
    if (examSgpaBar) examSgpaBar.style.width = `${data.sgpaProgressPercent || 85.0}%`;

    // KPI 3: Backlogs
    const examBacklogsNumber = document.getElementById('examBacklogsNumber');
    const examBacklogPill = document.getElementById('examBacklogPill');
    const examBacklogPillText = document.getElementById('examBacklogPillText');
    const examDegreeHonorsText = document.getElementById('examDegreeHonorsText');

    if (examBacklogsNumber) examBacklogsNumber.textContent = data.activeBacklogs || 0;
    if (examBacklogPill && examBacklogPillText) {
        examBacklogPillText.textContent = data.backlogStatusTag || 'Clean Record • All Cleared';
        examBacklogPill.className = `exam-pill-tag ${data.backlogTagClass || 'green'}`;
    }
    if (examDegreeHonorsText) examDegreeHonorsText.textContent = data.degreeEligibilityText || 'Eligible for Degree with Honors';

    // Hall Ticket Card
    if (data.hallTicket) {
        const ht = data.hallTicket;
        const examHtSubtitle = document.getElementById('examHtSubtitle');
        const examCenterVal = document.getElementById('examCenterVal');
        const examReportingTimeVal = document.getElementById('examReportingTimeVal');
        const examHtFeeBadge = document.getElementById('examHtFeeBadge');
        const examHtFeeBadgeText = document.getElementById('examHtFeeBadgeText');
        const examHtAttendanceText = document.getElementById('examHtAttendanceText');
        const btnDownloadHallTicket = document.getElementById('btnDownloadHallTicket');

        if (examHtSubtitle) {
            examHtSubtitle.textContent = `Semester ${ht.semesterRoman || 'VII'} End-Semester Theory & Practical Examination • Commencement Date: ${ht.commencementDateFormatted || 'Nov 18, 2026'}`;
        }
        if (examCenterVal) examCenterVal.textContent = (ht.examCenter || 'Block B - SVVV Main Campus').replace(/,\s*Indore/i, '');
        if (examReportingTimeVal) examReportingTimeVal.textContent = ht.reportingTime || '09:30 AM (Morning Shift)';

        if (examHtFeeBadge && examHtFeeBadgeText) {
            examHtFeeBadgeText.textContent = ht.statusBadgeText || 'Fee Dues Cleared';
            examHtFeeBadge.className = `exam-ht-badge ${ht.statusBadgeClass || 'green'}`;
        }
        if (examHtAttendanceText) {
            examHtAttendanceText.textContent = `${ht.attendancePercentage || 82.0}% Attendance • Verified`;
        }

        if (btnDownloadHallTicket) {
            if (ht.isHallTicketEnabled) {
                btnDownloadHallTicket.disabled = false;
                btnDownloadHallTicket.style.opacity = '1';
                btnDownloadHallTicket.style.cursor = 'pointer';
            } else {
                btnDownloadHallTicket.disabled = true;
                btnDownloadHallTicket.style.opacity = '0.6';
                btnDownloadHallTicket.style.cursor = 'not-allowed';
            }
        }
    }

    // Results Table
    renderExamResultsTable(data.semesterResults || []);
}

function renderExamResultsTable(results) {
    const tableBody = document.getElementById('examResultsTableBody');
    const countBadge = document.getElementById('resultsCount');

    if (countBadge) countBadge.textContent = results.length;
    if (!tableBody) return;

    if (!results || results.length === 0) {
        tableBody.innerHTML = `
            <tr>
                <td colspan="6" style="text-align:center; padding:2rem; color:var(--text-muted);">
                    No published examination results available yet.
                </td>
            </tr>
        `;
        return;
    }

    tableBody.innerHTML = results.map(r => `
        <tr>
            <td>
                <div class="sem-title-box">
                    <span class="sem-roman-badge">${escapeHtml(r.semesterRoman || 'VI')}</span>
                    <div>
                        <div class="sem-name-bold">${escapeHtml(r.semesterName || '')}</div>
                        <div class="sem-sub-tag">B.Tech CSE &bull; Regular</div>
                    </div>
                </div>
            </td>
            <td>${escapeHtml(r.sessionPeriod || '')}</td>
            <td style="font-weight:700; color:#0f172a;">${r.enrolledCredits || 24} Credits</td>
            <td style="font-weight:700; color:#0f172a;">${r.backlogsInSem != null ? r.backlogsInSem : 0}</td>
            <td>
                <span class="exam-pass-badge">
                    <span class="status-dot"></span>
                    <span>${escapeHtml(r.status || 'PASS')}</span>
                </span>
            </td>
            <td>
                <button class="btn-download-gradecard" onclick="openGradeCardModal('${escapeHtml(r.semesterName || '')}', ${r.enrolledCredits || 24}, ${r.sgpa || 8.50}, '${escapeHtml(r.status || 'PASS')}')">
                    <svg width="14" height="14" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4"/>
                    </svg>
                    <span>Download Grade Card PDF</span>
                </button>
            </td>
        </tr>
    `).join('');
}

function openHallTicketModal() {
    if (!currentExamData || !currentExamData.hallTicket || !currentExamData.hallTicket.isHallTicketEnabled) {
        showToast('Hall ticket is blocked. Please clear outstanding semester fee dues.');
        return;
    }

    const studentName = currentExamData.studentName || (currentUser && currentUser.name) || 'Yash Rathore';
    const enrollmentNo = currentExamData.enrollmentNo || (currentUser && currentUser.enrollmentNo) || '23100BTCSE15208';
    const branch = currentExamData.branch || 'B.Tech Computer Science & Engineering';
    const ht = currentExamData.hallTicket;

    document.getElementById('htModalName').textContent = studentName;
    document.getElementById('htModalEnroll').textContent = enrollmentNo;
    document.getElementById('htModalBranch').textContent = branch;
    document.getElementById('htModalSem').textContent = `Semester ${ht.semesterRoman || 'VII'} (${ht.sessionName || 'Autumn 2026'})`;
    document.getElementById('htModalCenter').textContent = ht.examCenter || 'Block B - SVVV Main Campus, Indore';
    document.getElementById('htModalShift').textContent = ht.reportingTime || '09:30 AM (Morning Shift)';
    document.getElementById('htModalNumber').textContent = ht.hallTicketNumber || 'HT-2026-AUT-23100BTCSE15208';

    const modal = document.getElementById('hallTicketModal');
    if (modal) modal.style.display = 'flex';
}

function closeHallTicketModal() {
    const modal = document.getElementById('hallTicketModal');
    if (modal) modal.style.display = 'none';
}

function openTimetableModal() {
    const modal = document.getElementById('timetableModal');
    if (modal) modal.style.display = 'flex';
}

function closeTimetableModal() {
    const modal = document.getElementById('timetableModal');
    if (modal) modal.style.display = 'none';
}

function openGradingPolicyModal() {
    const modal = document.getElementById('gradingPolicyModal');
    if (modal) modal.style.display = 'flex';
}

function closeGradingPolicyModal() {
    const modal = document.getElementById('gradingPolicyModal');
    if (modal) modal.style.display = 'none';
}

function openGradeCardModal(semName, credits, sgpa, status) {
    const modal = document.getElementById('gradeCardModal');
    if (!modal) return;

    const studentName = (currentExamData && currentExamData.studentName) || (currentUser && currentUser.name) || 'Yash Rathore';
    const enrollmentNo = (currentExamData && currentExamData.enrollmentNo) || (currentUser && currentUser.enrollmentNo) || '23100BTCSE15208';

    document.getElementById('gradeCardTitle').textContent = `Statement of Grades • ${semName}`;
    document.getElementById('gcStudentName').textContent = studentName;
    document.getElementById('gcEnroll').textContent = enrollmentNo;
    document.getElementById('gcCredits').textContent = `${credits || 24} Credits Secured`;
    document.getElementById('gcStatus').textContent = `${status || 'PASS'} (SGPA: ${(sgpa || 8.50).toFixed(2)})`;

    modal.style.display = 'flex';
}

function closeGradeCardModal() {
    const modal = document.getElementById('gradeCardModal');
    if (modal) modal.style.display = 'none';
}

// Close modals on backdrop click
window.addEventListener('click', (e) => {
    const receiptModal = document.getElementById('feeReceiptModal');
    const policyModal = document.getElementById('feePolicyModal');
    const htModal = document.getElementById('hallTicketModal');
    const ttModal = document.getElementById('timetableModal');
    const gpModal = document.getElementById('gradingPolicyModal');
    const gcModal = document.getElementById('gradeCardModal');

    if (e.target === receiptModal) closeFeeReceiptModal();
    if (e.target === policyModal) closeFeePolicyModal();
    if (e.target === htModal) closeHallTicketModal();
    if (e.target === ttModal) closeTimetableModal();
    if (e.target === gpModal) closeGradingPolicyModal();
    if (e.target === gcModal) closeGradeCardModal();
});

// ==========================================================================
// Academic LMS & Subjects Dynamic Loader
// ==========================================================================
async function loadAcademicSubjects(branch, semester) {
    const b = branch || (currentProfile && currentProfile.branch) || (currentUser && currentUser.branch) || 'B.Tech CSE';
    const s = semester || (currentProfile && currentProfile.semester) || (currentUser && currentUser.semester) || 7;

    try {
        const url = `${API_BASE_URL}/academics/subjects?branch=${encodeURIComponent(b)}&semester=${encodeURIComponent(s)}`;
        const response = await fetch(url);

        if (!response.ok) {
            console.warn(`[Academic] Failed to fetch subjects for ${b} Sem ${s}: ${response.status}`);
            return;
        }

        const subjects = await response.json();

        // 1. Render Academic LMS table if present
        const tableBody = document.getElementById('lmsSubjectsTableBody');
        if (tableBody) {
            renderAcademicSubjects(subjects);
        }

        // 2. Render Branch-wise Attendance List if present
        const attendanceContainer = document.getElementById('subjectAttendanceList');
        if (attendanceContainer) {
            renderAttendanceSubjects(subjects, b, s);
        }
    } catch (err) {
        console.error('Error fetching academic subjects:', err);
    }
}

function renderAttendanceSubjects(subjects, branch, semester) {
    const container = document.getElementById('subjectAttendanceList');
    if (!container) return;

    const bNorm = (branch || '').toLowerCase();
    
    // Target codes as per user requirement:
    // B.Tech CSE: BTCS710, BTCS705N, BTCS706N, BTCS712, BTDSE722
    // BCA: BCA501, BCA502, BCA503, BCA504, BCA505, BCA506
    // B.Sc CS: BSCS501, BSCS502, BSCS503, BSCS504, BSCS505
    // MBA: MBA301, MBA302, MBA303, MBA304, MBA305, MBA306
    let targetCodes = [];
    if (bNorm.includes('bca')) {
        targetCodes = ['BCA501', 'BCA502', 'BCA503', 'BCA504', 'BCA505', 'BCA506'];
    } else if (bNorm.includes('b.sc') || bNorm.includes('bsc')) {
        targetCodes = ['BSCS501', 'BSCS502', 'BSCS503', 'BSCS504', 'BSCS505'];
    } else if (bNorm.includes('mba')) {
        targetCodes = ['MBA301', 'MBA302', 'MBA303', 'MBA304', 'MBA305', 'MBA306'];
    } else {
        targetCodes = ['BTCS710', 'BTCS705N', 'BTCS706N', 'BTCS712', 'BTDSE722'];
    }

    // Filter and order subjects based on targetCodes
    let displaySubjects = [];
    targetCodes.forEach(code => {
        const found = (subjects || []).find(s => s.subjectCode === code || s.subjectCode.startsWith(code));
        if (found) {
            displaySubjects.push(found);
        } else {
            displaySubjects.push({
                subjectCode: code,
                subjectName: getFallbackSubjectName(code),
                credits: 4
            });
        }
    });

    if (displaySubjects.length === 0 && subjects && subjects.length > 0) {
        displaySubjects = subjects.slice(0, 5);
    }

    // Predefined realistic attendance profiles per index
    const statsTemplates = [
        { pct: 85, attended: 34, total: 40, status: 'Safe', badgeClass: 'safe', fillClass: 'green', note: '+10% above minimum', noteColor: '#059669' },
        { pct: 90, attended: 18, total: 20, status: 'Excellent', badgeClass: 'excellent', fillClass: 'emerald', note: 'Full attendance record', noteColor: '#059669' },
        { pct: 80, attended: 12, total: 15, status: 'Safe', badgeClass: 'safe', fillClass: 'green', note: 'Reviews on track', noteColor: '#059669' },
        { pct: 70, attended: 21, total: 30, status: 'Low Attendance Warning', badgeClass: 'warning', fillClass: 'red', note: '5% below threshold', noteColor: '#dc2626', warningMsg: '2 more consecutive classes needed to reach 75% threshold' },
        { pct: 86, attended: 31, total: 36, status: 'Safe', badgeClass: 'safe', fillClass: 'green', note: '+11% above minimum', noteColor: '#059669' },
        { pct: 88, attended: 22, total: 25, status: 'Safe', badgeClass: 'safe', fillClass: 'green', note: 'Practicals completed', noteColor: '#059669' }
    ];

    let totalAttended = 0;
    let totalClasses = 0;
    let totalCredits = 0;

    const cardsHtml = displaySubjects.map((subj, idx) => {
        const stat = statsTemplates[idx % statsTemplates.length];
        const credits = subj.credits || 4;
        totalCredits += credits;
        totalAttended += stat.attended;
        totalClasses += stat.total;

        const warningHtml = stat.warningMsg ? `
            <div class="att-warning-note">
                <svg width="13" height="13" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd" d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z" clip-rule="evenodd"/>
                </svg>
                <span>${stat.warningMsg}</span>
            </div>
        ` : '';

        return `
            <div class="subject-att-card">
                <div class="subject-att-top">
                    <div class="subject-att-name">${escapeHtml(subj.subjectCode)} ${escapeHtml(subj.subjectName)} <span>&bull; ${credits} Credits</span></div>
                    <div class="subject-att-status-group">
                        <span class="subject-att-pct" style="${stat.pct < 75 ? 'color:#dc2626;' : ''}">${stat.pct}%</span>
                        <span class="att-status-badge ${stat.badgeClass}">${stat.status}</span>
                    </div>
                </div>
                <div class="subject-att-track">
                    <div class="subject-att-fill ${stat.fillClass}" style="width: ${stat.pct}%;"></div>
                </div>
                <div class="subject-att-bottom">
                    <span>Attended: ${stat.attended} / ${stat.total} Classes</span>
                    <span style="color:${stat.noteColor};font-weight:600;">${stat.note}</span>
                </div>
                ${warningHtml}
            </div>
        `;
    }).join('');

    container.innerHTML = cardsHtml;

    // Update aggregate attendance and header
    const aggregatePct = totalClasses > 0 ? Math.round((totalAttended / totalClasses) * 100) : 82;
    const aggPercentEl = document.getElementById('aggregateAttendancePercent');
    const aggFillEl = document.getElementById('aggregateAttendanceFill');
    const creditsEl = document.getElementById('academicTotalCreditsVal');
    const semHeaderEl = document.getElementById('academicSubjectAttendanceHeader');

    if (aggPercentEl) aggPercentEl.textContent = `${aggregatePct}%`;
    if (aggFillEl) aggFillEl.style.width = `${aggregatePct}%`;
    if (creditsEl) creditsEl.textContent = totalCredits || 21;

    const romanSem = { 1: 'I', 2: 'II', 3: 'III', 4: 'IV', 5: 'V', 6: 'VI', 7: 'VII', 8: 'VIII' }[semester] || semester || 'VII';
    if (semHeaderEl) semHeaderEl.textContent = `Subject-wise Attendance (Semester ${romanSem})`;
}

function getFallbackSubjectName(code) {
    const names = {
        'BTCS710': 'Deep Learning',
        'BTCS705N': 'Industrial Training',
        'BTCS706N': 'Major Capstone Project',
        'BTCS712': 'Cloud Competency Lab',
        'BTDSE722': 'Prompt Engineering and Fine-Tuning',
        'BCA501': 'Python Programming & Data Science',
        'BCA502': 'Information Security & Cyber Laws',
        'BCA503': 'Mobile Application Development',
        'BCA504': 'Cloud Computing Essentials',
        'BCA505': 'Python & Data Science Lab',
        'BCA506': 'Mobile App Development Lab',
        'BSCS501': 'Advanced Java Programming',
        'BSCS502': 'Computer Networks & Security',
        'BSCS503': 'Web Application Development',
        'BSCS504': 'Software Engineering Principles',
        'BSCS505': 'Advanced Java Lab',
        'MBA301': 'Strategic Management',
        'MBA302': 'Business Analytics & Big Data',
        'MBA303': 'Consumer Behavior & Marketing Research',
        'MBA304': 'Financial Derivatives & Risk Management',
        'MBA305': 'Human Resource Development',
        'MBA306': 'Supply Chain & Logistics Management'
    };
    return names[code] || code;
}

function renderAcademicSubjects(subjects) {
    const tableBody = document.getElementById('lmsSubjectsTableBody');
    if (!tableBody) return;

    if (!subjects || subjects.length === 0) {
        tableBody.innerHTML = `
            <tr>
                <td colspan="5" style="text-align:center; padding:2rem; color:var(--text-muted); font-size:0.88rem;">
                    No academic subjects found for the enrolled semester.
                </td>
            </tr>
        `;
        return;
    }

    tableBody.innerHTML = subjects.map((subj, idx) => {
        const bg = (idx % 2 === 0) ? '#ffffff' : '#eaf4fd';
        const outlineUrl = subj.courseOutlineUrl || '';

        return `
            <tr style="background:${bg};border-bottom:1px solid #f1f5f9;">
                <td style="padding:0.8rem 0.85rem;color:#475569;font-weight:500;text-align:center;">${idx + 1}</td>
                <td style="padding:0.8rem 0.85rem;font-weight:600;color:#334155;">${escapeHtml(subj.subjectCode)}</td>
                <td style="padding:0.8rem 0.85rem;font-weight:600;color:#334155;text-transform:uppercase;">${escapeHtml(subj.subjectName)}</td>
                <td style="padding:0.8rem 0.85rem;">
                    <a href="javascript:void(0)" onclick="openCourseOutline('${escapeHtml(subj.subjectCode)}', '${escapeHtml(outlineUrl)}')" style="color:#0284c7;text-decoration:none;font-weight:600;font-size:0.83rem;">
                        LMS &gt;&gt;
                    </a>
                </td>
                <td style="padding:0.8rem 0.85rem;text-align:center;">
                    <button onclick="openSubjectChat('${escapeHtml(subj.subjectCode)}')" style="background:none;border:none;cursor:pointer;color:#334155;font-weight:600;font-size:0.83rem;display:inline-flex;align-items:center;gap:0.3rem;">
                        <svg width="15" height="15" fill="#475569" viewBox="0 0 24 24"><path d="M20 2H4c-1.1 0-2 .9-2 2v18l4-4h14c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2z"/></svg> Chat
                    </button>
                </td>
            </tr>
        `;
    }).join('');
}

function openCourseOutline(subjectCode, outlineUrl) {
    if (outlineUrl && outlineUrl.trim() !== '') {
        showToast(`Opening LMS Outline for ${subjectCode}...`);
        window.open(outlineUrl, '_blank');
    } else {
        showToast(`Loading LMS Course Outline for ${subjectCode}...`);
    }
}

function openSubjectChat(subjectCode) {
    showToast(`Connecting to Subject Chat Desk: ${subjectCode}`);
}

// ==========================================================================
// Academic Calendar Dynamic Loader
// ==========================================================================
async function loadAcademicCalendar() {
    const monthYearEl = document.getElementById('academicCalendarMonthYear');
    const gridEl = document.getElementById('academicCalendarGrid');
    const eventsListEl = document.getElementById('academicCalendarEventsList');

    try {
        const response = await fetch(`${API_BASE_URL}/academics/calendar`);
        if (!response.ok) {
            console.warn(`[Calendar] Failed to fetch calendar: ${response.status}`);
            return;
        }

        const calData = await response.json();
        renderAcademicCalendar(calData);
    } catch (err) {
        console.error('Error fetching academic calendar:', err);
    }
}

function renderAcademicCalendar(calData) {
    if (!calData) return;

    // 1. Month & Year Title
    const monthYearEl = document.getElementById('academicCalendarMonthYear');
    if (monthYearEl && calData.monthYear) {
        monthYearEl.textContent = calData.monthYear;
    }

    // 2. Calendar Grid
    const gridEl = document.getElementById('academicCalendarGrid');
    if (gridEl) {
        const headers = ['M', 'T', 'W', 'T', 'F', 'S', 'S'];
        let gridHtml = headers.map(h => `<div class="cal-header">${h}</div>`).join('');

        // Map events by day number
        const eventMap = {};
        if (Array.isArray(calData.events)) {
            calData.events.forEach(ev => {
                eventMap[ev.day] = ev;
            });
        }

        // Leading muted days from previous month
        // firstDayOfWeek: 1 (Mon) to 7 (Sun)
        const firstDayOfWeek = calData.firstDayOfWeek || 1;
        const leadingDays = firstDayOfWeek - 1;
        const prevMonthLastDate = new Date(calData.year, calData.month - 1, 0).getDate();

        for (let i = leadingDays; i >= 1; i--) {
            gridHtml += `<div class="cal-day muted">${prevMonthLastDate - i + 1}</div>`;
        }

        // Current month days
        const daysInMonth = calData.daysInMonth || 31;
        const currentDay = calData.currentDay || new Date().getDate();

        for (let d = 1; d <= daysInMonth; d++) {
            const isToday = (d === currentDay);
            const hasEvent = !!eventMap[d];

            let cellClass = 'cal-day';

            if (isToday) {
                cellClass += ' today';
            } else if (hasEvent) {
                const ev = eventMap[d];
                if (ev.type === 'HOLIDAY') {
                    cellClass += ' highlight-event';
                } else {
                    cellClass += ' highlight-active';
                }
            }

            gridHtml += `<div class="${cellClass}">${d}</div>`;
        }

        // Trailing muted days to complete 7-day grid rows
        const totalRendered = leadingDays + daysInMonth;
        const remainder = totalRendered % 7;
        const trailingDays = remainder === 0 ? 0 : 7 - remainder;

        for (let t = 1; t <= trailingDays; t++) {
            gridHtml += `<div class="cal-day muted">${t}</div>`;
        }

        gridEl.innerHTML = gridHtml;
    }

    // 3. Events List
    const eventsListEl = document.getElementById('academicCalendarEventsList');
    if (eventsListEl && Array.isArray(calData.events)) {
        if (calData.events.length === 0) {
            eventsListEl.innerHTML = `<div style="text-align:center;padding:1rem;color:var(--text-muted);font-size:0.8rem;">No events scheduled for this month.</div>`;
        } else {
            eventsListEl.innerHTML = calData.events.map(ev => {
                const dotColor = ev.color || ev.dotColor || 'blue';
                const badgeClass = ev.badgeClass || 'academic';
                const dateText = ev.date || ev.formattedDate || '';
                return `
                    <div class="event-row">
                        <div class="event-name-group">
                            <span class="event-dot ${escapeHtml(dotColor)}"></span>
                            <span>${escapeHtml(dateText)} &bull; ${escapeHtml(ev.title)}</span>
                        </div>
                        <span class="event-type-badge ${escapeHtml(badgeClass)}">${escapeHtml(ev.type)}</span>
                    </div>
                `;
            }).join('');
        }
    }
}

// Global Toast Handler
function showToast(message) {
    const toast = document.getElementById('toastNotification');
    const toastText = document.getElementById('toastText');
    if (!toast || !toastText) {
        console.log('[Toast]', message);
        return;
    }
    toastText.textContent = message;
    toast.style.display = 'flex';
    clearTimeout(window.__toastTimeout);
    window.__toastTimeout = setTimeout(() => {
        toast.style.display = 'none';
    }, 3500);
}

// ==========================================================================
// Dynamic Campus Notices & Announcements Feed
// ==========================================================================
async function loadStudentCampusAnnouncements() {
    const listEl = document.getElementById('studentAnnouncementsFeedList');
    const cardEl = document.getElementById('studentAnnouncementsFeedCard');
    if (!listEl) return;

    const studentBranch = (currentProfile && currentProfile.branch) || (currentUser && currentUser.branch) || '';
    const queryParam = studentBranch ? `?branch=${encodeURIComponent(studentBranch)}` : '';

    try {
        const res = await fetch(`${API_BASE_URL}/announcements${queryParam}`);
        if (res.ok) {
            const data = await res.json();
            if (Array.isArray(data) && data.length > 0) {
                if (cardEl) cardEl.style.display = 'flex';
                listEl.innerHTML = data.map(ann => {
                    const dateStr = ann.createdAt 
                        ? new Date(ann.createdAt).toLocaleDateString('en-IN', { day: '2-digit', month: 'short', hour: '2-digit', minute: '2-digit' })
                        : 'Recently';
                    
                    return `
                        <div class="student-notice-bubble" style="background:#f8fafc;border:1px solid #e2e8f0;border-left:3px solid #2563eb;border-radius:8px;padding:0.75rem 0.9rem;transition:all 0.2s;">
                            <div style="display:flex;align-items:flex-start;justify-content:space-between;gap:0.5rem;margin-bottom:0.25rem;">
                                <div style="font-weight:700;color:#0f172a;font-size:0.86rem;line-height:1.35;">${escapeHtml(ann.title)}</div>
                                <span style="font-size:0.7rem;color:#64748b;white-space:nowrap;">${escapeHtml(dateStr)}</span>
                            </div>
                            <div style="margin-bottom:0.35rem;">
                                <span style="display:inline-block;background:#e0f2fe;color:#0369a1;padding:1px 6px;border-radius:4px;font-size:0.7rem;font-weight:700;">
                                    ${escapeHtml(ann.targetAudience || 'Campus Wide')}
                                </span>
                            </div>
                            <div style="font-size:0.79rem;color:#475569;line-height:1.45;">
                                ${escapeHtml(ann.content)}
                            </div>
                        </div>
                    `;
                }).join('');
                return;
            }
        }
    } catch (err) {
        console.warn('Error fetching student campus announcements:', err);
    }

    listEl.innerHTML = `
        <div style="text-align:center;padding:1.5rem 0.5rem;color:var(--text-muted);font-size:0.82rem;">
            No campus notices currently posted for your branch.
        </div>
    `;
}





