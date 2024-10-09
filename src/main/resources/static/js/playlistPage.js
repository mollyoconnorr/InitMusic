function populateRenamePlaylistModal(modal, playlistName) {
    const modalTitle = modal.querySelector('.modal-title');
    modalTitle.textContent = 'Rename Playlist: ' + playlistName;

    const modalInput = modal.querySelector('.modal-body input');
    if (modalInput) {
        modalInput.value = playlistName;  // Pre-fill the input if needed
    }
}

// Hook into the modal's 'show.bs.modal' event
const renamePlaylistModal = document.getElementById('renamePlaylistModal');
renamePlaylistModal.addEventListener('show.bs.modal', function (event) {
    const button = event.relatedTarget;
    const playlistName = button.getAttribute('data-playlist-name'); // Get the playlist name
    populateRenamePlaylistModal(renamePlaylistModal, playlistName); // Pass it to the modal
});