document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');

    if (loginForm) {
        loginForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const email = document.getElementById('loginEmail').value;
            const password = document.getElementById('loginPassword').value;

            console.log('Sending login request with email:', email, 'and password:', password);

            try {
                const response = await fetch('http://localhost:8090/api/users/login', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ email, password }),
                    credentials: 'include'
                });

                if (response.ok) {
                    const result = await response.json();
                    alert(result.message);
                    sessionStorage.setItem('isLoggedIn', 'true');
                    sessionStorage.setItem('userId', result.userId); // Store userId
                    console.log('User ID stored in sessionStorage:', sessionStorage.getItem('userId')); // Log userId
                    window.location.href = 'index.html';
                } else {
                    const errorData = await response.json();
                    console.error('Error:', errorData.message);
                    alert('Login failed: ' + errorData.message);
                }
            } catch (error) {
                console.error('Error:', error);
                alert('An error occurred: ' + error.message);
            }
        });
    }
});
