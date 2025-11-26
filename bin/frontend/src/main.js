import './style.css';

const API_URL = 'http://localhost:8080/api';
let currentUser = null;
let newSurveyQuestions = [];
let isEditing = false;
let editingSurveyId = null;
let allSurveys = [];

// ==========================================
// 1. RENDERIZADO PRINCIPAL (HTML)
// ==========================================
function renderApp() {
    const app = document.getElementById('app');

    const customStyles = `
    <style>
        .input-glass { width: 100%; padding: 15px; background: rgba(0, 0, 0, 0.3); border: 1px solid rgba(255, 255, 255, 0.1); border-radius: 12px; color: white; font-size: 1rem; transition: all 0.3s ease; outline: none; }
        .input-glass:hover { background: rgba(255, 255, 255, 0.1); border-color: rgba(255, 255, 255, 0.5); transform: translateY(-2px); }
        .input-glass:focus { border-color: #00b894; background: rgba(0, 0, 0, 0.5); }
        .input-glass option { background: #2d3436; color: white; } 
        .label-hd { display: block; margin-bottom: 10px; color: #ffffff; font-weight: 800; font-size: 1.1rem; }
        .survey-row { background: linear-gradient(90deg, rgba(255,255,255,0.1) 0%, rgba(255,255,255,0.05) 100%); backdrop-filter: blur(10px); border: 1px solid rgba(255,255,255,0.1); transition: all 0.3s ease; }
        .survey-row:hover { background: rgba(255,255,255,0.15); transform: translateX(5px); border-color: rgba(255,255,255,0.3); }
        .wide-card { width: 600px !important; max-width: 90vw !important; min-width: 350px; }
        .tabs-container { display: flex; gap: 20px; margin-bottom: 20px; border-bottom: 1px solid rgba(255,255,255,0.1); padding-bottom: 10px; align-items: center; justify-content: space-between; flex-wrap: wrap; }
        .tab-btn { background: transparent; border: none; color: #aaa; font-size: 1.1rem; font-weight: bold; cursor: pointer; padding: 10px 20px; transition: all 0.3s; }
        .tab-btn.active { color: white; border-bottom: 3px solid #6c5ce7; }
        .search-box { background: rgba(0,0,0,0.3); border: 1px solid rgba(255,255,255,0.1); padding: 10px 15px; border-radius: 20px; color: white; width: 250px; outline: none; transition: width 0.3s; }
        .search-box:focus { width: 300px; border-color: #6c5ce7; }
    </style>
  `;

    app.innerHTML = customStyles + `
    <section id="auth-section" style="min-height: 100vh; width: 100%; display: flex; flex-direction: column; align-items: center; justify-content: center;">
      <div id="login-container" class="auth-container">
        <div class="auth-card wide-card" style="padding: 3rem; background: rgba(40, 40, 60, 0.85); backdrop-filter: blur(20px); border-radius: 24px; border: 1px solid rgba(255,255,255,0.1);">
            <div style="text-align:center; margin-bottom: 40px;">
                <h1 style="color:white; font-size: 3.5rem; margin: 0; font-weight: 800;">SurveyCraft</h1>
                <p class="subtitle" style="color: #a0a0ff; margin-top: 10px; font-size: 1.2rem;">Plataforma de Encuestas Gaming</p>
            </div>
          <form id="login-form">
            <div class="form-group" style="margin-bottom: 25px;"><label class="label-hd">📧 Email</label><input type="email" id="login-email" required class="input-glass"></div>
            <div class="form-group" style="margin-bottom: 30px;"><label class="label-hd">🔒 Contraseña</label><input type="password" id="login-password" required class="input-glass"></div>
            <button type="submit" class="btn btn-primary btn-block" style="background: linear-gradient(45deg, #6c5ce7, #a29bfe); width: 100%; padding: 15px; border:none; border-radius:12px; color:white; font-weight:bold; cursor:pointer;">Iniciar Sesión</button>
          </form>
          <p class="auth-link" style="text-align: center; margin-top: 30px; color: #ccc;">¿No tienes cuenta? <a href="#" id="show-register-link" style="color: #a29bfe;">Regístrate aquí</a></p>
        </div>
      </div>

      <div id="register-container" class="auth-container hidden">
        <div class="auth-card wide-card" style="padding: 3rem; background: rgba(40, 40, 60, 0.85); backdrop-filter: blur(20px); border-radius: 24px; border: 1px solid rgba(255,255,255,0.1);">
          <h1 style="color:white; text-align: center; margin-bottom: 40px;">Crear Cuenta</h1>
          <form id="register-form">
            <div class="form-group" style="margin-bottom: 20px;"><label class="label-hd">👤 Nombre</label><input type="text" id="register-name" required class="input-glass"></div>
            <div class="form-group" style="margin-bottom: 20px;"><label class="label-hd">📧 Email</label><input type="email" id="register-email" required class="input-glass"></div>
            <div class="form-group" style="margin-bottom: 20px;"><label class="label-hd">🔒 Contraseña</label><input type="password" id="register-password" required class="input-glass"></div>
            <div class="form-group" style="margin-bottom: 30px;">
                <label class="label-hd">🎭 Rol</label>
                <select id="register-role" required class="input-glass">
                    <option value="PLAYER">🎮 Jugador</option>
                    <option value="CRAFTER">🔨 Creador</option>
                </select>
            </div>
            <button type="submit" class="btn btn-primary btn-block" style="background: linear-gradient(45deg, #00b894, #55efc4); width:100%; padding:15px; border:none; border-radius:12px; color:#2d3436; font-weight:bold; cursor:pointer;">Registrarse</button>
          </form>
          <p class="auth-link" style="text-align: center; margin-top: 30px; color: #ccc;">¿Ya tienes cuenta? <a href="#" id="show-login-link" style="color: #55efc4;">Inicia Sesión</a></p>
        </div>
      </div>
    </section>

    <section id="dashboard-section" class="hidden">
      <nav class="navbar" style="background: rgba(0, 0, 0, 0.2); backdrop-filter: blur(10px); padding: 1rem 2rem; display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid rgba(255,255,255,0.1); position: sticky; top: 0; z-index: 100;">
        <div class="navbar-brand"><h2 style="margin:0; font-weight: 800; font-size: 1.5rem; color: white;">SurveyCraft</h2></div>
        <div class="navbar-user" style="display: flex; gap: 20px; align-items: center;">
          <button id="create-btn" onclick="window.showCreateScreen()" style="display:none; background: linear-gradient(45deg, #6c5ce7, #a29bfe); color: white; padding: 8px 20px; border-radius: 20px; font-weight: bold; border: none; cursor:pointer;">➕ Nueva Encuesta</button>
          <span style="font-weight: 500; color: #ddd;">Hola, <span id="user-name" style="font-weight: bold; color: white;">Usuario</span></span>
          <button id="logout-btn" class="btn" style="background: rgba(255, 71, 87, 0.2); color: #ff4757; padding: 5px 15px; border: 1px solid #ff4757; border-radius: 20px; cursor: pointer;">Salir</button>
        </div>
      </nav>

      <main class="container" style="max-width: 1200px; margin: 0 auto; padding: 3rem 2rem;">
        
        <div class="tabs-container">
            <div id="crafter-tabs" style="display:none;">
                <button id="tab-all" class="tab-btn active" onclick="window.switchTab('all')">🌎 Explorar</button>
                <button id="tab-mine" class="tab-btn" onclick="window.switchTab('mine')">📂 Mis Encuestas</button>
            </div>
            
            <div id="search-container" style="display: flex; align-items: center; gap: 10px;">
                <input type="text" id="search-input" placeholder="🔍 Buscar por juego o título..." class="search-box">
            </div>
        </div>

        <h2 id="dashboard-title" style="font-size: 2rem; margin-bottom: 2rem; font-weight: 700; color: white;">Encuestas Disponibles</h2>
        
        <div id="surveys-container" class="surveys-grid" style="display: flex; flex-direction: column; gap: 20px;">
          <p class="loading" style="color:white;">Cargando encuestas...</p>
        </div>
      </main>
    </section>

    <section id="create-section" class="hidden container" style="padding: 3rem 1rem; min-height: 90vh; display: flex; justify-content: center;">
        <div class="glass-card" style="width: 100%; max-width: 900px; background: rgba(30, 30, 40, 0.9); backdrop-filter: blur(20px); border-radius: 24px; padding: 3rem; color: white;">
            <div style="text-align: center; margin-bottom: 40px;">
                <h2 id="form-title" style="font-size: 2.5rem; font-weight: 800; margin: 0;">📝 Nueva Encuesta</h2>
            </div>
            <form id="create-survey-form">
                <div style="margin-bottom: 25px;">
                    <label class="label-hd">Título</label>
                    <input type="text" id="new-survey-title" required class="input-glass" placeholder="Ej: Opiniones sobre el nuevo parche">
                </div>
                
                <div style="margin-bottom: 25px;">
                    <label class="label-hd">🎮 Videojuego / Categoría</label>
                    <div style="display: flex; gap: 10px;">
                        <select id="new-survey-game" class="input-glass" required>
                            <option value="">Cargando juegos...</option>
                        </select>
                        <button type="button" onclick="window.createNewGameQuick()" style="background: #00b894; color: white; border: none; padding: 0 20px; border-radius: 12px; font-weight: bold; cursor: pointer; font-size: 1.2rem;" title="Crear Nuevo Juego">➕</button>
                    </div>
                </div>

                <div style="margin-bottom: 25px;">
                    <label class="label-hd">Descripción</label>
                    <textarea id="new-survey-desc" required rows="3" class="input-glass" placeholder="Describe brevemente de qué trata..."></textarea>
                </div>
                <hr style="border:0; height: 1px; background: rgba(255,255,255,0.2); margin: 40px 0;">
                <h3 style="color:#55efc4; margin-bottom:20px;">Preguntas</h3>
                <div id="questions-list" style="margin-bottom: 25px; display: flex; flex-direction: column; gap: 10px;"></div>
                
                <div style="background: rgba(0,0,0,0.2); padding: 25px; border-radius: 16px;">
                    <div style="display:flex; gap:15px; margin-bottom:15px;">
                        <div style="flex: 2;">
                            <label style="color: #ccc;">Pregunta</label>
                            <input type="text" id="q-text" class="input-glass">
                        </div>
                        <div style="flex: 2;">
                            <label style="color: #ccc;">Opciones (separadas por coma)</label>
                            <input type="text" id="q-options" placeholder="Ej: Sí, No" class="input-glass">
                        </div>
                    </div>
                    <button type="button" onclick="window.addQuestionToLocal()" style="background: #6c5ce7; color: white; border: none; padding: 10px; width: 100%; border-radius: 8px; cursor: pointer;">➕ Agregar pregunta</button>
                </div>

                <div style="margin-top: 40px; display: flex; gap: 15px; justify-content: flex-end;">
                    <button type="button" onclick="window.showDashboard()" style="background: transparent; border: 1px solid #ff7675; color: #ff7675; padding: 12px 25px; border-radius: 10px; cursor: pointer;">Cancelar</button>
                    <button type="submit" id="save-btn" style="background: #00b894; color: white; border: none; padding: 12px 40px; border-radius: 10px; cursor: pointer; font-weight: bold;">💾 Publicar</button>
                </div>
            </form>
        </div>
    </section>

    <section id="respond-section" class="hidden" style="position: fixed; top:0; left:0; width:100%; height:100%; background: rgba(0,0,0,0.85); backdrop-filter: blur(5px); display: flex; justify-content: center; align-items: center; z-index: 99;">
        <div class="auth-card" style="width: 100%; max-width: 600px; background: #2d3436; padding: 30px; border-radius: 20px; border: 1px solid #444;">
            <h2 id="respond-title" style="color:white; text-align: center; margin-bottom: 20px;">Responder</h2>
            <form id="respond-form">
                <div style="display:grid; grid-template-columns: 1fr 1fr; gap:15px; margin-bottom:25px;">
                    <div>
                        <label style="color:#ccc;">Tiempo Jugado</label>
                        <select id="time-played" class="input-glass" style="background:rgba(0,0,0,0.5);"><option>Menos de 10h</option><option>+10h</option></select>
                    </div>
                    <div>
                        <label style="color:#ccc;">Plataforma</label>
                        <select id="platform-used" class="input-glass" style="background:rgba(0,0,0,0.5);"><option>PC</option><option>Consola</option><option>Móvil</option></select>
                    </div>
                </div>
                <div id="respond-questions-container" style="display:flex; flex-direction:column; gap:15px;"></div>
                <div style="margin-top:30px; display:flex; gap:15px;">
                    <button type="button" onclick="window.showDashboard()" style="flex:1; background:transparent; border:1px solid #ff7675; color:#ff7675; border-radius:8px; cursor:pointer;">Cancelar</button>
                    <button type="submit" style="flex:2; background:#00b894; border:none; color:white; border-radius:8px; cursor:pointer;">Enviar</button>
                </div>
            </form>
        </div>
    </section>

    <section id="results-section" class="hidden container" style="padding: 3rem 2rem; min-height: 100vh;">
        <button onclick="window.showDashboard()" style="background:rgba(255,255,255,0.1); color:white; border:1px solid rgba(255,255,255,0.2); padding:10px 25px; border-radius:30px; cursor:pointer; margin-bottom:30px;">⬅ Volver al Dashboard</button>
        <div id="results-content" class="results-card" style="background:rgba(30,30,40,0.8); padding:3rem; border-radius:24px; border:1px solid rgba(255,255,255,0.1);"></div>
    </section>
  `;
    setupEventListeners();
    checkAuth();
}

// ==========================================
// 2. LISTENERS
// ==========================================
function setupEventListeners() {
    document.getElementById('login-form')?.addEventListener('submit', handleLogin);
    document.getElementById('register-form')?.addEventListener('submit', handleRegister);
    document.getElementById('logout-btn')?.addEventListener('click', handleLogout);
    document.getElementById('create-survey-form')?.addEventListener('submit', handleCreateSurveySubmit);
    document.getElementById('respond-form')?.addEventListener('submit', handleRespondSubmit);
    document.getElementById('show-register-link')?.addEventListener('click', (e) => { e.preventDefault(); showRegister(); });
    document.getElementById('show-login-link')?.addEventListener('click', (e) => { e.preventDefault(); showLogin(); });

    // Buscador en tiempo real
    document.getElementById('search-input')?.addEventListener('input', (e) => {
        const term = e.target.value.toLowerCase();
        const filtered = allSurveys.filter(s =>
                (s.titulo && s.titulo.toLowerCase().includes(term)) ||
                (s.descripcion && s.descripcion.toLowerCase().includes(term)) ||
				(s.juegoNombre && s.juegoNombre.toLowerCase().includes(term)) ||
				(s.categoriaNombre && s.categoriaNombre.toLowerCase().includes(term))
            // Podríamos filtrar también por nombre de juego si viniera en el DTO
        );
        const isMine = document.getElementById('tab-mine').classList.contains('active');
        displaySurveys(filtered, isMine);
    });
}

// ==========================================
// 3. LOGICA DASHBOARD Y BUSQUEDA
// ==========================================

window.switchTab = function(tabName) {
    document.getElementById('tab-all').classList.remove('active');
    document.getElementById('tab-mine').classList.remove('active');
    document.getElementById(`tab-${tabName}`).classList.add('active');
    document.getElementById('search-input').value = '';

    if (tabName === 'all') {
        document.getElementById('dashboard-title').textContent = "Encuestas Disponibles";
        loadSurveys(false);
    } else {
        document.getElementById('dashboard-title').textContent = "Mis Encuestas Creadas";
        loadSurveys(true);
    }
}

async function loadSurveys(isMine = false) {
    const container = document.getElementById('surveys-container');
    container.innerHTML = '<p class="loading" style="color:white;">Cargando encuestas...</p>';
    try {
        const endpoint = isMine ? `${API_URL}/surveys/my-surveys` : `${API_URL}/surveys`;
        const res = await fetch(endpoint, { headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` } });
        if (res.ok) {
            const data = await res.json();
            allSurveys = data;
            displaySurveys(data, isMine);
        } else {
            container.innerHTML = '<p style="color:#ff7675;">Error al cargar datos.</p>';
        }
    } catch (error) { console.error(error); }
}

function displaySurveys(surveys, isMyList) {
    const container = document.getElementById('surveys-container');
    if (surveys.length === 0) {
        container.innerHTML = '<p style="color:white; text-align:center; opacity:0.7;">No se encontraron encuestas.</p>';
        return;
    }

    const isCrafter = currentUser && (currentUser.role.includes('CRAFTER'));

    container.innerHTML = surveys.map(survey => {
        let buttonsHtml = '';

        if (isCrafter && isMyList) {
            buttonsHtml = `
            <button onclick="window.viewSurveyResults(${survey.surveyId})" style="background:#6c5ce7; border:none; color:white; padding:8px 15px; border-radius:8px; cursor:pointer; margin-right:5px;">📊 Resultados</button>
            <button onclick="window.editSurvey(${survey.surveyId})" style="background:#0984e3; border:none; color:white; padding:8px 15px; border-radius:8px; cursor:pointer; margin-right:5px;">✏ Editar</button>
            <button onclick="window.deleteSurvey(${survey.surveyId})" style="background:#d63031; border:none; color:white; padding:8px 15px; border-radius:8px; cursor:pointer;">🗑</button>
        `;
        } else if (isCrafter && !isMyList) {
            buttonsHtml = `
            <button onclick="window.viewSurveyResults(${survey.surveyId})" style="background:rgba(108, 92, 231, 0.2); border:1px solid #a29bfe; color:#a29bfe; padding:8px 15px; border-radius:8px; cursor:pointer;">📊 Ver Resultados</button>
         `;
        } else {
            // PLAYER: Ahora ve Responder Y Resultados
            buttonsHtml = `
            <button onclick="window.viewSurveyResults(${survey.surveyId})" style="background:rgba(255,255,255,0.1); border:1px solid rgba(255,255,255,0.3); color:white; padding:10px 15px; border-radius: 10px; cursor:pointer; margin-right:10px; font-weight:bold;">📊 Resultados</button>
            <button onclick="window.openRespondModal(${survey.surveyId})" style="background: linear-gradient(45deg, #0984e3, #74b9ff); color: white; padding: 10px 30px; border:none; border-radius: 10px; cursor:pointer; font-weight:bold;">Responder</button>
        `;
        }

        return `
    <div class="survey-row" style="padding: 1.5rem; border-radius: 16px; display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
      <div style="flex: 1;">
        <div style="display: flex; align-items: center; gap: 10px; margin-bottom: 5px;">
            <span style="background: #00b894; color: white; padding: 2px 10px; border-radius: 20px; font-size: 0.7rem; font-weight:bold;">${survey.estado || 'ACTIVA'}</span>
            <span style="background: #6c5ce7; color: white; padding: 2px 10px; border-radius: 20px; font-size: 0.7rem; font-weight:bold;">${survey.juegoNombre || 'Sin juego'}</span>
        </div>
        <h3 style="margin: 0; color: white; font-size: 1.4rem;">${survey.titulo}</h3>
        <p style="margin: 5px 0 0 0; color: rgba(255,255,255,0.7); font-size: 0.95rem;">${survey.descripcion || 'Sin descripción'}</p>
        <small style="color: #a29bfe;">${survey.totalRespuestas || 0} respuestas • Juego: ${survey.juegoNombre || 'No especificado'}</small>
      </div>
      <div style="display: flex; align-items: center;">
        ${buttonsHtml}
      </div>
    </div>
  `}).join('');
}

// ==========================================
// 4. ACCIONES (CREAR, EDITAR, BORRAR)
// ==========================================

window.showCreateScreen = async function() {
    if (!currentUser || !currentUser.role.includes('CRAFTER')) { alert("Acceso denegado"); return; }
    hideAll();
    document.getElementById('create-section').classList.remove('hidden');
    isEditing = false;
    editingSurveyId = null;
    document.getElementById('form-title').textContent = "📝 Nueva Encuesta";
    document.getElementById('save-btn').innerHTML = "💾 Publicar";
    document.getElementById('new-survey-title').value = "";
    document.getElementById('new-survey-desc').value = "";
    newSurveyQuestions = [];
    renderQuestionsList();

    // CARGAR LISTA DE JUEGOS
    await loadGamesForSelect();

    window.scrollTo(0,0);
}

// NUEVA FUNCIÓN: CARGAR JUEGOS EN EL SELECT
async function loadGamesForSelect() {
    const select = document.getElementById('new-survey-game');
    select.innerHTML = '<option>Cargando...</option>';
    try {
        const res = await fetch(`${API_URL}/games`, { headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` } });
        if(res.ok) {
            const games = await res.json();
            if(games.length === 0) {
                select.innerHTML = '<option value="">No hay juegos. ¡Crea uno!</option>';
            } else {
                select.innerHTML = games.map(g => `<option value="${g.id}">${g.titulo} (${g.categoriaNombre})</option>`).join('');
            }
        }
    } catch(e) { console.error(e); select.innerHTML='<option>Error al cargar</option>'; }
}

// NUEVA FUNCIÓN: CREAR JUEGO RÁPIDO
window.createNewGameQuick = async function() {
    const titulo = prompt("Nombre del nuevo videojuego:");
    if(!titulo) return;
    const cat = prompt("Categoría del juego (ej: RPG, Shooter):");
    if(!cat) return;

    try {
        const res = await fetch(`${API_URL}/games`, {
            method: 'POST',
            headers: {'Content-Type':'application/json','Authorization':`Bearer ${localStorage.getItem('token')}`},
            body: JSON.stringify({ titulo: titulo, descripcion: "Creado desde encuesta", nombreCategoria: cat })
        });
        if(res.ok) {
            alert("Juego creado. Seleccionándolo...");
            await loadGamesForSelect(); // Recargar lista
            // Intentar seleccionar el nuevo (simplificado, selecciona el ultimo o recarga)
        } else { alert("Error al crear juego"); }
    } catch(e) { console.error(e); }
}

window.editSurvey = async function(id) {
    try {
        const res = await fetch(`${API_URL}/surveys/${id}`, { headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` } });
        if(res.ok) {
            const survey = await res.json();
            isEditing = true;
            editingSurveyId = id;
            hideAll();
            document.getElementById('create-section').classList.remove('hidden');
            document.getElementById('form-title').textContent = "✏ Editar Encuesta";
            document.getElementById('save-btn').innerHTML = "🔄 Actualizar";
            document.getElementById('new-survey-title').value = survey.titulo;
            document.getElementById('new-survey-desc').value = survey.descripcion;

            // Cargar juegos y seleccionar el correcto
            await loadGamesForSelect();
            if(survey.juegoId) document.getElementById('new-survey-game').value = survey.juegoId;

            newSurveyQuestions = survey.preguntas.map(q => ({
                texto: q.textoPregunta || q.texto,
                tipoPregunta: 'SINGLE_CHOICE',
                opciones: q.opciones.map(o => o.textoOpcion || o.texto)
            }));
            renderQuestionsList();
        }
    } catch(e) { console.error(e); alert("Error al cargar para editar"); }
}

window.addQuestionToLocal = function() {
    const t = document.getElementById('q-text').value;
    const o = document.getElementById('q-options').value;
    if(t && o) { newSurveyQuestions.push({texto:t, tipoPregunta:'SINGLE_CHOICE', opciones:o.split(',')}); renderQuestionsList(); document.getElementById('q-text').value=''; document.getElementById('q-options').value=''; } else { alert("Faltan datos"); }
}

function renderQuestionsList() {
    const list = document.getElementById('questions-list');
    list.innerHTML = newSurveyQuestions.map((q,i)=>`
        <div style="background:rgba(255,255,255,0.05); padding:10px; margin-bottom:5px; border-radius:8px; display:flex; justify-content:space-between;">
            <div><strong>${i+1}. ${q.texto}</strong> <small>(${q.opciones.join(', ')})</small></div>
            <button onclick="window.removeQuestion(${i})" style="color:#ff7675; background:none; border:none; cursor:pointer;">&times;</button>
        </div>`).join('');
}
window.removeQuestion = function(i) { newSurveyQuestions.splice(i,1); renderQuestionsList(); }

async function handleCreateSurveySubmit(e) {
    e.preventDefault();
    if(newSurveyQuestions.length === 0) { alert("Agrega preguntas primero"); return; }

    const gameId = document.getElementById('new-survey-game').value;
    if(!gameId) { alert("Selecciona un videojuego"); return; }

    const btn = e.target.querySelector('button[type="submit"]');
    const originalText = btn.innerHTML;
    btn.innerHTML="⏳ Procesando..."; btn.disabled=true;

    const p = {
        titulo: document.getElementById('new-survey-title').value,
        descripcion: document.getElementById('new-survey-desc').value,
        juegoId: parseInt(gameId), // AQUI SE TOMA EL ID DEL SELECT
        tipoEncuesta: 'JUEGO',
        preguntas: newSurveyQuestions
    };

    try {
        let url = `${API_URL}/surveys`;
        let method = 'POST';
        if(isEditing && editingSurveyId) {
            url = `${API_URL}/surveys/${editingSurveyId}`;
            method = 'PUT';
        }
        const r = await fetch(url, {
            method: method,
            headers:{'Content-Type':'application/json','Authorization':`Bearer ${localStorage.getItem('token')}`},
            body:JSON.stringify(p)
        });

        if(r.ok){
            alert(isEditing ? "✅ Encuesta Actualizada" : "✅ Encuesta Publicada");
            window.showDashboard();
            if (document.getElementById('tab-mine').classList.contains('active')) {
                window.switchTab('mine');
            }
        } else { alert('Error al guardar'); }
    } catch(err){ console.error(err); } finally { btn.innerHTML=originalText; btn.disabled=false; }
}

// RESPONDER
window.openRespondModal = async function(id) {
    const res = await fetch(`${API_URL}/surveys/${id}`, { headers: {'Authorization':`Bearer ${localStorage.getItem('token')}`} });
    const s = await res.json();
    document.getElementById('respond-title').textContent = s.titulo;
    document.getElementById('respond-form').dataset.surveyId = id;
    document.getElementById('respond-questions-container').innerHTML = s.preguntas.map(q=>`
        <div style="margin-bottom:20px; background:rgba(0,0,0,0.2); padding:20px; border-radius:12px;">
            <p style="color:white; margin-bottom:10px;">${q.textoPregunta}</p>
            ${q.opciones.map(o=>`
                <label style="display:block; padding:8px; cursor:pointer;">
                    <input type="radio" name="q_${q.questionId}" value="${o.optionId}" required> ${o.textoOpcion}
                </label>`).join('')}
        </div>
    `).join('');
    hideAll(); document.getElementById('respond-section').classList.remove('hidden');
}

async function handleRespondSubmit(e) {
    e.preventDefault();
    const id = e.target.dataset.surveyId;
    const fd = new FormData(e.target);
    const resp = [];
    for(let [k,v] of fd.entries()) if(k.startsWith('q_')) resp.push({questionId: parseInt(k.split('_')[1]), optionId: parseInt(v)});

    try {
        await fetch(`${API_URL}/surveys/${id}/respond`, { method:'POST', headers:{'Content-Type':'application/json','Authorization':`Bearer ${localStorage.getItem('token')}`}, body:JSON.stringify({respuestas:resp, tiempoJugadoJuego:'10h', plataformaUsada:'PC'}) });
        alert("¡Gracias por tu respuesta!"); window.showDashboard();
    } catch(err){ console.error(err); }
}

// RESULTADOS
window.viewSurveyResults = async function(id) {
    const res = await fetch(`${API_URL}/surveys/${id}/results`, { headers: {'Authorization':`Bearer ${localStorage.getItem('token')}`} });
    const d = await res.json();
    hideAll(); document.getElementById('results-section').classList.remove('hidden');
    let html = `<div style="text-align:center; margin-bottom:40px;"><h2 style="color:white; font-size:2.5rem;">${d.titulo}</h2><p>Total Votos: ${d.totalRespuestas}</p></div>`;
    d.preguntas.forEach(q=>{
        html += `<div style="margin-bottom:30px; background:rgba(0,0,0,0.2); padding:20px; border-radius:16px;"><h3>${q.textoPregunta}</h3>`;
        q.opciones.forEach(o=>{
            const pct = o.porcentaje || 0;
            html+=`<div style="margin-bottom:10px;"><div style="display:flex; justify-content:space-between;"><span>${o.textoOpcion}</span><span>${o.votos} (${pct}%)</span></div>
                   <div style="background:rgba(255,255,255,0.1); height:10px; border-radius:5px;"><div style="background:#00b894; width:${pct}%; height:100%; border-radius:5px;"></div></div></div>`;
        });
        html+='</div>';
    });
    document.getElementById('results-content').innerHTML = html;
}

window.deleteSurvey = async function(id) {
    if(confirm("¿Eliminar permanentemente?")) {
        await fetch(`${API_URL}/surveys/${id}`, {method:'DELETE', headers:{'Authorization':`Bearer ${localStorage.getItem('token')}`}});
        if(document.getElementById('tab-mine').classList.contains('active')) window.switchTab('mine');
        else window.switchTab('all');
    }
}

// NAV
window.showDashboard = function() {
    hideAll();
    document.getElementById('dashboard-section').classList.remove('hidden');
    window.scrollTo(0,0);

    if(currentUser){
        document.getElementById('user-name').textContent = currentUser.nombre;

        // LOGICA DE ROLES MEJORADA
        const isCrafter = (currentUser.role.includes('CRAFTER'));

        const createBtn = document.getElementById('create-btn');
        const crafterTabs = document.getElementById('crafter-tabs');

        if(isCrafter) {
            createBtn.style.display = 'block';
            crafterTabs.style.display = 'flex';
            window.switchTab('mine');
        } else {
            createBtn.style.display = 'none';
            crafterTabs.style.display = 'none';
            loadSurveys(false);
        }
    }
}

function showLogin() {
    hideAll();
    document.getElementById('auth-section').classList.remove('hidden');
    document.getElementById('login-container').classList.remove('hidden');
    document.getElementById('register-container').classList.add('hidden');
}
function showRegister() {
    document.getElementById('auth-section').classList.remove('hidden');
    document.getElementById('login-container').classList.add('hidden');
    document.getElementById('register-container').classList.remove('hidden');
}
function hideAll() { ['auth-section','dashboard-section','results-section','create-section','respond-section'].forEach(id=>document.getElementById(id).classList.add('hidden')); }

// AUTH
function checkAuth() {
    const t=localStorage.getItem('token');
    const u=localStorage.getItem('user');
    if(t&&u){
        try{ currentUser=JSON.parse(u); window.showDashboard(); }
        catch(e){ handleLogout(); }
    } else showLogin();
}

async function handleLogin(e) {
    e.preventDefault();
    const res=await fetch(`${API_URL}/auth/login`, {method:'POST', headers:{'Content-Type':'application/json'}, body:JSON.stringify({email:document.getElementById('login-email').value, password:document.getElementById('login-password').value})});
    if(res.ok){
        const d=await res.json();
        if(d.token){
            localStorage.setItem('token',d.token);
            const usuarioAdaptado = {
                nombre: d.username || (d.role.includes('CRAFTER') ? 'Admin' : 'Player'),
                role: d.role,
                email: document.getElementById('login-email').value
            };
            localStorage.setItem('user',JSON.stringify(usuarioAdaptado));
            currentUser=usuarioAdaptado;
            window.showDashboard();
        }
    } else alert('Error credenciales');
}

async function handleRegister(e) { e.preventDefault(); const p={username:document.getElementById('register-name').value, email:document.getElementById('register-email').value, password:document.getElementById('register-password').value, role:document.getElementById('register-role').value}; const res=await fetch(`${API_URL}/auth/register`, {method:'POST', headers:{'Content-Type':'application/json'}, body:JSON.stringify(p)}); if(res.ok){ alert('Ok'); showLogin(); } }
function handleLogout() { localStorage.clear(); currentUser=null; showLogin(); }

document.addEventListener('DOMContentLoaded', renderApp);