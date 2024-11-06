/**
 * Populate the RenamePlaylistModal with the playlist name that will be renamed
 * @param modal Modal to use.
 * @param playlistName Name of playlist
 * @param playlistID ID of playlist
 */
function populateRenamePlaylistModal(modal, playlistName, playlistID) {
    const modalTitle = modal.querySelector('.modal-title');
    modalTitle.textContent = 'Rename ' + playlistName;

    const modalInput = modal.querySelector('.modal-body input');
    if (modalInput) {
        //Prefill input
        modalInput.value = playlistName;
    }

    document.getElementById('playlistID').value = playlistID;

    //Add CSRF token to the form
    addCsrfTokenToForm(modal);
}

/**
 * Populate the DeletePlaylistModal with the playlist name that will be deleted
 * @param modal Modal to use.
 * @param playlistName Name of playlist
 * @param playlistID ID of playlist to delete
 */
function populateDeletePlaylistModal(modal, playlistName, playlistID) {
    const modalBody = modal.querySelector('.modal-body');
    modalBody.textContent = 'Are you sure you want to delete ' + playlistName + '?';

    document.getElementById('deletePlaylistID').value = playlistID;
    document.getElementById('deletePlaylistName').value = playlistName;
}

/**
 * Add CSRF token to the form in the modal
 * @param modal Modal element
 */
function addCsrfTokenToForm(modal) {
    const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const form = modal.querySelector('form');

    // If the form exists and the token is present, add the CSRF token as a hidden input
    if (form && token) {
        let input = form.querySelector('input[name="_csrf"]');
        if (!input) {
            input = document.createElement('input');
            input.type = 'hidden';
            input.name = '_csrf';
            form.appendChild(input);
        }
        input.value = token;
    }
}


// Use the modal's 'show.bs.modal' event
const renamePlaylistModal = document.getElementById('renamePlaylistModal');
renamePlaylistModal.addEventListener('show.bs.modal', function (event) {
    const button = event.relatedTarget;
    const playlistName = button.getAttribute('data-playlist-name'); // Get the playlist name
    const playlistID = button.getAttribute('data-playlist-id');
    populateRenamePlaylistModal(renamePlaylistModal, playlistName, playlistID); // Pass it to the modal
});

//Populates delete playlist modal
const deletePlaylistModal = document.getElementById('deletePlaylistModal');
deletePlaylistModal.addEventListener('show.bs.modal', function (event) {
    const button = event.relatedTarget;
    const playlistName = button.getAttribute('data-playlist-name');
    const playlistID = button.getAttribute('data-playlist-id');
    populateDeletePlaylistModal(deletePlaylistModal, playlistName, playlistID);
});

//Adds event listener to playlist Containers so on double click, a new page opens with the songs inside the playlist
const playlistContainer = document.getElementsByClassName('list-group-item-container');
for (let i = 0; i < playlistContainer.length; i++) {
    playlistContainer[i].addEventListener('dblclick', function () {
        //Find the button inside this container
        const button = playlistContainer[i].querySelector('#renameBtn');

        //Get the data-playlist-name and data-playlist-id attributes from the button
        const playlistName = button.getAttribute('data-playlist-name');
        const playlistID = button.getAttribute('data-playlist-id');

        //Check if the attributes were correctly retrieved
        if (playlistName && playlistID) {
            //Redirect to a URL, including the playlist ID and name as query parameters
            window.location.href = `/viewPlaylist/${playlistID}`;
        } else {
            console.log('Playlist name or ID is missing');
        }
    });
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
