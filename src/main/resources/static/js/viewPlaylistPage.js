//Populates the deleteSongModal (But could do any modal) with given data
function populateDeleteSongModal(modal, songID, songName, playlistID, playlistName) {
    const modalBody = modal.querySelector('.modal-body');
    modalBody.textContent = 'Are you sure you want to delete ' + songName + ' from ' +
        playlistName + '?';

    document.getElementById('playlistID').value = playlistID;
    document.getElementById('songID').value = songID;
    document.getElementById('playlistName').value = playlistName;
    document.getElementById('songName').value = songName;
}

// Adds event listener to deleteSongModal and populates it with data
const deleteSongModal = document.getElementById('deleteSongModal');
deleteSongModal.addEventListener('show.bs.modal', function (event) {
    const button = event.relatedTarget;
    const songID = button.getAttribute('data-song-id');
    const songName = button.getAttribute('data-song-name')
    const playlistID = button.getAttribute('data-playlist-id');
    const playlistName = button.getAttribute('data-playlist-name')

    populateDeleteSongModal(deleteSongModal, songID, songName, playlistID, playlistName);
});