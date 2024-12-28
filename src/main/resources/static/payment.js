document.addEventListener('DOMContentLoaded', () => {
    const paymentForm = document.getElementById('paymentForm');

    if (paymentForm) {
        paymentForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const amount = document.getElementById('amount').value;

            try {
                const response = await fetch('http://localhost:8090/api/payment/create', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ amount })
                });

                if (response.ok) {
                    const result = await response.json();
                    const options = {
                        "key": "rzp_test_AIEfgCrKyUEdo8", // Enter your Razorpay Key ID
                        "amount": result.amount, // Amount in currency subunits. Default currency is INR.
                        "currency": "INR",
                        "name": "MyApp",
                        "description": "Test Transaction",
                        "order_id": result.id, // Order ID obtained from backend
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
                            window.location.href = 'success.html'; // Redirect to success page after successful payment
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
