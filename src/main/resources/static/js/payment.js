document.addEventListener('DOMContentLoaded', () => {
    const paymentForm = document.getElementById('paymentForm');

    function getQueryParam(param) {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get(param);
    }

    const userId = getQueryParam('userId'); // Retrieve userId from URL
    console.log('userId from URL:', userId); // Log userId for debugging

    if (paymentForm) {
        paymentForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const amount = document.getElementById('amount').value;

            try {
                const response = await fetch('http://localhost:8090/api/payment/create?userId=' + userId, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ amount })
                });

                if (response.ok) {
                    const result = await response.json();
                    const options = {
                        "key": "rzp_test_AIEfgCrKyUEdo8",
                        "amount": result.amount,
                        "currency": "INR",
                        "name": "MyApp",
                        "description": "Test Transaction",
                        "order_id": result.id,
                        "handler": async function (response) {
                            await fetch('http://localhost:8090/api/payment/update', {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/json'
                                },
                                body: JSON.stringify({
                                    orderId: result.id,
                                    paymentId: response.razorpay_payment_id,
                                    status: 'success'
                                })
                            });
                            window.location.href = 'static/success.html';
                        },
                        "prefill": {
                            "name": "Your Name",
                            "email": "Your Email",
                            "contact": "Your Contact Number"
                        },
                        "theme": {
                            "color": "#3399cc"
                        }
                    };
                    const rzp1 = new Razorpay(options);
                    rzp1.open();
                } else {
                    const errorData = await response.text();
                    console.error('Error:', errorData);
                    alert('Payment failed: ' + errorData);
                }
            } catch (error) {
                console.error('Error:', error);
                alert('An error occurred: ' + error.message);
            }
        });
    }
});
