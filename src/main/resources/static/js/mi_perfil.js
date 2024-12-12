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
    
    const token_custom_foodspringapp = document.cookie.split('; ').find(row => row.startsWith('token_custom_foodspringapp=')) ?.split('=')[1];

    if (!token_custom_foodspringapp) {
        alert('No se encontró la cookie token_custom_foodspringapp. Por favor, asegúrate de estar autenticado.');
        return;
    }

    const tokenData = {
        token: token_custom_foodspringapp
    };
    
    fetch('/api', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify({ usuarioData, tokenData })
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
    });
});
