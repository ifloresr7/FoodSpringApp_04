document.querySelectorAll('.editButton').forEach(button => {
    button.addEventListener('click', function () {
        const row = this.closest('tr');

        const PutusuarioId = row.querySelector('td[data-id]').getAttribute('data-id');
        const Putnombre = row.querySelector('td[data-nombre]').getAttribute('data-nombre');
        const Putapellidos = row.querySelector('td[data-apellidos]').getAttribute('data-apellidos');
        const Putdni = row.querySelector('td[data-dni]').getAttribute('data-dni');
        const Putemail = row.querySelector('td[data-email]').getAttribute('data-email');
        const Putrole = row.querySelector('td[data-role]').getAttribute('data-role');
        const Puttelefono = row.querySelector('td[data-telefono]').getAttribute('data-telefono');
        const Putdireccion = row.querySelector('td[data-direccion]').getAttribute('data-direccion');

        document.getElementById('PutdiUsuarioId').value = PutusuarioId;
        document.getElementById('PutdiNombre').value = Putnombre;
        document.getElementById('PutdiApellidos').value = Putapellidos;
        document.getElementById('PutdiDNI').value = Putdni;
        document.getElementById('PutdiEmail').value = Putemail;
        document.getElementById('PutdiRole').value = Putrole;
        document.getElementById('PutdiTelefono').value = Puttelefono;
        document.getElementById('PutdiDireccion').value = Putdireccion;

        document.getElementById('PutdiPassword').value = '';
        document.getElementById('PutdiConfirmPassword').value = '';

        document.getElementById('PutdialogUsuario').showModal();
    });
});

document.getElementById('PutdialogUsuario').addEventListener('submit', function(event) {
    event.preventDefault();

    const PutdiUsuarioId = document.getElementById('PutdiUsuarioId').value;
    const PutdiNombre = document.getElementById('PutdiNombre').value;
    const PutdiApellidos = document.getElementById('PutdiApellidos').value;
    const PutdiDNI = document.getElementById('PutdiDNI').value;
    const PutdiEmail = document.getElementById('PutdiEmail').value;
    const PutdiRole = document.getElementById('PutdiRole').value;
    const PutdiTelefono = document.getElementById('PutdiTelefono').value;
    const PutdiDireccion = document.getElementById('PutdiDireccion').value;
    const PutdiPassword = document.getElementById('PutdiPassword').value;
    const PutdiConfirmPassword = document.getElementById('PutdiConfirmPassword').value;

    if (PutdiPassword && PutdiPassword !== PutdiConfirmPassword) {
        alert('Las contraseñas no coinciden. Por favor, intentalo nuevamente.');
        return;
    }

    const usuarioData = {
        id: PutdiUsuarioId,
        nombre: PutdiNombre,
        apellidos: PutdiApellidos,
        dni: PutdiDNI,
        email: PutdiEmail,
        role: PutdiRole,
        telefono: PutdiTelefono,
        direccion: PutdiDireccion,
        password: PutdiPassword ? PutdiConfirmPassword : null
    };

    fetch('/api/usuarios/update-client', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify(usuarioData)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Error al actualizar el perfil');
        }
        return response.json();
    })
    .then(data => {
        if (data) {
            alert('Perfil actualizado con éxito.');
        } else {
            alert('No se pudo actualizar el perfil.');
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
        eliminarUsuario(id);
    });
});

function eliminarUsuario(id) {
    if (confirm('¿Estás seguro de que deseas eliminar este usuario?')) {
        fetch(`/api/usuarios/eliminar/${id}`, {
            method: 'DELETE',
        })
        .then(() => {
            alert('Usuario eliminado correctamente');
        })
        .catch(error => console.error('Error al eliminar el usuario:', error))
        .finally(() => {
            window.location.reload();
        });
    }
}

document.getElementById('createButton').addEventListener('click', function() {
    document.getElementById('PostdialogUsuario').showModal();
});

function registrarUsuario() {
    const formData = {
        nombre: document.getElementById('Postnombre').value,
        apellidos: document.getElementById('Postapellidos').value,
        dni: document.getElementById('Postdni').value,
        email: document.getElementById('Postemail').value,
        role: document.getElementById('Postrole').value,
        telefono: document.getElementById('Posttelefono').value,
        direccion: document.getElementById('Postdireccion').value,
        password: document.getElementById('Postpassword').value,
        confirmPassword: document.getElementById('PostconfirmPassword').value
    };
    if (formData.password !== formData.confirmPassword) {
        document.getElementById('error-message').style.display = 'block';
        document.getElementById('error-message').textContent = 'Las contraseñas no coinciden.';
        return;
    }
    fetch('/api/usuarios/save-client', {
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
            document.getElementById('error-message').style.display = 'block';
            document.getElementById('error-message').textContent = 'Hubo un error al registrar el usuario.';
        }
    })
    .catch(error => {
        console.error('Error:', error);
        document.getElementById('error-message').style.display = 'block';
        document.getElementById('error-message').textContent = 'Hubo un error al registrar el usuario.';
    })
    .finally(() => {
        window.location.reload();
    });
}