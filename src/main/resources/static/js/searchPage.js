function populateModal(button) {
    const songId = button.getAttribute('data-song-id');
    const songName = button.getAttribute('data-song-name');
    const songLength = button.getAttribute('data-song-length');
    const artistName = button.getAttribute('data-artist-name');
    const artistId = button.getAttribute('data-artist-id');
    const albumName = button.getAttribute('data-album-name');
    const albumId = button.getAttribute('data-album-id');
    const songImg = button.getAttribute('data-song-img');
    const songPreview = button.getAttribute('data-song-pre');


    // Get the hidden input fields in the modal
    document.getElementById('songID').value = songId;
    document.getElementById('songName').value = songName;
    document.getElementById('songLength').value = songLength;
    document.getElementById('artistName').value = artistName;
    document.getElementById('artistID').value = artistId;
    document.getElementById('albumName').value = albumName;
    document.getElementById('albumID').value = albumId;
    document.getElementById('songImg').value = songImg;
    document.getElementById('songPreview').value = songPreview;

}