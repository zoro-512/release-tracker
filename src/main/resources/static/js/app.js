/* ================================================================
   Mini Release Tracker — Frontend JavaScript
   Connects to Spring Boot REST API at /api/releases
   ================================================================ */

'use strict';

const API_BASE = '/api/releases';

/* ── State ────────────────────────────────────────────────────── */
let allReleases = [];
let editingId   = null;   // null = create mode, number = edit mode
let deletingId  = null;
let statusId    = null;

/* ── Helpers ──────────────────────────────────────────────────── */

async function apiFetch(path, options = {}) {
  const res = await fetch(API_BASE + path, {
    headers: { 'Content-Type': 'application/json' },
    ...options
  });
  if (res.status === 204) return null;
  const data = await res.json();
  if (!res.ok) throw data;
  return data;
}

function envBadge(env) {
  const map = { DEV: 'badge-dev', QA: 'badge-qa', PROD: 'badge-prod' };
  return `<span class="badge ${map[env] || ''}">${env}</span>`;
}

function statusBadge(s) {
  const map = {
    PENDING:  'badge-pending',
    DEPLOYED: 'badge-deployed',
    FAILED:   'badge-failed',
    ROLLBACK: 'badge-rollback'
  };
  return `<span class="badge ${map[s] || ''}">${s}</span>`;
}

function formatDate(iso) {
  if (!iso) return '—';
  const [y, m, d] = iso.split('-');
  return `${d}/${m}/${y}`;
}

/* ── Toast ────────────────────────────────────────────────────── */
function showToast(message, type = 'info') {
  const icons = { success: '✓', error: '✕', info: 'ℹ' };
  const el = document.createElement('div');
  el.className = `toast ${type}`;
  el.innerHTML = `<span class="toast-icon">${icons[type]}</span><span>${message}</span>`;
  document.getElementById('toast-container').appendChild(el);
  setTimeout(() => el.remove(), 3500);
}

/* ── Navigation ───────────────────────────────────────────────── */
function switchView(name) {
  document.querySelectorAll('.view').forEach(v => v.classList.remove('active'));
  document.querySelectorAll('.nav-item').forEach(n => n.classList.remove('active'));
  document.getElementById(`view-${name}`).classList.add('active');
  document.getElementById(`nav-${name}`).classList.add('active');
  document.getElementById('page-title').textContent =
    name === 'dashboard' ? 'Dashboard' : 'All Releases';
}

document.querySelectorAll('.nav-item').forEach(item => {
  item.addEventListener('click', e => {
    e.preventDefault();
    const view = item.dataset.view;
    switchView(view);
    if (view === 'releases') loadReleases();
    if (view === 'dashboard') loadDashboard();
  });
});

/* ── Dashboard ────────────────────────────────────────────────── */
async function loadDashboard() {
  try {
    const data = await apiFetch('');
    allReleases = data;

    const total    = data.length;
    const deployed = data.filter(r => r.status === 'DEPLOYED').length;
    const pending  = data.filter(r => r.status === 'PENDING').length;
    const failed   = data.filter(r => r.status === 'FAILED' || r.status === 'ROLLBACK').length;

    document.getElementById('stat-total').textContent    = total;
    document.getElementById('stat-deployed').textContent = deployed;
    document.getElementById('stat-pending').textContent  = pending;
    document.getElementById('stat-failed').textContent   = failed;

    const recent = [...data].reverse().slice(0, 8);
    renderDashTable(recent);
  } catch (err) {
    showToast('Failed to load dashboard data', 'error');
  }
}

function renderDashTable(releases) {
  const tbody = document.getElementById('dash-tbody');
  if (!releases.length) {
    tbody.innerHTML = `<tr><td colspan="6" class="empty-row">No releases found</td></tr>`;
    return;
  }
  tbody.innerHTML = releases.map(r => `
    <tr>
      <td class="id-cell">#${r.id}</td>
      <td><span class="project-name">${escHtml(r.projectName)}</span></td>
      <td><span class="version-tag">${escHtml(r.version)}</span></td>
      <td>${envBadge(r.environment)}</td>
      <td>${statusBadge(r.status)}</td>
      <td>${formatDate(r.releaseDate)}</td>
    </tr>
  `).join('');
}

/* ── All Releases ─────────────────────────────────────────────── */
async function loadReleases(params = {}) {
  const tbody = document.getElementById('releases-tbody');
  tbody.innerHTML = `<tr><td colspan="8" class="empty-row">Loading…</td></tr>`;

  try {
    let query = '';
    if (params.project)     query = `?project=${encodeURIComponent(params.project)}`;
    else if (params.status) query = `?status=${params.status}`;
    else if (params.env)    query = `?environment=${params.env}`;

    const data = await apiFetch(query);
    allReleases = data;
    renderReleasesTable(data);
    document.getElementById('record-count').textContent = `${data.length} record${data.length !== 1 ? 's' : ''}`;
  } catch (err) {
    tbody.innerHTML = `<tr><td colspan="8" class="empty-row">Failed to load releases.</td></tr>`;
    showToast('Failed to load releases', 'error');
  }
}

function renderReleasesTable(releases) {
  const tbody = document.getElementById('releases-tbody');
  if (!releases.length) {
    tbody.innerHTML = `<tr><td colspan="8" class="empty-row">No releases found</td></tr>`;
    return;
  }
  tbody.innerHTML = releases.map(r => `
    <tr>
      <td class="id-cell">#${r.id}</td>
      <td><span class="project-name">${escHtml(r.projectName)}</span></td>
      <td><span class="version-tag">${escHtml(r.version)}</span></td>
      <td>${envBadge(r.environment)}</td>
      <td>${statusBadge(r.status)}</td>
      <td>${formatDate(r.releaseDate)}</td>
      <td><span class="desc-cell" title="${escHtml(r.description || '')}">${escHtml(r.description || '—')}</span></td>
      <td>
        <div class="action-group">
          <button class="btn btn-icon btn-edit" data-id="${r.id}" title="Edit">✎</button>
          <button class="btn btn-icon btn-status" data-id="${r.id}" data-status="${r.status}"
                  data-label="${escHtml(r.projectName)} ${escHtml(r.version)}" title="Update Status">⟳</button>
          <button class="btn btn-icon btn-del" data-id="${r.id}"
                  data-label="${escHtml(r.projectName)} ${escHtml(r.version)}" title="Delete">✕</button>
        </div>
      </td>
    </tr>
  `).join('');

  // Bind action buttons
  tbody.querySelectorAll('.btn-edit').forEach(btn =>
    btn.addEventListener('click', () => openEditModal(parseInt(btn.dataset.id))));

  tbody.querySelectorAll('.btn-status').forEach(btn =>
    btn.addEventListener('click', () =>
      openStatusModal(parseInt(btn.dataset.id), btn.dataset.status, btn.dataset.label)));

  tbody.querySelectorAll('.btn-del').forEach(btn =>
    btn.addEventListener('click', () =>
      openDeleteModal(parseInt(btn.dataset.id), btn.dataset.label)));
}

/* ── Filter ───────────────────────────────────────────────────── */
document.getElementById('btn-filter').addEventListener('click', () => {
  const project = document.getElementById('filter-project').value.trim();
  const status  = document.getElementById('filter-status').value;
  const env     = document.getElementById('filter-env').value;
  loadReleases({ project, status, env });
});

document.getElementById('btn-reset').addEventListener('click', () => {
  document.getElementById('filter-project').value = '';
  document.getElementById('filter-status').value  = '';
  document.getElementById('filter-env').value     = '';
  loadReleases();
});

/* ── Create / Edit Modal ──────────────────────────────────────── */
function openCreateModal() {
  editingId = null;
  document.getElementById('modal-title').textContent  = 'New Release';
  document.getElementById('btn-submit').textContent   = 'Create Release';
  document.getElementById('release-form').reset();
  clearFormErrors();
  document.getElementById('modal-overlay').classList.add('open');
}

function openEditModal(id) {
  const release = allReleases.find(r => r.id === id);
  if (!release) return;

  editingId = id;
  document.getElementById('modal-title').textContent  = `Edit Release #${id}`;
  document.getElementById('btn-submit').textContent   = 'Save Changes';

  document.getElementById('form-project').value = release.projectName;
  document.getElementById('form-version').value = release.version;
  document.getElementById('form-env').value     = release.environment;
  document.getElementById('form-date').value    = release.releaseDate;
  document.getElementById('form-status').value  = release.status;
  document.getElementById('form-desc').value    = release.description || '';

  clearFormErrors();
  document.getElementById('modal-overlay').classList.add('open');
}

function closeModal() {
  document.getElementById('modal-overlay').classList.remove('open');
}

document.getElementById('btn-new-release').addEventListener('click', openCreateModal);
document.getElementById('modal-close').addEventListener('click', closeModal);
document.getElementById('btn-cancel').addEventListener('click', closeModal);
document.getElementById('modal-overlay').addEventListener('click', e => {
  if (e.target === document.getElementById('modal-overlay')) closeModal();
});

/* ── Form Submit ──────────────────────────────────────────────── */
document.getElementById('release-form').addEventListener('submit', async e => {
  e.preventDefault();
  if (!validateForm()) return;

  const payload = {
    projectName:  document.getElementById('form-project').value.trim(),
    version:      document.getElementById('form-version').value.trim(),
    environment:  document.getElementById('form-env').value,
    releaseDate:  document.getElementById('form-date').value,
    status:       document.getElementById('form-status').value,
    description:  document.getElementById('form-desc').value.trim() || null
  };

  const btn = document.getElementById('btn-submit');
  btn.disabled = true;
  btn.textContent = 'Saving…';

  try {
    if (editingId) {
      await apiFetch(`/${editingId}`, { method: 'PUT', body: JSON.stringify(payload) });
      showToast('Release updated successfully', 'success');
    } else {
      await apiFetch('', { method: 'POST', body: JSON.stringify(payload) });
      showToast('Release created successfully', 'success');
    }
    closeModal();
    loadDashboard();
    if (document.getElementById('view-releases').classList.contains('active')) loadReleases();
  } catch (err) {
    showToast(err?.message || 'Something went wrong', 'error');
  } finally {
    btn.disabled = false;
    btn.textContent = editingId ? 'Save Changes' : 'Create Release';
  }
});

/* ── Form Validation ──────────────────────────────────────────── */
function validateForm() {
  clearFormErrors();
  let valid = true;

  const project = document.getElementById('form-project').value.trim();
  const version = document.getElementById('form-version').value.trim();
  const env     = document.getElementById('form-env').value;
  const date    = document.getElementById('form-date').value;
  const status  = document.getElementById('form-status').value;

  if (!project) { setError('form-project', 'err-project', 'Project name is required'); valid = false; }
  if (!version) { setError('form-version', 'err-version', 'Version is required');      valid = false; }
  if (!env)     { setError('form-env',     'err-env',     'Environment is required');  valid = false; }
  if (!date)    { setError('form-date',    'err-date',    'Release date is required'); valid = false; }
  if (!status)  { setError('form-status',  'err-status',  'Status is required');       valid = false; }

  return valid;
}

function setError(inputId, errId, msg) {
  document.getElementById(inputId).classList.add('error');
  document.getElementById(errId).textContent = msg;
}

function clearFormErrors() {
  ['form-project','form-version','form-env','form-date','form-status'].forEach(id => {
    document.getElementById(id).classList.remove('error');
  });
  ['err-project','err-version','err-env','err-date','err-status'].forEach(id => {
    document.getElementById(id).textContent = '';
  });
}

/* ── Status Modal ─────────────────────────────────────────────── */
function openStatusModal(id, currentStatus, label) {
  statusId = id;
  document.getElementById('status-release-label').textContent = label;
  document.getElementById('status-select').value = currentStatus;
  document.getElementById('status-modal-overlay').classList.add('open');
}

function closeStatusModal() {
  document.getElementById('status-modal-overlay').classList.remove('open');
}

document.getElementById('status-modal-close').addEventListener('click', closeStatusModal);
document.getElementById('btn-status-cancel').addEventListener('click', closeStatusModal);
document.getElementById('status-modal-overlay').addEventListener('click', e => {
  if (e.target === document.getElementById('status-modal-overlay')) closeStatusModal();
});

document.getElementById('btn-status-save').addEventListener('click', async () => {
  const newStatus = document.getElementById('status-select').value;
  const btn = document.getElementById('btn-status-save');
  btn.disabled = true;
  btn.textContent = 'Saving…';
  try {
    await apiFetch(`/${statusId}/status`, {
      method: 'PATCH',
      body: JSON.stringify({ status: newStatus })
    });
    showToast(`Status updated to ${newStatus}`, 'success');
    closeStatusModal();
    loadReleases();
    loadDashboard();
  } catch (err) {
    showToast(err?.message || 'Failed to update status', 'error');
  } finally {
    btn.disabled = false;
    btn.textContent = 'Save Status';
  }
});

/* ── Delete Modal ─────────────────────────────────────────────── */
function openDeleteModal(id, label) {
  deletingId = id;
  document.getElementById('delete-release-label').textContent = label;
  document.getElementById('delete-modal-overlay').classList.add('open');
}

function closeDeleteModal() {
  document.getElementById('delete-modal-overlay').classList.remove('open');
}

document.getElementById('delete-modal-close').addEventListener('click', closeDeleteModal);
document.getElementById('btn-delete-cancel').addEventListener('click', closeDeleteModal);
document.getElementById('delete-modal-overlay').addEventListener('click', e => {
  if (e.target === document.getElementById('delete-modal-overlay')) closeDeleteModal();
});

document.getElementById('btn-delete-confirm').addEventListener('click', async () => {
  const btn = document.getElementById('btn-delete-confirm');
  btn.disabled = true;
  btn.textContent = 'Deleting…';
  try {
    await apiFetch(`/${deletingId}`, { method: 'DELETE' });
    showToast('Release deleted', 'success');
    closeDeleteModal();
    loadReleases();
    loadDashboard();
  } catch (err) {
    showToast(err?.message || 'Failed to delete release', 'error');
  } finally {
    btn.disabled = false;
    btn.textContent = 'Delete';
  }
});

/* ── Utility ──────────────────────────────────────────────────── */
function escHtml(str) {
  if (!str) return '';
  return str.replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;')
            .replace(/"/g,'&quot;').replace(/'/g,'&#39;');
}

/* ── Boot ─────────────────────────────────────────────────────── */
loadDashboard();
