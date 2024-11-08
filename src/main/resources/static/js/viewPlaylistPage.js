//Populates the deleteSongModal (But could do any modal) with given data
function populateDeleteSongModal(modal, songID, songName, playlistID, playlistName){
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

let popupWindow = null; // Global variable to store the pop-up window reference

//Opens popup window with given url
function openPopup(url) {
    console.log("Opening URL:", url); // Log the URL

    // Close any existing pop-up before opening a new one
    closePopup();

    const width = 400;
    const height = 300;
    const left = (window.innerWidth / 2) - (width / 2);
    const top = (window.innerHeight / 2) - (height / 2);

    // Open the pop-up window with the base URL and store the reference
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