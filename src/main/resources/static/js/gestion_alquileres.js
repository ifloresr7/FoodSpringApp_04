const crearAlquilerDialog = document.getElementById("crearAlquilerDialog");
const createButton = document.querySelector(".createButton");
createButton.addEventListener("click", () => {
    crearAlquilerDialog.showModal();
});
const closeButton = document.querySelector(".closeButtonModal");
closeButton.addEventListener("click", () => {
    crearAlquilerDialog.close();
});

document.querySelectorAll('.editButton').forEach(button => {
    button.addEventListener('click', function () {
        const row = this.closest('tr');

        const alquilerId = row.getAttribute('data-id');
        const clienteId = row.children[1].textContent;
        const vehiculoId = row.children[2].textContent;
        const fechaInicio = row.children[3].textContent;
        const fechaFin = row.children[4].textContent;
        const precio = row.children[5].textContent;

        document.getElementById('alquilerId').value = alquilerId;
        document.getElementById('clienteId').value = clienteId;
        document.getElementById('vehiculoId').value = vehiculoId;
        document.getElementById('fechaInicio').value = fechaInicio;
        document.getElementById('fechaFin').value = fechaFin;
        document.getElementById('precio').textContent = precio;

        // Actualizar el precio por día y calcular el precio total
        const selectedOption = document.querySelector(`#vehiculoId option[value="${vehiculoId}"]`);
        if (selectedOption) {
            price = selectedOption.getAttribute('data-price');
            document.getElementById('precio').innerHTML = price + '€';
            calculatePrice();
        }

         // Asegurar cálculo del precio total
         dateinit = fechaInicio;
         dateend = fechaFin;
         calculatePrice();
        
        document.getElementById('crearAlquilerDialog').showModal();
    });
});

const formCrearAlquiler = document.getElementById("formCrearAlquiler");

formCrearAlquiler.addEventListener("submit", async (event) => {
    event.preventDefault();
    const alquilerId = document.getElementById("alquilerId").value;
    const alquilerData = {
        id: alquilerId || null,
        clienteId: document.getElementById("clienteId").value,
        vehiculoId: document.getElementById("vehiculoId").value,
        fechaInicio: document.getElementById("fechaInicio").value,
        fechaFin: document.getElementById("fechaFin").value,
        precio: document.getElementById("precio").textContent.replace('€', '')
    };
    const url = alquilerId ? '/api/alquileres/actualizar-alquiler' : '/api/alquileres/crear-alquiler';
    const method = alquilerId ? 'PUT' : 'POST';
    const token_custom_foodspringapp = document.cookie.split('; ').find(row => row.startsWith('token_custom_foodspringapp='))?.split('=')[1];
    if (!token_custom_foodspringapp) {
        alert('No se encontró la cookie token_custom_foodspringapp. Por favor, asegúrate de estar autenticado.');
        return;
    }
    const tokenData = {
        token: token_custom_foodspringapp
    };
    fetch(`${url}`, {
        method: method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ alquilerData, tokenData })
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(errorData => {
                alert(`${errorData.message}`);
                throw new Error(errorData.message);
            });
        }
        return response.json();
    })
    .then(data => {
        alert(data.message);
    })
    .catch(error => {
        alert('Se a producido un error:', error);
    })
    .finally(() => {
        window.location.reload();
    });
});


let price;
let dateinit;
let dateend;

document.getElementById('vehiculoId').addEventListener('change', function() {
    price = this.options[this.selectedIndex].getAttribute('data-price');
    document.getElementById('precio').innerHTML = price + '€';
    calculatePrice();
});

document.getElementById('fechaInicio').addEventListener('change', function() {
    dateinit = this.value;
    calculatePrice();
});

document.getElementById('fechaFin').addEventListener('change', function() {
    dateend = this.value;
    calculatePrice();
});

function calculatePrice(){
    if(!price || !dateinit || !dateend){
        return;
    }
    let startDate = new Date(dateinit);
    let endDate = new Date(dateend);
    if (endDate < startDate) {
        alert('La fecha de fin no puede ser anterior a la fecha de inicio.');
        document.getElementById('fechaFin').value = dateinit;
        endDate = startDate;
    }
    const differenceInTime = endDate - startDate;
    const differenceInDays = differenceInTime / (1000 * 3600 * 24) + 1;
    const totalPrice = differenceInDays * price;
    document.getElementById('precioTotal').innerHTML = totalPrice.toFixed(2) + '€';
}

function calculateUpdatedPrice(fechaInicio, fechaFin, price) {
    if (!fechaInicio || !fechaFin || !price) {
        return 0;
    }
    const startDate = new Date(fechaInicio);
    const endDate = new Date(fechaFin);

    if (endDate < startDate) {
        alert('La fecha de fin no puede ser anterior a la fecha de inicio.');
        document.getElementById('fechaFin').value = fechaInicio;
        return 0;
    }

    const differenceInTime = endDate - startDate;
    const differenceInDays = Math.ceil(differenceInTime / (1000 * 3600 * 24)) + 1;
    const totalPrice = differenceInDays * parseFloat(price);

    document.getElementById('precio').textContent = totalPrice.toFixed(2) + '€';
    return totalPrice.toFixed(2);
}

document.querySelectorAll('.deleteButton').forEach(button => {
    button.addEventListener('click', async function () {
        const row = this.closest('tr');
        const alquilerId = row.getAttribute('data-id');

        if (confirm(`¿Estás seguro de que deseas eliminar el alquiler con ID ${alquilerId}?`)) {
            try {
                const response = await fetch(`/api/alquileres/eliminar-alquiler/${alquilerId}`, {
                    method: 'DELETE',
                    headers: { 'Content-Type': 'application/json' },
                });

                if (response.ok) {
                    alert("Alquiler eliminado exitosamente.");
                    row.remove();
                } else {
                    alert("Error al eliminar el alquiler.");
                }
            } catch (error) {
                console.error("Error:", error);
                alert("Hubo un problema al comunicarse con el servidor.");
            }
        }
    });
});
