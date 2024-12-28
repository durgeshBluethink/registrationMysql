document.addEventListener('DOMContentLoaded', () => {
    const welcomeMessage = document.getElementById('welcome-message');
    const isLoggedIn = sessionStorage.getItem('isLoggedIn') === 'true';

    if (isLoggedIn) {
        const userName = sessionStorage.getItem('userName');
        if (userName) {
            welcomeMessage.textContent = `Welcome, ${userName}!`;
        } else {
            welcomeMessage.textContent = 'Welcome to MyApp';
        }
    } else {
        welcomeMessage.textContent = 'Welcome to MyApp';
    }
});
