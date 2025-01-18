// Fetch user details and display referral tree
async function fetchUserDetails(userId) {
    try {
        const response = await fetch(`http://localhost:8090/api/users/${userId}`);
        if (response.ok) {
            const userDetails = await response.json();
            displayUserDetails(userDetails);
        } else {
            alert('Failed to fetch user details');
        }
    } catch (error) {
        alert('An error occurred: ' + error.message);
    }
}

function displayUserDetails(userDetails) {
    // Display user details (e.g., fullName, email, city, etc.)
    document.getElementById('fullName').textContent = userDetails.fullName;
    document.getElementById('email').textContent = userDetails.email;
    document.getElementById('city').textContent = userDetails.city;
    document.getElementById('mobileNumber').textContent = userDetails.mobileNumber;
    document.getElementById('referrer').textContent = userDetails.referrer;
    document.getElementById('paymentStatus').textContent = userDetails.paymentStatus;

    // Display referral tree
    const referralTree = userDetails.referralTree;
    const referralTreeContainer = document.getElementById('referralTree');
    displayReferralTree(referralTreeContainer, referralTree);
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
