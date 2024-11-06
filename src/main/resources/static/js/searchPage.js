/**
 * This function takes all the song attributes from a song when its 'Add to playlist' button is pressed
 * And then adds them to their corresponding ids, which are used in a form so the song can eventually
 * be turned into a song object and then added to a playlist.
 * @param button Button of song that was pressed.
 */
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


    //Get the hidden input fields in the modal
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


let popupWindow = null; // Global variable to store the pop-up window reference

//Opens a new popup window with the given url
function openPopup(url) {
    console.log("Opening URL:", url); // Log the URL

    // Close any existing pop-up before opening a new one
    closePopup();

    const width = 400;
    const height = 300;
    const left = (window.innerWidth / 2) - (width / 2);
    const top = (window.innerHeight / 2) - (height / 2);

    // Open the pop-up window and store the reference
    popupWindow = window.open(url, 'songPreview', `width=${width},height=${height},top=${top},left=${left},resizable=yes`);
}

//Closes popup window
function closePopup() {
    if (popupWindow) {
        popupWindow.close(); // Close the pop-up window
        popupWindow = null; // Clear the reference
    }
}

// Event listener for the preview links
document.querySelectorAll('.preview-link').forEach(link => {
    link.addEventListener('click', function(event) {
        event.preventDefault(); // Prevent the default anchor click behavior
        const url = this.dataset.url; // Get the URL from data attribute
        openPopup(url); // Call the popup function
    });
});

// Close pop-up window when the main window is clicked
document.addEventListener('click', function(event) {
    // Check if the clicked element is not one of the preview links
    const isPreviewLink = event.target.classList.contains('preview-link');

    if (!popupWindow.closed && !isPreviewLink) {
        closePopup(); // Close the pop-up if it's open and the click is outside of the links
    }
});