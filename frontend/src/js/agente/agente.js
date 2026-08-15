import { apiFetch } from '../api.js';
import { crearVenta, obtenerMisVentas } from '../api.js';
import {
    obtenerRol,
    obtenerUsername,
    cerrarSesion,
    protegerVista
} from '../auth.js';


// ==============================
// VERIFICAR ROL
// ==============================

protegerVista(['AGENTE']);


// ==============================
// MOSTRAR USUARIO
// ==============================

const usernameElement = document.getElementById('username');

if (usernameElement) {
    usernameElement.textContent = obtenerUsername();
}


// ==============================
// ELEMENTOS DEL FORMULARIO
// ==============================

const ventaForm = document.getElementById('ventaForm');

const successMessage = document.getElementById('successMessage');
const errorMessage = document.getElementById('errorMessage');


// ==============================
// REGISTRAR VENTA
// ==============================

ventaForm.addEventListener('submit', async (event) => {

    event.preventDefault();

    // Ocultar mensajes anteriores
    successMessage.classList.add('d-none');
    errorMessage.classList.add('d-none');


    // Obtener datos del formulario
    const venta = {

        dniCliente: document.getElementById('dniCliente').value,

        nombreCliente: document.getElementById('nombreCliente').value,

        telefonoCliente: document.getElementById('telefonoCliente').value,

        direccionCliente: document.getElementById('direccionCliente').value,

        planActual: document.getElementById('planActual').value,

        planNuevo: document.getElementById('planNuevo').value,

        codigoLlamada: document.getElementById('codigoLlamada').value,

        producto: document.getElementById('producto').value,

        monto: Number(document.getElementById('monto').value)

    };


    console.log('Venta a registrar:', venta);


    try {

        const response = await apiFetch('/ventas', {

            method: 'POST',

            body: JSON.stringify(venta)

        });


        console.log('Venta registrada:', response);


        // Mostrar mensaje
        successMessage.textContent =
            'Venta registrada correctamente.';

        successMessage.classList.remove('d-none');


        // Limpiar formulario
        ventaForm.reset();


    } catch (error) {

        console.error(
            'Error al registrar venta:',
            error
        );


        errorMessage.textContent =
             error.message;

        errorMessage.classList.remove('d-none');

    }

});


// ==============================
// CERRAR SESIÓN
// ==============================

const logoutButton = document.getElementById('logoutButton');

if (logoutButton) {

    logoutButton.addEventListener('click', () => {

        cerrarSesion();

        window.location.href = '/';

    });

}

// =========================================
// ELEMENTOS DE MIS VENTAS
// =========================================

const tablaVentas = document.getElementById('ventasTableBody');
const btnFiltrar = document.getElementById('buscarVentasButton');

const filtroEstado = document.getElementById('filtroEstado');
const filtroDesde = document.getElementById('filtroDesde');
const filtroHasta = document.getElementById('filtroHasta');
const paginaAnterior = document.getElementById('paginaAnterior');
const paginaSiguiente = document.getElementById('paginaSiguiente');
const paginaActualTexto = document.getElementById('paginaActual');

// =========================================
// PAGINACIÓN
// =========================================

let paginaActual = 0;
const tamanioPagina = 10;

// =========================================
// CARGAR MIS VENTAS
// =========================================

async function cargarMisVentas() {

    try {

        const filtros = {
            estado: filtroEstado.value,
            desde: filtroDesde.value,
            hasta: filtroHasta.value,
            page: paginaActual,
            size: tamanioPagina
        };

        const data = await obtenerMisVentas(filtros);

        console.log('Mis ventas:', data);

        mostrarVentas(data);
        mostrarPaginacion(data);
        
    } catch (error) {

        console.error('Error al cargar mis ventas:', error);

        tablaVentas.innerHTML = `
            <tr>
                <td colspan="8" class="text-center text-danger">
                    Error al cargar las ventas
                </td>
            </tr>
        `;
    }
}


// =========================================
// MOSTRAR VENTAS
// =========================================

function mostrarVentas(data) {

    const ventas = data.content || data;


    if (!ventas || ventas.length === 0) {

        tablaVentas.innerHTML = `
            <tr>
                <td colspan="8" class="text-center text-muted">
                    No hay ventas para mostrar.
                </td>
            </tr>
        `;

        return;
    }


    tablaVentas.innerHTML = '';


    ventas.forEach(venta => {

        const fila = document.createElement('tr');

        fila.innerHTML = `

            <td>${venta.nombreCliente}</td>

            <td>${venta.dniCliente}</td>

            <td>${venta.producto}</td>

            <td>${venta.planNuevo}</td>

            <td>S/ ${venta.monto}</td>

            <td>${crearEstadoBadge(venta.estado)}</td>

            <td>${formatearFecha(venta.fechaRegistro)}</td>
        `;

        tablaVentas.appendChild(fila);

    });
}

// =========================================
// MOSTRAR PAGINACIÓN
// =========================================

function mostrarPaginacion(data) {

    paginaActualTexto.textContent =
        `Página ${data.number + 1} de ${data.totalPages}`;

    paginaAnterior.disabled =
        data.first;

    paginaSiguiente.disabled =
        data.last;
}

paginaAnterior.addEventListener('click', async () => {

    if (paginaActual > 0) {

        paginaActual--;

        await cargarMisVentas();

    }

});

paginaSiguiente.addEventListener('click', async () => {

    paginaActual++;

    await cargarMisVentas();

});


// =========================================
// ESTADO
// =========================================

function crearEstadoBadge(estado) {

    switch (estado) {

        case 'PENDIENTE':

            return `
                <span class="badge bg-warning text-dark">
                    Pendiente
                </span>
            `;


        case 'APROBADA':

            return `
                <span class="badge bg-success">
                    Aprobada
                </span>
            `;


        case 'RECHAZADA':

            return `
                <span class="badge bg-danger">
                    Rechazada
                </span>
            `;


        default:

            return `
                <span class="badge bg-secondary">
                    ${estado}
                </span>
            `;
    }
}


// =========================================
// FECHA
// =========================================

function formatearFecha(fecha) {

    if (!fecha) {
        return '-';
    }

    return new Date(fecha).toLocaleDateString('es-PE');
}


// =========================================
// ACTUALIZAR
// =========================================

btnFiltrar.addEventListener('click', () => {

    cargarMisVentas();

});


// =========================================
// CARGAR AL ENTRAR
// =========================================


cargarMisVentas();

