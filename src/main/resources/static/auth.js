document.addEventListener('DOMContentLoaded', () => {
    const isLoggedIn = sessionStorage.getItem('isLoggedIn') === 'true';

    if (!isLoggedIn) {
        alert('Please log in to access the payment page.');
        window.location.href = 'login.html'; // Redirect to login page if not logged in
    }
});
