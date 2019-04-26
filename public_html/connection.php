<?php

define('servername', 'localhost');
define('user', 'your_db_user_name');
define('password', 'your_db_user_password');
define('database', 'your_db_name');

$conn = mysqli_connect(servername, user, password, database) or die("Connection failed: " . $conn->connect_error);

date_default_timezone_set('Asia/Kolkata');// Change this line acording to your time zone.

//https://www.php.net/manual/en/timezones.php for more details on time zone
