import { obtenerVentasEquipo, obtenerResumen} from '../api.js';

import {
    obtenerRol,
    obtenerUsername,
    cerrarSesion,
    protegerVista
} from '../auth.js';


// ==============================
// VERIFICAR ROL
// ==============================

protegerVista(['SUPERVISOR', 'ADMIN']);

// ==============================
// MOSTRAR USUARIO
// ==============================

const usernameElement =
    document.getElementById('username');

if (usernameElement) {

    usernameElement.textContent =
        obtenerUsername();

}


// ==============================
// ELEMENTOS
// ==============================

const tablaVentas =
    document.getElementById('ventasTableBody');

const tablaVentasPorDia =
    document.getElementById(
        'ventasPorDiaTableBody'
    );

const filtroEstado =
    document.getElementById('filtroEstado');

const filtroAgente =
    document.getElementById('filtroAgente');

const filtroDesde =
    document.getElementById('filtroDesde');

const filtroHasta =
    document.getElementById('filtroHasta');

const filtrarButton =
    document.getElementById('filtrarButton');

const actualizarButton =
    document.getElementById('actualizarButton');

const successMessage =
    document.getElementById('successMessage');

const errorMessage =
    document.getElementById('errorMessage');


// ==============================
// RESUMEN
// ==============================

const totalPendientes =
    document.getElementById(
        'totalPendientes'
    );

const totalAprobadas =
    document.getElementById(
        'totalAprobadas'
    );

const totalRechazadas =
    document.getElementById(
        'totalRechazadas'
    );

const montoTotalAprobadas =
    document.getElementById(
        'montoTotalAprobadas'
    );


// ==============================
// CARGAR VENTAS DEL EQUIPO
// ==============================

async function cargarVentasEquipo() {

    try {

        const filtros = {

            estado:
                filtroEstado.value,

            agenteId:
                filtroAgente.value,

            desde:
                filtroDesde.value,

            hasta:
                filtroHasta.value

        };


        const data =
            await obtenerVentasEquipo(
                filtros
            );


        console.log(
            'Ventas del equipo:',
            data
        );


        mostrarVentas(data);


    } catch (error) {

        console.error(
            'Error al cargar ventas del equipo:',
            error
        );


        tablaVentas.innerHTML = `
            <tr>
                <td
                    colspan="9"
                    class="text-center text-danger">

                    Error al cargar las ventas.

                </td>
            </tr>
        `;

    }

}


// ==============================
// MOSTRAR VENTAS
// ==============================

function mostrarVentas(data) {

    const ventas =
        data.content || data;


    if (
        !ventas ||
        ventas.length === 0
    ) {

        tablaVentas.innerHTML = `
            <tr>
                <td
                    colspan="9"
                    class="text-center text-muted">

                    No hay ventas para mostrar.

                </td>
            </tr>
        `;

        return;

    }


    tablaVentas.innerHTML = '';


    ventas.forEach(venta => {

        const fila =
            document.createElement('tr');


        fila.innerHTML = `

            <td>
                ${venta.agenteId?.username ?? '-'}
            </td>

            <td>
                ${venta.nombreCliente}
            </td>

            <td>
                ${venta.dniCliente}
            </td>

            <td>
                ${venta.producto}
            </td>

            <td>
                ${venta.planNuevo}
            </td>

            <td>
                S/ ${venta.monto}
            </td>

            <td>
                ${crearEstadoBadge(
            venta.estado
        )}
            </td>

            <td>
                ${formatearFecha(
            venta.fechaRegistro
        )}
            </td>

        `;


        tablaVentas.appendChild(fila);

    });

}


// ==============================
// ESTADO
// ==============================

function crearEstadoBadge(estado) {

    switch (estado) {

        case 'PENDIENTE':

            return `
                <span
                    class="badge bg-warning text-dark">

                    Pendiente

                </span>
            `;


        case 'APROBADA':

            return `
                <span
                    class="badge bg-success">

                    Aprobada

                </span>
            `;


        case 'RECHAZADA':

            return `
                <span
                    class="badge bg-danger">

                    Rechazada

                </span>
            `;


        default:

            return `
                <span
                    class="badge bg-secondary">

                    ${estado}

                </span>
            `;

    }

}


// ==============================
// FECHA
// ==============================

function formatearFecha(fecha) {

    if (!fecha) {
        return '-';
    }

    return new Date(fecha)
        .toLocaleDateString('es-PE');

}


// ==============================
// CARGAR RESUMEN
// ==============================

async function cargarResumen() {

    try {

        const desde = filtroDesde.value;
        const hasta = filtroHasta.value;

        const data =
            await obtenerResumen(
                desde,
                hasta
            );


        console.log(
            'Resumen del supervisor:',
            data
        );


        totalPendientes.textContent =
            data.pendientes ?? 0;

        totalAprobadas.textContent =
            data.aprobadas ?? 0;

        totalRechazadas.textContent =
            data.rechazadas ?? 0;


        montoTotalAprobadas.textContent =
            `S/ ${Number(
                data.montoTotalAprobadas ?? 0
            ).toFixed(2)}`;


        mostrarVentasPorDia(
            data.ventasPorDia
        );


    } catch (error) {

        console.error(
            'Error al cargar resumen:',
            error
        );

    }

}


// ==============================
// VENTAS POR DÍA
// ==============================

function mostrarVentasPorDia(
    ventasPorDia
) {

    if (
        !ventasPorDia ||
        ventasPorDia.length === 0
    ) {

        tablaVentasPorDia.innerHTML = `
            <tr>
                <td
                    colspan="3"
                    class="text-center text-muted">

                    No hay datos para mostrar.

                </td>
            </tr>
        `;

        return;

    }


    tablaVentasPorDia.innerHTML = '';


    ventasPorDia.forEach(dia => {

        const fila =
            document.createElement('tr');


        fila.innerHTML = `

            <td>
                ${dia.fecha}
            </td>

            <td>
                ${dia.cantidad}
            </td>

            <td>
                S/ ${Number(
            dia.monto ?? 0
        ).toFixed(2)}
            </td>

        `;


        tablaVentasPorDia.appendChild(
            fila
        );

    });

}


// ==============================
// FILTRAR
// ==============================

filtrarButton.addEventListener(
    'click',
    async () => {

        await cargarVentasEquipo();

        await cargarResumen();

    }
);


// ==============================
// ACTUALIZAR
// ==============================

actualizarButton.addEventListener(
    'click',
    async () => {

        await cargarVentasEquipo();

        await cargarResumen();

    }
);


// ==============================
// CERRAR SESIÓN
// ==============================

const logoutButton =
    document.getElementById(
        'logoutButton'
    );


if (logoutButton) {

    logoutButton.addEventListener(
        'click',
        () => {

            cerrarSesion();

            window.location.href = '/';

        }
    );

}


// ==============================
// CARGAR AL ENTRAR
// ==============================

cargarVentasEquipo();
cargarResumen();
