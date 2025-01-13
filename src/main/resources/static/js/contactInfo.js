// Toggle the visibility of the contact info panel
function toggleContactInfoPanel() {
    const panel = document.getElementById('contact-info-panel');
    panel.style.display = panel.style.display === 'block' ? 'none' : 'block';
}

// Close the contact info panel
function closeContactInfoPanel() {
    const panel = document.getElementById('contact-info-panel');
    panel.style.display = 'none';
}
