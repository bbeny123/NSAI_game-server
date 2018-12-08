$(document).ready(function () {
    $('#data').DataTable();
});

function validatePassword() {
    var password = document.getElementById("password"), confirm_password = document.getElementById("confirm_password");
    if (password.value !== confirm_password.value) {
        confirm_password.setCustomValidity("Passwords Don't Match");
    } else {
        confirm_password.setCustomValidity('');
    }
}

function activateUsers() {
    activateMenu('users');
}

function activateLogin() {
    activateMenu('login');
}

function activateRegister() {
    activateMenu('register');
}

function activateMenu(active) {
    document.getElementById(active).classList.add('active');
}