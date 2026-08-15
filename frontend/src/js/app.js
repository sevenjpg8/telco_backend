import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';

import { apiFetch } from './api.js';
import { guardarSesion } from './auth.js';

const API_URL = import.meta.env.VITE_API_URL;

const loginForm = document.getElementById('loginForm');
const loginError = document.getElementById('loginError');

loginForm.addEventListener('submit', async (event) => {

    event.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    try {

        const response = await fetch(`${API_URL}/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: username,
                password: password
            })
        });

        if (!response.ok) {
            throw new Error('Usuario o contraseña incorrectos');
        }

        const data = await response.json();

        guardarSesion(
            data.token,
            data.rol,
            username
        );

    
        // Redirigir según el rol

        if (data.rol === 'AGENTE') {

            window.location.href = '/src/views/agente.html';

        } else if (data.rol === 'BACKOFFICE') {

            window.location.href = '/src/views/backoffice.html';

        } else if (
            data.rol === 'SUPERVISOR' ||
            data.rol === 'ADMIN'
        ) {

            window.location.href = '/src/views/supervisor.html';

        } else {

            console.error('Rol no reconocido:', data.rol);

        }

        loginError.classList.add('d-none');

    } catch (error) {

        console.error('Error en login:', error.message);

        loginError.textContent = error.message;
        loginError.classList.remove('d-none');
    }
});