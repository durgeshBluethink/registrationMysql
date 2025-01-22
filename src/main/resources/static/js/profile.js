document.addEventListener('DOMContentLoaded', function () {
    const userId = '20a8cd52-0fa0-4412-91d8-a93b3fc6b777'; // Example userId, replace with the actual user ID
    fetchUserDetails(userId);
});

async function fetchUserDetails(userId) {
    try {
        console.log(`Fetching details for user ID: ${userId}`);
        const response = await fetch(`http://localhost:8090/api/users/${userId}`);
        if (response.ok) {
            const userDetails = await response.json();
            console.log('User details fetched:', userDetails);
            displayUserDetails(userDetails);
            showMessage('User details loaded successfully!', 'success');
        } else {
            const errorText = await response.text();
            console.error('Failed to fetch user details:', errorText);
            showMessage('Failed to fetch user details: ' + errorText, 'error');
        }
    } catch (error) {
        console.error('An error occurred:', error.message);
        showMessage('An error occurred: ' + error.message, 'error');
    }
}

function displayUserDetails(userDetails) {
    if (userDetails) {
        document.getElementById('fullName').textContent = userDetails.fullName;
        document.getElementById('email').textContent = userDetails.email;
        document.getElementById('city').textContent = userDetails.city;
        document.getElementById('mobileNumber').textContent = userDetails.mobileNumber;
        document.getElementById('referrer').textContent = userDetails.referrer;
        document.getElementById('paymentStatus').textContent = userDetails.isPaymentComplete ? "Payment Done" : "Payment Pending";

        // Display referral ID
        const referralIdElement = document.createElement('p');
        referralIdElement.innerHTML = `<strong>Referral ID:</strong> ${userDetails.referralId}`;
        document.getElementById('user-details').appendChild(referralIdElement);

        // Display referrer ID
        const referrerIdElement = document.createElement('p');
        referrerIdElement.innerHTML = `<strong>Referrer ID:</strong> ${userDetails.referrerId}`;
        document.getElementById('user-details').appendChild(referrerIdElement);

        // Display referral tree
        const referralTree = userDetails.referralTree;
        const referralTreeContainer = document.getElementById('referralTree');
        displayReferralTree(referralTreeContainer, referralTree);
    } else {
        console.error('No user details found');
    }
}


function displayReferralTree(container, referralTree) {
    if (referralTree.length === 0) {
        container.textContent = 'No referrals';
        return;
    }

    const ul = document.createElement('ul');
    referralTree.forEach(referral => {
        const li = document.createElement('li');
        li.textContent = `${referral.fullName} (${referral.email}) - ${referral.paymentStatus}`;
        ul.appendChild(li);
        if (referral.referrals.length > 0) {
            displayReferralTree(li, referral.referrals);
        }
    });
    container.appendChild(ul);
}

function showMessage(message, type) {
    const messageContainer = document.getElementById('message-container');
    const messageElement = document.createElement('div');
    messageElement.textContent = message;
    messageElement.className = `alert alert-${type === 'success' ? 'success' : 'danger'} animate__animated animate__fadeIn`;
    messageContainer.appendChild(messageElement);

    // Automatically hide the message after 5 seconds
    setTimeout(() => {
        messageElement.remove();
    }, 5000);
}

// Example contact info panel toggle functions
function toggleContactInfoPanel() {
    const panel = document.getElementById('contact-info-panel');
    if (panel.style.display === 'block') {
        panel.style.display = 'none';
    } else {
        panel.style.display = 'block';
    }
}

function closeContactInfoPanel() {
    const panel = document.getElementById('contact-info-panel');
    panel.style.display = 'none';
}
