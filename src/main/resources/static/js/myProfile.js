function confirmDelete() {
    return confirm("Are you sure you want to delete your account? This action cannot be undone.");
}

/**
 * Shows a popup message that makes user confirm logout, and then fetches post for /logout and logs user out
 */
function logout() {
    // Show confirmation dialog
    const userConfirmed = confirm("Are you sure you want to log out?");

    // log out only if the user confirms
    if (userConfirmed) {
        fetch('/logout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-Requested-With': 'XMLHttpRequest' // CSRF protection
            },
            credentials: 'include' // include cookies in the request
        })
            .then(response => {
                if (response.ok) {
                    // Redirect to the login page
                    window.location.href = '/login?logout';
                } else {
                    throw new Error('Logout failed');
                }
            })
            .catch(error => {
                console.error('Error during logout:', error);
            });
    }
}

//add an event listener to the button
document.getElementById('logoutButton').addEventListener('click', logout);

function confirmLogout() {
    return confirm("Are you sure you want to logout?");
}

