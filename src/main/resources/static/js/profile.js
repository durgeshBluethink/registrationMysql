const userDetailsDiv = document.getElementById('user-details');

if (userDetailsDiv) {
    const userId = sessionStorage.getItem('userId');
    console.log('User ID:', userId);

    if (userId) {
        fetch(`http://localhost:8080/api/users/${userId}`)
            .then(response => response.json())
            .then(data => {
                console.log('User Details Response:', data);
                if (data.error) {
                    userDetailsDiv.innerHTML = `<p>Error: ${data.error}</p>`;
                } else {
                    userDetailsDiv.innerHTML = `
                        <p><strong>Name:</strong> ${data.fullName}</p>
                        <p><strong>Email:</strong> ${data.email}</p>
                        <p><strong>City:</strong> ${data.city}</p>
                        <p><strong>Mobile:</strong> ${data.mobileNumber}</p>
                        <p><strong>Referrer:</strong> ${data.referrer}</p>
                        <p><strong>Payment Status:</strong> ${data.paymentStatus}</p>
                    `;
                }
            })
            .catch(error => {
                console.error('Error fetching details:', error);
                userDetailsDiv.innerHTML = `<p>Error fetching details: ${error.message}</p>`;
            });
    } else {
        userDetailsDiv.innerHTML = '<p>Please log in to view your details.</p>';
    }
} else {
    console.error('Element with id "user-details" not found.');
}
