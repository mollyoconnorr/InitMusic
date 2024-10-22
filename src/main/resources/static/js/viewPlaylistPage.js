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

function confirmLogout() {
    return confirm("Are you sure you want to log out?");
}