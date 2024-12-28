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

    if (isLoggedIn) {
        document.getElementById('login-nav').style.display = 'none';
        document.getElementById('register-nav').style.display = 'none';
        document.getElementById('payment-nav').style.display = 'block';
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
}
