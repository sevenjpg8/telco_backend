import { obtenerToken } from './auth.js';

const API_URL = import.meta.env.VITE_API_URL;


// =========================================
// PETICIÓN GENERAL
// =========================================

export async function apiFetch(endpoint, options = {}) {

    const token = localStorage.getItem('token');

    const headers = {
        'Content-Type': 'application/json',
        ...options.headers
    };

    if (token) {
        headers.Authorization = `Bearer ${token}`;
    }

    const response = await fetch(`${API_URL}${endpoint}`, {
        ...options,
        headers
    });

    if (!response.ok) {

        const errorData = await response.json();

        console.error('Error del backend:', errorData);

        throw new Error(
            errorData.message || `Error HTTP: ${response.status}`
        );
    }

    return response.json();
}


// =========================================
// VENTAS - AGENTE
// =========================================

export async function crearVenta(data) {

    return await apiFetch('/ventas', {
        method: 'POST',
        body: JSON.stringify(data)
    });

}


export async function obtenerMisVentas(filtros = {}) {

    const params = new URLSearchParams();

    if (filtros.estado) {
        params.append('estado', filtros.estado);
    }

    if (filtros.desde) {
        params.append('desde', filtros.desde);
    }

    if (filtros.hasta) {
        params.append('hasta', filtros.hasta);
    }

    params.append('page', filtros.page ?? 0);
    params.append('size', filtros.size ?? 10);

    return await apiFetch(
        `/ventas/mis-ventas?${params.toString()}`
    );
}


// =========================================
// VENTAS - BACKOFFICE
// =========================================

export async function obtenerVentasPendientes() {

    return await apiFetch('/ventas/pendientes');

}


// =========================================
// APROBAR VENTA - BACKOFFICE
// =========================================

export async function aprobarVenta(id) {

    return await apiFetch(`/ventas/${id}/aprobar`, {
        method: 'POST'
    });

}


// =========================================
// RECHAZAR VENTA - BACKOFFICE
// =========================================

export async function rechazarVenta(id, motivo) {

    return await apiFetch(`/ventas/${id}/rechazar`, {

        method: 'POST',

        body: JSON.stringify({
            motivoRechazo: motivo
        })

    });

}


// =========================================
// VENTAS - SUPERVISOR
// =========================================

export async function obtenerVentasEquipo(filtros = {}) {

    const params = new URLSearchParams();

    if (filtros.estado) {
        params.append('estado', filtros.estado);
    }

    if (filtros.agenteId) {
        params.append('agenteId', filtros.agenteId);
    }

    if (filtros.desde) {
        params.append('desde', filtros.desde);
    }

    if (filtros.hasta) {
        params.append('hasta', filtros.hasta);
    }

    const query = params.toString();

    const endpoint = query
        ? `/ventas/equipo?${query}`
        : '/ventas/equipo';

    return await apiFetch(endpoint);
}


// =========================================
// RESUMEN - SUPERVISOR
// =========================================

export async function obtenerResumen(desde, hasta) {

    const params = new URLSearchParams();

    params.append('desde', desde);
    params.append('hasta', hasta);

    return await apiFetch(`/reportes/resumen?${params.toString()}`);
}








