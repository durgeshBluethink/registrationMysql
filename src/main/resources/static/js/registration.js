// registration.js

// Get the form and response message elements
const form = document.getElementById('registrationForm');
const responseMessage = document.getElementById('responseMessage');

// Handle form submission
form.addEventListener('submit', async (e) => {
    e.preventDefault(); // Prevent default form submission

    // Get form data
    const userDto = {
        fullName: document.getElementById('fullName').value,
        email: document.getElementById('email').value,
        mobileNumber: document.getElementById('mobile').value,
        city: document.getElementById('city').value,
        password: document.getElementById('password').value,
        referrerId: document.getElementById('referrer').value || null // Set to null if referrer is not provided
    };

    try {
        // Send POST request to backend API
        const response = await fetch('http://localhost:8090/api/users/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(userDto),
        });

        const data = await response.json();

        // Handle success or error response
        if (response.ok) {
            responseMessage.innerHTML = `<p style="color: green;">${data.message}</p>`;
            form.reset(); // Reset form fields after successful registration
            window.location.href = 'login.html'; // Redirect to login page after successful registration
        } else {
            responseMessage.innerHTML = `<p style="color: red;">${data.message}</p>`;
        }

    } catch (error) {
        console.error('Error:', error);
        responseMessage.innerHTML = `<p style="color: red;">Registration failed. Please try again later.</p>`;
    }
});
