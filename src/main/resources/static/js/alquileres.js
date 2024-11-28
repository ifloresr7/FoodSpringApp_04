let alquileresList;

async function obtenerListadoAlquileres(){
    fetch('/api/alquileres').then(response => response.json()).then(alquileres => {alquileresList = alquileres});
}

async function recargarTabla() {
    alquileresList.forEach(alquiler => {
            const row = `<tr data-id="${alquiler.id}">
                <td>${alquiler.id}</td>
                <td>${alquiler.nombre} ${alquiler.apellidos}</td>
                <td>${alquiler.email}</td>
                <td>${alquiler.telefono}</td>
                <td>${alquiler.direccion}</td>
                <td>
                    <button class="editButton">Editar</button>
                    <button class="deleteButton">Eliminar</button>
                </td>
            </tr>`;
            tableBody.insertAdjacentHTML('beforeend', row);
        });
        createFunctionalityButton();
}

