function populateDeleteSongModal(modal, songID, songName, playlistID, playlistName){
    const modalBody = modal.querySelector('.modal-body');
    modalBody.textContent = 'Are you sure you want to delete ' + songName + ' from ' +
    playlistName + '?';

    document.getElementById('playlistID').value = playlistID;
    document.getElementById('songID').value = songID;
    document.getElementById('playlistName').value = playlistName;
    document.getElementById('songName').value = songName;
}

const deleteSongModal = document.getElementById('deleteSongModal');
deleteSongModal.addEventListener('show.bs.modal', function (event) {
    const button = event.relatedTarget;
    const songID = button.getAttribute('data-song-id');
    const songName = button.getAttribute('data-song-name')
    const playlistID = button.getAttribute('data-playlist-id');
    const playlistName = button.getAttribute('data-playlist-name')

    populateDeleteSongModal(deleteSongModal, songID, songName, playlistID, playlistName);
});

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