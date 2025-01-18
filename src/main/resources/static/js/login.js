// login.js

document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');
    const responseMessage = document.getElementById('responseMessage'); // Add this line to get the response message element

    if (loginForm) {
        loginForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const email = document.getElementById('loginEmail').value.trim();
            const password = document.getElementById('loginPassword').value.trim();

            try {
                const response = await fetch('http://localhost:8090/api/users/login', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ email, password }),
                    credentials: 'include'
                });

                const result = await response.json();

                // Handle success or error response
                if (response.ok) {
                    responseMessage.innerHTML = `<p style="color: green;">${result.message || 'Login successful!'}</p>`;
                    sessionStorage.setItem('isLoggedIn', 'true');
                    sessionStorage.setItem('userId', result.userId);
                    window.location.href = 'index.html'; // Redirect to homepage after successful login
                } else {
                    responseMessage.innerHTML = `<p style="color: red;">Login failed: ${result.message || 'Invalid credentials'}</p>`;
                }
            } catch (error) {
                responseMessage.innerHTML = `<p style="color: red;">An error occurred: ${error.message}</p>`;
            }
        });
    }
});
