document.addEventListener('DOMContentLoaded', () => {
    fetch('header.html')
        .then(response => response.text())
        .then(data => {
            document.getElementById('header').innerHTML = data;
            loadUserActions();
        })
        .catch(error => console.error('Error loading header:', error));
});

function loadUserActions() {
    const isLoggedIn = sessionStorage.getItem('isLoggedIn') === 'true';
    const userId = sessionStorage.getItem('userId'); // Retrieve userId from sessionStorage

    console.log('isLoggedIn:', isLoggedIn);
    console.log('userId:', userId); // Log userId for debugging

    if (isLoggedIn && userId) {
        document.getElementById('login-nav').style.display = 'none';
        document.getElementById('register-nav').style.display = 'none';

        const paymentNav = document.getElementById('payment-nav');
        paymentNav.style.display = 'block';
        paymentNav.querySelector('a').setAttribute('href', 'payment.html?userId=' + userId); // Set userId in the payment link
        console.log('Payment link set to:', 'payment.html?userId=' + userId); // Log payment link for debugging

        document.getElementById('logout-nav').style.display = 'block';
        document.getElementById('logout').addEventListener('click', () => {
            sessionStorage.clear();
            window.location.href = 'index.html'; // Redirect to index page after logout
        });
    } else {
        document.getElementById('login-nav').style.display = 'block';
        document.getElementById('register-nav').style.display = 'block';
        document.getElementById('payment-nav').style.display = 'none';
        document.getElementById('logout-nav').style.display = 'none';
    }
    document.getElementById('about-nav').style.display = 'block';
}
