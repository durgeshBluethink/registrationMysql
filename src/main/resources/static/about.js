document.addEventListener('DOMContentLoaded', () => {
    const userDetails = document.getElementById('user-details');
    const isLoggedIn = sessionStorage.getItem('isLoggedIn') === 'true';

    if (isLoggedIn) {
        const userName = sessionStorage.getItem('userName');
        const userEmail = sessionStorage.getItem('userEmail');
        const userCity = sessionStorage.getItem('userCity');
        const userMobile = sessionStorage.getItem('userMobile');
        const userReferrer = sessionStorage.getItem('userReferrer');

        if (userName && userEmail && userCity && userMobile && userReferrer) {
            userDetails.innerHTML = `
                <p><strong>Name:</strong> ${userName}</p>
                <p><strong>Email:</strong> ${userEmail}</p>
                <p><strong>City:</strong> ${userCity}</p>
                <p><strong>Mobile:</strong> ${userMobile}</p>
                <p><strong>Referrer:</strong> ${userReferrer}</p>
            `;
        } else {
            userDetails.innerHTML = '<p>Your details are not available. Please log in again to view your details.</p>';
        }
    } else {
        userDetails.innerHTML = '<p>Please log in to view your details.</p>';
    }
});
