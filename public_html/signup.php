<?php
require_once 'connection.php';

$inpemail = $_POST['email'];
$inppsw = $_POST['password'];
$inpname = $_POST['name'];
$chat_table = 'user_'.time();




$query = "SELECT * FROM `user_table` WHERE `email` = '".$inpemail."'";

if ($result = mysqli_query($conn, $query)) {
    $row = mysqli_fetch_array($result);
    if (count($row) <= 0) {
        $sql = "INSERT INTO `user_table`(`name` ,`email`, `password`, `chat_table`) VALUES ('$inpname','$inpemail','$inppsw','$chat_table')";
        $create_table = "CREATE TABLE `id9391468_chatterbox`.`$chat_table` ( `user_id` INT NOT NULL , `message_table` VARCHAR(1000) NOT NULL , `sent_time` BIGINT NOT NULL ) ENGINE = InnoDB";
        if (mysqli_query($conn, $sql) && mysqli_query($conn, $create_table)) {
            $idsql = "SELECT `id` FROM `user_table` WHERE `chat_table` = '$chat_table'";
            $r =  mysqli_query($conn, $idsql);
            $r = mysqli_fetch_assoc($r)['id'];
            $array = array('status' => 'done','chat_table'=> $chat_table , 'id' =>$r);
        } else {
            $array = array('status' => 'err','chat_table'=> $sql ,'id' => '');
        }
    } else {
        $array = array('status' => 'user_present','chat_table'=> '' ,'id' => '');
    }
    echo json_encode($array);
}
