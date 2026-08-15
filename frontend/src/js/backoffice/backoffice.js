import { obtenerVentasPendientes, aprobarVenta, rechazarVenta } from '../api.js';

import {
    obtenerRol,
    obtenerUsername,
    cerrarSesion,
    protegerVista
} from '../auth.js';


// ==============================
// VERIFICAR ROL
// ==============================

protegerVista(['BACKOFFICE', 'ADMIN']);


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

const actualizarButton =
    document.getElementById('actualizarButton');

const successMessage =
    document.getElementById('successMessage');

const errorMessage =
    document.getElementById('errorMessage');

const rechazarModalElement =
    document.getElementById('rechazarModal');

const rechazarModal =
    new bootstrap.Modal(rechazarModalElement);

const ventaIdRechazar =
    document.getElementById('ventaIdRechazar');

const motivoRechazo =
    document.getElementById('motivoRechazo');

const modalError =
    document.getElementById('modalError');

const confirmarRechazoButton =
    document.getElementById('confirmarRechazoButton');


// ==============================
// CARGAR VENTAS PENDIENTES
// ==============================

async function cargarVentasPendientes() {

    try {

        const data =
            await obtenerVentasPendientes();

        mostrarVentas(data);

    } catch (error) {

        console.error(
            'Error al cargar ventas pendientes:',
            error
        );

        tablaVentas.innerHTML = `
            <tr>
                <td
                    colspan="8"
                    class="text-center text-danger">

                    Error al cargar las ventas pendientes.

                </td>
            </tr>
        `;

    }

}



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
                    colspan="8"
                    class="text-center text-muted">

                    No hay ventas pendientes.

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
                ${formatearFecha(
            venta.fechaRegistro
        )}
            </td>

            <td>

                <button
                    class="btn btn-success btn-sm me-1"
                    data-id="${venta.id}"
                    data-action="aprobar">

                    Aprobar

                </button>

                <button
                    class="btn btn-danger btn-sm"
                    data-id="${venta.id}"
                    data-action="rechazar">

                    Rechazar

                </button>

            </td>

        `;


        tablaVentas.appendChild(fila);


        // ==============================
        // BOTÓN RECHAZAR
        // ==============================


        const botonRechazar =
            fila.querySelector(
                '[data-action="rechazar"]'
            );


        botonRechazar.addEventListener(
            'click',
            () => {

                const id =
                    botonRechazar.dataset.id;

                abrirModalRechazo(id);

            }
        );


        // ==============================
        // BOTÓN APROBAR
        // ==============================

        const botonAprobar =
            fila.querySelector(
                '[data-action="aprobar"]'
            );


        botonAprobar.addEventListener(
            'click',
            () => {

                const id =
                    botonAprobar.dataset.id;

                aprobarVentaDesdePanel(id);

            }
        );

    });

}



// ==============================
// APROBAR VENTA
// ==============================

async function aprobarVentaDesdePanel(id) {

    try {

        const response =
            await aprobarVenta(id);


        successMessage.textContent =
            'Venta aprobada correctamente.';

        successMessage.classList.remove(
            'd-none'
        );


        errorMessage.classList.add(
            'd-none'
        );


        // Recargar la tabla
        await cargarVentasPendientes();


    } catch (error) {

        console.error(
            'Error al aprobar venta:',
            error
        );


        errorMessage.textContent =
            'No se pudo aprobar la venta.';

        errorMessage.classList.remove(
            'd-none'
        );


        successMessage.classList.add(
            'd-none'
        );

    }

}


// ==============================
// EVENTOS DE APROBAR
// ==============================

document
    .querySelectorAll('[data-action="aprobar"]')
    .forEach(button => {

        button.addEventListener(
            'click',
            () => {

                const id =
                    button.dataset.id;

                aprobarVentaDesdePanel(id);

            }
        );

    });



// ==============================
// CONFIRMAR RECHAZO
// ==============================

confirmarRechazoButton.addEventListener(
    'click',
    async () => {

        const id =
            ventaIdRechazar.value;

        const motivo =
            motivoRechazo.value.trim();


        // Validar motivo

        if (!motivo) {

            modalError.textContent =
                'Debe ingresar un motivo de rechazo.';

            modalError.classList.remove(
                'd-none'
            );

            return;
        }


        try {


            const response =
                await rechazarVenta(
                    id,
                    motivo
                );




            // Cerrar modal

            rechazarModal.hide();


            // Mostrar mensaje

            successMessage.textContent =
                'Venta rechazada correctamente.';

            successMessage.classList.remove(
                'd-none'
            );


            errorMessage.classList.add(
                'd-none'
            );


            // Recargar tabla

            await cargarVentasPendientes();


        } catch (error) {

            console.error(
                'Error al rechazar venta:',
                error
            );


            modalError.textContent =
                'No se pudo rechazar la venta.';

            modalError.classList.remove(
                'd-none'
            );

        }

    }
);




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
// ACTUALIZAR
// ==============================

actualizarButton.addEventListener(
    'click',
    cargarVentasPendientes
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
// ABRIR MODAL
// ==============================
function abrirModalRechazo(id) {

    ventaIdRechazar.value = id;

    motivoRechazo.value = '';

    modalError.classList.add('d-none');

    rechazarModal.show();

}


// ==============================
// CARGAR AL ENTRAR
// ==============================

cargarVentasPendientes();
