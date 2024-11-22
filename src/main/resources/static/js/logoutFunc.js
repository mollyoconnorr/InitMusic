/**
 * Shows a popup message that makes user confirm logout, and then fetches post for /logout and logs user out
 */
function logout() {
    // Show confirmation dialog
    const userConfirmed = confirm("Are you sure you want to log out?");

    // log out only if the user confirms
    if (userConfirmed) {
        // Get CSRF token from the HTML meta tag
        const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');

        // Perform the fetch request to logout
        fetch('/logout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-Requested-With': 'XMLHttpRequest', // CSRF protection
                'X-CSRF-TOKEN': csrfToken // Add CSRF token to headers
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