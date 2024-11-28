document.getElementById('clienteForm').addEventListener('submit', function(event) {
    event.preventDefault();
    const usuarioId = document.getElementById('diUsuarioId').value;
    const nombre = document.getElementById('diNombre').value;
    const apellidos = document.getElementById('diApellidos').value;
    const email = document.getElementById('diEmail').value;
    const telefono = document.getElementById('diTelefono').value;
    const direccion = document.getElementById('diDireccion').value;
    const nuevaContrasena = document.getElementById('diPassword').value;
    const confirmarContrasena = document.getElementById('diConfirmPassword').value;
    if (nuevaContrasena && nuevaContrasena !== confirmarContrasena) {
        alert('Las contraseñas no coinciden. Por favor, intentalo nuevamente.');
        return;
    }
    const usuarioData = {
        id: usuarioId,
        nombre: nombre,
        apellidos: apellidos,
        email: email,
        telefono: telefono,
        direccion: direccion,
        password: nuevaContrasena ? nuevaContrasena : null 
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
    });
});
