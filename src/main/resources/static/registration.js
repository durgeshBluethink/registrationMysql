document.addEventListener('DOMContentLoaded', () => {
    const registrationForm = document.getElementById('registrationForm');

    if (registrationForm) {
        registrationForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const fullName = document.getElementById('fullName').value;
            const email = document.getElementById('email').value;
            const mobileNumber = document.getElementById('mobile').value;
            const city = document.getElementById('city').value;
            const password = document.getElementById('password').value;
            const referrer = document.getElementById('referrer').value;

            try {
                const response = await fetch('http://localhost:8090/api/users/register', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ fullName, email, mobileNumber, city, password, referrer })
                });

                if (response.ok) {
                    window.location.href = 'login.html'; // Redirect to login page after successful registration
                } else {
                    const errorData = await response.text();
                    alert('Registration failed: ' + errorData);
                }
            } catch (error) {
                alert('An error occurred: ' + error.message);
            }
        });
    }
});
