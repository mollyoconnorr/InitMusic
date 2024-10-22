function goBack() {
    window.history.back();
}

function validatePassword() {
    var password = document.getElementById("newPassword").value;
    var confirmPassword = document.getElementById("confirmPassword").value;

    // Check if the passwords match
    if (password !== confirmPassword) {
        document.getElementById("passwordError").innerHTML = "Passwords do not match!";
        return false; // Prevent form submission
    } else {
        document.getElementById("passwordError").innerHTML = ""; // Clear error if passwords match
    }
    return true;
}