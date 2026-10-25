/**
 * SVVV ERP — Hostel Registration & Room Selection Script
 * Handles dynamic profile retrieval, interactive auto-fee calculation,
 * REST API application submission, and status banner lifecycle.
 */

const API_BASE_URL = window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1'
    ? 'http://localhost:8080/api'
    : '/api';

// Current session student ID (fallback to 1 if not logged in)
let currentStudentId = 1;
let currentStudentProfile = null;
let currentApplication = null;

// Form state variables
let selectedWing = "Boys Hostel (Campus Main Wing)";
let selectedOccupancy = "2-Sharing Room";
let baseRoomFee = 50000.0;
let selectedClimate = "AC Room";
let climateFeeAddon = 10000.0;

document.addEventListener('DOMContentLoaded', () => {
    initUserSession();
    loadStudentProfile();
    checkApplicationStatus();
    updateFeeBreakdown();
});

/**
 * Helper to update header user info pill badge (Image 4 Alignment)
 */
function updateHeaderUserInfo(data) {
    if (!data) return;
    const name = data.name || data.fullName || data.studentName || 'Yash Rathore';
    const rollNo = data.enrollmentNo || data.rollNo || data.username || '23100BTCSE15208';
    
    const topNameEl = document.getElementById('topStudentName') || document.getElementById('navUserName');
    if (topNameEl) topNameEl.textContent = name;
    
    const topRollEl = document.getElementById('topRollNo');
    if (topRollEl) topRollEl.textContent = `Roll No: ${rollNo}`;
    
    const legacyMetaEl = document.getElementById('navUserMeta');
    if (legacyMetaEl) legacyMetaEl.textContent = `Roll No: ${rollNo} • ${data.branch || 'B.Tech CSE'} Sem VII`;
    
    const avatarEl = document.getElementById('userAvatar') || document.getElementById('navUserAvatar');
    if (avatarEl) {
        const initials = name.split(' ').filter(Boolean).map(n => n[0]).join('').substring(0, 2).toUpperCase() || 'YR';
        avatarEl.textContent = initials;
    }
}

/**
 * Identify student from session/local storage
 */
function initUserSession() {
    try {
        const rawUser = 
            localStorage.getItem('user') || 
            sessionStorage.getItem('user') || 
            localStorage.getItem('loggedInUser') || 
            sessionStorage.getItem('loggedInUser') || 
            localStorage.getItem('currentUser') || 
            sessionStorage.getItem('currentUser') ||
            sessionStorage.getItem('svvv_user');
            
        if (rawUser) {
            const user = JSON.parse(rawUser);
            if (user.studentId) {
                currentStudentId = user.studentId;
            } else if (user.id) {
                currentStudentId = user.id;
            }
            updateHeaderUserInfo(user);
        }
    } catch (e) {
        console.warn('Session parse error, using default student ID', e);
    }
}

/**
 * 1. Auto-Fetch Student Profile from REST API
 */
async function loadStudentProfile() {
    try {
        const res = await fetch(`${API_BASE_URL}/hostel/profile/${currentStudentId}`);
        if (!res.ok) throw new Error('Failed to load profile');
        const data = await res.json();
        currentStudentProfile = data;

        // Populate Student Profile Record Card
        const profName = document.getElementById('profStudentName');
        if (profName) profName.textContent = data.name || 'Yash Rathore';
        
        const profEnroll = document.getElementById('profEnrollmentNo');
        if (profEnroll) profEnroll.textContent = data.enrollmentNo || '23100BTCSE15208';
        
        const profProg = document.getElementById('profProgramTerm');
        if (profProg) profProg.textContent = data.programTerm || 'B.Tech CSE (Semester VII)';
        
        const profContact = document.getElementById('profContactGender');
        if (profContact) profContact.textContent = `${data.phone || '+91 9876543210'} • ${data.gender || 'Male'}`;

        // Top header user badge (Image 4 Alignment)
        updateHeaderUserInfo(data);

        // Auto-select wing based on gender if available
        if (data.gender && data.gender.toLowerCase() === 'female') {
            selectWing('Girls Hostel (Campus West Wing)');
        }
    } catch (err) {
        console.error('Error fetching student profile:', err);
    }
}

/**
 * 2. Check Existing Application Status
 */
async function checkApplicationStatus() {
    try {
        const res = await fetch(`${API_BASE_URL}/hostel/application/${currentStudentId}`);
        if (!res.ok) return;
        const data = await res.json();

        const amberBanner = document.getElementById('statusBannerAmber');
        const greenBanner = document.getElementById('statusBannerGreen');
        const redBanner = document.getElementById('statusBannerRed');
        const submitBtn = document.getElementById('btnSubmitApplication');

        // Hide all banners first
        amberBanner.style.display = 'none';
        greenBanner.style.display = 'none';
        if (redBanner) redBanner.style.display = 'none';

        if (data.hasApplication && data.application) {
            currentApplication = data.application;
            const app = data.application;

            // Reflect applied preferences on UI
            if (app.hostelWing) selectWing(app.hostelWing, false);
            if (app.roomOccupancyType) selectOccupancy(app.roomOccupancyType, app.baseRoomFee, false);
            if (app.climateFacility) selectClimate(app.climateFacility, app.climateFeeAddon, false);

            if (app.status === 'APPLIED') {
                amberBanner.style.display = 'flex';
                submitBtn.disabled = true;
                submitBtn.innerHTML = `
                    <svg fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24" style="width:18px;height:18px;">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"/>
                    </svg>
                    Application Under Review
                `;
                lockFormInputs();
            } else if (app.status === 'APPROVED') {
                greenBanner.style.display = 'flex';
                document.getElementById('greenAllottedRoom').textContent = app.allottedRoomNo || 'Block-A Room 302';
                document.getElementById('greenWardenInfo').textContent = `Verified by: ${app.wardenName || 'Mr. S. K. Sharma (Chief Warden)'}`;
                submitBtn.disabled = true;
                submitBtn.innerHTML = `
                    <svg fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24" style="width:18px;height:18px;">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"/>
                    </svg>
                    Room Allotted — Completed
                `;
                lockFormInputs();
            } else if (app.status === 'REJECTED') {
                if (redBanner) redBanner.style.display = 'flex';
                submitBtn.disabled = false;
                submitBtn.innerHTML = `<span>Re-submit Hostel Application</span>`;
            }
        }
    } catch (err) {
        console.error('Error fetching application status:', err);
    }
}

/**
 * Lock form selection when already submitted/approved
 */
function lockFormInputs() {
    const cards = document.querySelectorAll('.selection-card, .occupancy-card, .climate-card');
    cards.forEach(c => {
        c.style.pointerEvents = 'none';
        c.style.opacity = '0.85';
    });
    const chk = document.getElementById('declarationCheckbox');
    if (chk) {
        chk.checked = true;
        chk.disabled = true;
    }
}

/**
 * Step 1: Select Hostel Wing
 */
function selectWing(wingName, isUserClick = true) {
    selectedWing = wingName;
    const wingBoys = document.getElementById('wingBoys');
    const wingGirls = document.getElementById('wingGirls');

    if (wingName.includes('Boys')) {
        wingBoys.classList.add('selected');
        wingGirls.classList.remove('selected');
    } else {
        wingGirls.classList.add('selected');
        wingBoys.classList.remove('selected');
    }

    if (isUserClick) {
        showToast(`Selected: ${wingName}`);
    }
}

/**
 * Step 2: Select Room Occupancy
 */
function selectOccupancy(type, fee, isUserClick = true) {
    selectedOccupancy = type;
    baseRoomFee = fee;

    const cards = document.querySelectorAll('.occupancy-card');
    cards.forEach(card => {
        card.classList.remove('selected');
        const actionBtn = card.querySelector('.occ-action-text');
        if (actionBtn) {
            actionBtn.innerHTML = 'Select Option';
        }
    });

    let targetCard = null;
    if (type.includes('Single')) targetCard = document.getElementById('occSingle');
    else if (type.includes('2-Sharing')) targetCard = document.getElementById('occDouble');
    else if (type.includes('3-Sharing')) targetCard = document.getElementById('occTriple');

    if (targetCard) {
        targetCard.classList.add('selected');
        const actionBtn = targetCard.querySelector('.occ-action-text');
        if (actionBtn) {
            actionBtn.innerHTML = `
                <svg fill="none" stroke="currentColor" stroke-width="2.5" viewBox="0 0 24 24" style="width:14px;height:14px;display:inline-block;vertical-align:-2px;">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7"/>
                </svg> Selected
            `;
        }
    }

    updateFeeBreakdown();
    if (isUserClick) {
        showToast(`Selected: ${type} (₹${fee.toLocaleString('en-IN')})`);
    }
}

/**
 * Step 3: Select Climate Facility
 */
function selectClimate(climateType, addonFee, isUserClick = true) {
    selectedClimate = climateType;
    climateFeeAddon = addonFee;

    const acCard = document.getElementById('climateAC');
    const nonAcCard = document.getElementById('climateNonAC');

    if (climateType.includes('AC Room') || climateType.includes('AC')) {
        acCard.classList.add('selected');
        nonAcCard.classList.remove('selected');
    } else {
        nonAcCard.classList.add('selected');
        acCard.classList.remove('selected');
    }

    updateFeeBreakdown();
    if (isUserClick) {
        showToast(`Selected: ${climateType} (+₹${addonFee.toLocaleString('en-IN')})`);
    }
}

/**
 * Dynamic Fee Breakdown Auto-Calculation
 */
function updateFeeBreakdown() {
    const baseBox = document.getElementById('breakdownBaseRoom');
    const climateBox = document.getElementById('breakdownClimate');
    const totalBox = document.getElementById('breakdownTotalPayable');

    if (baseBox) {
        baseBox.textContent = `${selectedOccupancy}: ₹${baseRoomFee.toLocaleString('en-IN')}`;
    }
    if (climateBox) {
        const label = selectedClimate.includes('AC Room') || selectedClimate.includes('AC') ? 'AC Facility' : 'Non-AC';
        climateBox.textContent = `${label}: ₹${climateFeeAddon.toLocaleString('en-IN')}`;
    }

    const total = baseRoomFee + climateFeeAddon;
    if (totalBox) {
        totalBox.innerHTML = `₹${total.toLocaleString('en-IN')} <span>/ Semester</span>`;
    }
}

/**
 * Submit Hostel Application via REST API
 */
async function submitHostelApplication() {
    const declaration = document.getElementById('declarationCheckbox');
    if (!declaration || !declaration.checked) {
        showToast('Please check the declaration agreement to proceed.', true);
        return;
    }

    const submitBtn = document.getElementById('btnSubmitApplication');
    const originalText = submitBtn.innerHTML;
    submitBtn.disabled = true;
    submitBtn.innerHTML = `<span>Submitting Application...</span>`;

    const payload = {
        studentId: currentStudentId,
        hostelWing: selectedWing,
        roomOccupancyType: selectedOccupancy,
        baseRoomFee: baseRoomFee,
        climateFacility: selectedClimate,
        climateFeeAddon: climateFeeAddon,
        totalPayableFee: baseRoomFee + climateFeeAddon,
        guardianContact: currentStudentProfile ? currentStudentProfile.phone : '+91 9876543210'
    };

    try {
        const res = await fetch(`${API_BASE_URL}/hostel/apply`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        const data = await res.json();
        if (res.ok && data.success) {
            showToast('Hostel Application Submitted Successfully!');
            await checkApplicationStatus();
        } else {
            showToast(data.message || 'Submission failed. Please try again.', true);
            submitBtn.disabled = false;
            submitBtn.innerHTML = originalText;
        }
    } catch (err) {
        console.error('Error submitting application:', err);
        showToast('Network error while submitting. Please check connection.', true);
        submitBtn.disabled = false;
        submitBtn.innerHTML = originalText;
    }
}

/**
 * Download Official Allotment Slip (PDF format window)
 */
function downloadAllotmentSlip() {
    const app = currentApplication || {
        allottedRoomNo: 'Block-A Room 302',
        wardenName: 'Mr. S. K. Sharma (Chief Warden)',
        hostelWing: selectedWing,
        roomOccupancyType: selectedOccupancy,
        totalPayableFee: baseRoomFee + climateFeeAddon,
        applicationDate: new Date().toISOString()
    };

    const sName = currentStudentProfile ? currentStudentProfile.name : 'Yash Rathore';
    const sRoll = currentStudentProfile ? currentStudentProfile.enrollmentNo : '23100BTCSE15208';
    const sBranch = currentStudentProfile ? currentStudentProfile.branch : 'Computer Science & Engineering';

    const slipWindow = window.open('', '_blank');
    slipWindow.document.write(`
        <!DOCTYPE html>
        <html>
        <head>
            <title>Hostel Allotment Slip — ${sRoll}</title>
            <style>
                body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; padding: 40px; color: #1e293b; background: #fff; }
                .slip-card { max-width: 720px; margin: 0 auto; border: 2px solid #2563eb; border-radius: 12px; padding: 32px; box-shadow: 0 10px 25px rgba(0,0,0,0.05); }
                .header { text-align: center; border-bottom: 2px solid #e2e8f0; padding-bottom: 20px; margin-bottom: 24px; }
                .univ-title { font-size: 20px; font-weight: 800; color: #1e3a8a; }
                .doc-title { font-size: 16px; font-weight: 700; color: #2563eb; margin-top: 6px; letter-spacing: 1px; }
                .meta-table { width: 100%; border-collapse: collapse; margin-bottom: 24px; }
                .meta-table td { padding: 10px 14px; border: 1px solid #e2e8f0; font-size: 14px; }
                .meta-table td.label { font-weight: 700; background: #f8fafc; color: #475569; width: 35%; }
                .highlight-room { background: #dcfce7; color: #166534; font-weight: 800; font-size: 16px; }
                .stamp-box { display: flex; justify-content: space-between; margin-top: 40px; padding-top: 20px; border-top: 1px dashed #cbd5e1; }
                .stamp { text-align: center; }
                .stamp-line { width: 180px; border-bottom: 1px solid #000; margin-bottom: 6px; height: 40px; }
                .print-btn { background: #2563eb; color: #fff; border: none; padding: 10px 20px; border-radius: 6px; font-weight: 600; cursor: pointer; margin-top: 20px; }
                @media print { .print-btn { display: none; } }
            </style>
        </head>
        <body>
            <div class="slip-card">
                <div class="header">
                    <div class="univ-title">SHRI VAISHNAV VIDYAPEETH VISHWAVIDYALAYA, INDORE</div>
                    <div class="doc-title">OFFICIAL HOSTEL RESIDENCE ALLOTMENT SLIP</div>
                    <div style="font-size: 12px; color: #64748b; margin-top: 4px;">Academic Session 2026-2027 • Regular Allocation</div>
                </div>
                <table class="meta-table">
                    <tr><td class="label">Student Name</td><td><strong>${sName}</strong></td></tr>
                    <tr><td class="label">Enrollment / Roll No</td><td>${sRoll}</td></tr>
                    <tr><td class="label">Academic Branch</td><td>${sBranch}</td></tr>
                    <tr><td class="label">Hostel Wing</td><td>${app.hostelWing || selectedWing}</td></tr>
                    <tr><td class="label">Allotted Room No</td><td class="highlight-room">${app.allottedRoomNo || 'Block-A Room 302'}</td></tr>
                    <tr><td class="label">Occupancy Type</td><td>${app.roomOccupancyType || selectedOccupancy}</td></tr>
                    <tr><td class="label">Climate Facility</td><td>${app.climateFacility || selectedClimate}</td></tr>
                    <tr><td class="label">Semester Fee Payable</td><td>₹${(app.totalPayableFee || (baseRoomFee + climateFeeAddon)).toLocaleString('en-IN')}</td></tr>
                    <tr><td class="label">Authorized Warden</td><td>${app.wardenName || 'Mr. S. K. Sharma (Chief Warden)'}</td></tr>
                </table>
                <div style="font-size: 12px; color: #64748b; line-height: 1.5;">
                    * This allotment is valid subject to fee clearance and student verification at the Hostel Administrative Office. Gate closure curfew is strictly 10:00 PM.
                </div>
                <div class="stamp-box">
                    <div class="stamp"><div class="stamp-line"></div>Student Signature</div>
                    <div class="stamp"><div class="stamp-line"></div>Hostel Warden / Dean Seal</div>
                </div>
                <center><button class="print-btn" onclick="window.print()">Print / Save as PDF</button></center>
            </div>
        </body>
        </html>
    `);
    slipWindow.document.close();
}

/**
 * Download Hostel Ordinances Policy
 */
function downloadOrdinances() {
    showToast('Downloading SVVV Hostel Ordinances & Code of Conduct (PDF)...');
    setTimeout(() => {
        window.open('assets/hostel_ordinances_svvv.pdf', '_blank');
    }, 500);
}

/**
 * Toast feedback helper
 */
function showToast(message, isError = false) {
    const toast = document.getElementById('toastNotice');
    if (!toast) return;
    toast.textContent = message;
    toast.style.background = isError ? '#991b1b' : '#0f172a';
    toast.style.display = 'block';

    setTimeout(() => {
        toast.style.display = 'none';
    }, 3500);
}

// Global Logout Handler
function handleLogout() {
    localStorage.removeItem('user');
    localStorage.removeItem('loggedInUser');
    localStorage.removeItem('currentUser');
    localStorage.removeItem('svvv_user');
    sessionStorage.clear();
    window.location.href = 'login.html';
}
window.handleLogout = handleLogout;

document.getElementById('logoutBtn')?.addEventListener('click', (e) => {
    e.preventDefault();
    handleLogout();
});
