<?php

define('servername', 'localhost');
define('user', 'id9391468_admin_chatterbox');
define('password', 'Mypassword1#');
define('database', 'id9391468_chatterbox');

$conn = mysqli_connect(servername, user, password, database) or die("Connection failed: " . $conn->connect_error);

date_default_timezone_set('Asia/Kolkata');
