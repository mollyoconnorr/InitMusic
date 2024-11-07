/**
 * This function takes all the song attributes from a song when its 'Add to playlist' button is pressed
 * And then adds them to their corresponding ids, which are used in a form so the song can eventually
 * be turned into a song object and then added to a playlist.
 * @param button Button of song that was pressed.
 */
function populateModal(button) {
    saveButtonData(button);

    popModalWithButtonData();
}

//Take the data from the given button and saves in session storage
function saveButtonData(button) {
    const buttonData = {
        songId: button.getAttribute('data-song-id'),
        songName: button.getAttribute('data-song-name'),
        songLength: button.getAttribute('data-song-length'),
        artistName: button.getAttribute('data-artist-name'),
        artistId: button.getAttribute('data-artist-id'),
        albumName: button.getAttribute('data-album-name'),
        albumId: button.getAttribute('data-album-id'),
        songImg: button.getAttribute('data-song-img'),
        songPreview: button.getAttribute('data-song-pre')
    };

    //Convert the object to a JSON string and store it
    sessionStorage.setItem('clickedButtonData', JSON.stringify(buttonData));
}

//Populate the ids gotten with the data from SessionStorage
function popModalWithButtonData() {
    //Parse data
    const buttonData = JSON.parse(sessionStorage.getItem('clickedButtonData'));

    if (buttonData) {
        console.log(buttonData.songId);
        document.getElementById('songID').value = buttonData.songId;
        document.getElementById('songName').value = buttonData.songName;
        document.getElementById('songLength').value = buttonData.songLength;
        document.getElementById('artistName').value = buttonData.artistName;
        document.getElementById('artistID').value = buttonData.artistId;
        document.getElementById('albumName').value = buttonData.albumName;
        document.getElementById('albumID').value = buttonData.albumId;
        document.getElementById('songImg').value = buttonData.songImg;
        document.getElementById('songPreview').value = buttonData.songPreview;
    } else {
        console.log("No button data found in sessionStorage.");
    }
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

//When createPlaylistForm is submitted, and set showModal to true in sessionStorage so modal will show back up
document.getElementById('createPlaylistForm').addEventListener('submit', function(event) {
    sessionStorage.setItem('showModal', 'true');
});

//Check if the flag is set in sessionStorage on page load, if so, load and populate modal
window.addEventListener('load', function() {
    if (sessionStorage.getItem('showModal') === 'true') {
        const myModal = new bootstrap.Modal(document.getElementById('addToPlaylistModal')); // Initialize modal
        popModalWithButtonData();
        myModal.show();

        // lear the flag after showing the modal
        sessionStorage.removeItem('showModal');
    }
});