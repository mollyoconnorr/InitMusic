/**
 * Populate the RenamePlaylistModal with the playlist name that will be deleted
 * @param modal Modal to use.
 * @param playlistName Name of playlist
 */
function populateRenamePlaylistModal(modal, playlistName) {
    const modalTitle = modal.querySelector('.modal-title');
    modalTitle.textContent = 'Rename Playlist: ' + playlistName;

    const modalInput = modal.querySelector('.modal-body input');
    if (modalInput) {
        modalInput.value = playlistName;  // Pre-fill the input if needed
    }
}

/**
 * Populate the DeletePlaylistModal with the playlist name that will be deleted
 * @param modal Modal to use.
 * @param playlistName Name of playlist
 */
function populateDeletePlaylistModal(modal, playlistName){
    const modalBody = modal.querySelector('.modal-body');
    modalBody.textContent = 'Are you sure you want to delete ' + playlistName + '?';
}

//Use the modal's 'show.bs.modal' event
const renamePlaylistModal = document.getElementById('renamePlaylistModal');
renamePlaylistModal.addEventListener('show.bs.modal', function (event) {
    const button = event.relatedTarget;
    const playlistName = button.getAttribute('data-playlist-name'); // Get the playlist name
    populateRenamePlaylistModal(renamePlaylistModal, playlistName); // Pass it to the modal
});

const deletePlaylistModal = document.getElementById('deletePlaylistModal');
deletePlaylistModal.addEventListener('show.bs.modal',function (event){
   const button = event.relatedTarget;
   const playlistName = button.getAttribute('data-playlist-name');
   populateDeletePlaylistModal(deletePlaylistModal,playlistName);
});