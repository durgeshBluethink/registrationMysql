document.addEventListener('DOMContentLoaded', () => {
    const userDetailsDiv = document.getElementById('user-details');
    const userId = getQueryParam('userId') || sessionStorage.getItem('userId');

    console.log('User ID:', userId); // Log userId for debugging

    if (!userId) {
        alert('User ID is not set. Please log in.');
        showMessage('User ID is not set. Please log in.', 'error');
        return;
    }

    if (userDetailsDiv) {
        fetchUserDetails(userId);
    } else {
        console.error('Element with id "user-details" not found.');
    }
});

function getQueryParam(param) {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get(param);
}

async function fetchUserDetails(userId) {
    try {
        const response = await fetch(`http://localhost:8090/api/users/${userId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: 'include'
        });

        if (!response.ok) {
            throw new Error(`Network response was not ok: ${response.statusText}`);
        }

        const data = await response.json();
        console.log('User Details Response:', data); // Log response for debugging
        displayUserDetails(data);
        showMessage('User details loaded successfully!', 'success');

    } catch (error) {
        console.error('Error fetching details:', error);
        document.getElementById('user-details').innerHTML = `<p>Error fetching details: ${error.message}</p>`;
    }
}

function displayUserDetails(userDetails) {
    const userDetailsDiv = document.getElementById('user-details');
    if (userDetails) {
        userDetailsDiv.innerHTML = `
            <p><strong>Full Name:</strong> ${userDetails.fullName}</p>
            <p><strong>Email:</strong> ${userDetails.email}</p>
            <p><strong>City:</strong> ${userDetails.city}</p>
            <p><strong>Mobile Number:</strong> ${userDetails.mobileNumber}</p>
            <p><strong>Referrer:</strong> ${userDetails.referrer}</p>
            <p><strong>Payment Status:</strong> ${userDetails.paymentStatus}</p>
        `;

        // If payment is pending, show the payment link
        if (userDetails.paymentLink) {
            userDetailsDiv.innerHTML += `
                <p><a href="${userDetails.paymentLink}" class="btn btn-primary">Complete Payment</a></p>
            `;
        }

        // Add Referral ID and Referrer ID
        userDetailsDiv.innerHTML += `
            <p><strong>Referral ID:</strong> ${userDetails.referralId || 'Not Available'}</p>
            <p><strong>Referrer ID:</strong> ${userDetails.referrerId || 'Not Available'}</p>
        `;

        // Display referral tree if present
        if (userDetails.referralTree && userDetails.referralTree.length > 0) {
            const referralTreeContainer = document.createElement('div');
            referralTreeContainer.setAttribute('id', 'referralTree');
            userDetailsDiv.appendChild(referralTreeContainer);
            displayReferralTree(referralTreeContainer, userDetails.referralTree);
        }
    } else {
        userDetailsDiv.innerHTML = '<p>User details not found.</p>';
    }
}

function showMessage(message, type) {
    const messageContainer = document.getElementById('message-container');
    const messageElement = document.createElement('div');
    messageElement.textContent = message;
    messageElement.className = `alert alert-${type} animate__animated animate__fadeIn`;
    messageContainer.appendChild(messageElement);

    // Automatically hide the message after 5 seconds
    setTimeout(() => {
        messageElement.remove();
    }, 5000);
}

function displayReferralTree(container, referralTree) {
    if (referralTree && referralTree.length > 0) {
        const list = document.createElement('ul');
        referralTree.forEach(referral => {
            const listItem = document.createElement('li');
            listItem.textContent = `Referral: ${referral.fullName} (${referral.email}) - ${referral.paymentStatus}`;
            if (referral.referrals && referral.referrals.length > 0) {
                const subList = document.createElement('ul');
                displayReferralTree(subList, referral.referrals); // Recursive call
                listItem.appendChild(subList);
            }
            list.appendChild(listItem);
        });
        container.appendChild(list);
    } else {
        container.innerHTML = '<p>No referrals available.</p>';
    }
}

// Placeholder for Contact Info Panel toggling
function toggleContactInfoPanel() {
    const panel = document.getElementById('contact-info-panel');
    panel.style.display = panel.style.display === 'block' ? 'none' : 'block';
}

// Close Contact Info Panel
function closeContactInfoPanel() {
    document.getElementById('contact-info-panel').style.display = 'none';
}
