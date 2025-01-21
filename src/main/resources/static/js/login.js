// login.js

document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');
    const responseMessage = document.getElementById('responseMessage');

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

                let result;
                try {
                    result = await response.json();
                } catch (error) {
                    throw new Error('Failed to parse response');
                }

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
