document.querySelectorAll('.editButton').forEach(button => {
    button.addEventListener('click', function () {
        const row = this.closest('tr');
                   
        const PutvehiculoId = row.querySelector('td[data-id]').getAttribute('data-id');
        const Putcolor = row.querySelector('td[data-color]').getAttribute('data-color');
        const Putmarca = row.querySelector('td[data-marca]').getAttribute('data-marca');
        const Putmatricula = row.querySelector('td[data-matricula]').getAttribute('data-matricula');
        const Putpuertas = row.querySelector('td[data-puertas]').getAttribute('data-puertas');
        const Putautonomia_km = row.querySelector('td[data-autonomia_km]').getAttribute('data-autonomia_km');
        const Putpotencia_cv  = row.querySelector('td[data-potencia_cv]').getAttribute('data-potencia_cv');
        const Putprecio_dia = row.querySelector('td[data-precio_dia]').getAttribute('data-precio_dia');


        document.getElementById('PutdiVehiculoId').value = PutvehiculoId;
        document.getElementById('PutdiColor').value = Putcolor;
        document.getElementById('PutdiMarca').value = Putmarca;
        document.getElementById('PutdiMatricula').value =Putmatricula; 
        document.getElementById('PutdiPuertas').value = Putpuertas;
        document.getElementById('PutdiAutonomia_km').value = Putautonomia_km;
        document.getElementById('PutdiPotencia_cv').value = Putpotencia_cv;      
        document.getElementById('PutdiPrecio_dia').value = Putprecio_dia;
        
        document.getElementById('PutdialogVehiculo').showModal();
    });
});

document.getElementById('PutdialogVehiculo').addEventListener('submit', function(event) {
    event.preventDefault();

    const PutdiVehiculolId = document.getElementById('PutdiVehiculoId').value;
    const PutdiColor = document.getElementById('PutdiColor').value;    
    const PutdiMarca = document.getElementById('PutdiMarca').value;
    const PutdiMatricula = document.getElementById('PutdiMatricula').value;
    const PutdiPuertas = document.getElementById('PutdiPuertas').value;
    const PutdiAutonomia_km = document.getElementById('PutdiAutonomia_km').value;
    const PutdiPotencia_cv = document.getElementById('PutdiPotencia_cv').value;
    const PutdiPrecio_dia = document.getElementById('PutdiPrecio_dia').value;
  

    const vehiculoData = {
        id: PutdiVehiculolId,
        color: PutdiColor,
        matricula: PutdiMatricula,
        marca: PutdiMarca,
        puertas: PutdiPuertas,        
        autonomia_km: PutdiAutonomia_km,
        potencia_cv: PutdiPotencia_cv,
        precio_dia: PutdiPrecio_dia  
    };

    fetch('/api/vehiculos/update-vehiculo', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify(vehiculoData)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Error al actualizar el vehiculo');
        }
        return response.json();
    })
    .then(data => {
        if (data) {
            alert('Vehiculo actualizado con éxito.');
        } else {
            alert('No se pudo actualizar el vehiculo.');
        }
    })
    .catch(error => {
        console.error('Error al actualizar el perfil:', error);
        alert('Hubo un problema al intentar actualizar el perfil.');
    }).finally(() => {
        window.location.reload();
    });
});

document.querySelectorAll('.closeButtonModal').forEach(button => {
    button.addEventListener('click', (e) => {
        e.preventDefault();
        const closestDialog = button.closest('dialog');
        if (closestDialog) {
            closestDialog.close();
        }
    });
});


document.querySelectorAll('.deleteButton').forEach(button => {
    button.addEventListener('click', function () {
        const id = this.closest('tr').getAttribute('data-id');
        eliminarVehiculo(id);
    });
});

function eliminarVehiculo(id) {
    if (confirm('¿Estás seguro de que deseas eliminar este vehiculo?')) {
        fetch(`/api/vehiculos/eliminar/${id}`, {
            method: 'DELETE',
        })
        .then(() => {
            alert('Vehiculo eliminado correctamente');
        })
        .catch(error => console.error('Error al eliminar el vehiculo:', error))
        .finally(() => {
            window.location.reload();
        });
    }
}

document.getElementById('createButton').addEventListener('click', function() {
    document.getElementById('PostdialogVehiculo').showModal();
});

   

function registrarVehiculo() {
    const formData = {
        color: document.getElementById('Postcolor').value,
        marca: document.getElementById('Postmarca').value,
        matricula: document.getElementById('Postmatricula').value,
        puertas: document.getElementById('Postpuertas').value,
        autonomia_km: document.getElementById('Postautonomia_km').value,
        potencia_cv: document.getElementById('Postpotencia_cv').value,
        precio_dia: document.getElementById('Postprecio_dia').value,
    };
    
    fetch('/api/vehiculos/save-vehiculo', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData)
    })
    .then(response => response.json())
    .then(data => {
        if (data.message) {
            alert(data.message);
        } else {
           alert('Hubo un error al registrar el vehiculo.');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Hubo un error al registrar el vehiculo. CatchError');
    })
    .finally(() => {
        window.location.reload();
    });
}