export function guardarSesion(token, rol, username) {
    localStorage.setItem('token', token);
    localStorage.setItem('rol', rol);
    localStorage.setItem('username', username);
}

export function obtenerToken() {
    return localStorage.getItem('token');
}

export function obtenerRol() {
    return localStorage.getItem('rol');
}

export function obtenerUsername() {
    return localStorage.getItem('username');
}

export function estaAutenticado() {
    return obtenerToken() !== null;
}

export function cerrarSesion() {
    localStorage.removeItem('token');
    localStorage.removeItem('rol');
    localStorage.removeItem('username');
}

export function protegerVista(rolesPermitidos = []) {

    const token = obtenerToken();
    const rol = obtenerRol();

    // No hay sesión
    if (!token) {
        window.location.href = '/';
        return false;
    }

    // Hay sesión pero el rol no tiene permiso
    if (
        rolesPermitidos.length > 0 &&
        !rolesPermitidos.includes(rol)
    ) {
        window.location.href = '/';
        return false;
    }

    return true;
}