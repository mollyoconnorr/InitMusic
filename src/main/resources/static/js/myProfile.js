/**
 * Confirms if the user wants to delete their account and sends a POST request to delete the account if confirmed.
 */
function confirmDelete() {
    return confirm("Are you sure you want to delete your account? This action cannot be undone.");
}

/**
 * Shows a popup message that makes user confirm logout, and then fetches post for /logout and logs user out
 */
function confirmLogout() {
    // Show confirmation dialog
    const userConfirmed = confirm("Are you sure you want to log out?");

    // Log out only if the user confirms
    if (userConfirmed) {
        // Redirect to the /logout URL to trigger Spring Security's logout process
        window.location.href = '/logout';
    }
}

//add an event listener to the button
document.getElementById('logoutButton').addEventListener('click', logout);

