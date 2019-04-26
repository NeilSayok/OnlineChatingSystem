<?php
require_once 'connection.php';

$sId = $_POST['sId'];
$rId = $_POST['rId'];
$msgTable = $_POST['msg_table'];
$message  = $_POST['msg'];

$time = time();

$sql_sender_chat_table = "SELECT `chat_table` FROM `user_table` WHERE `id` = '$sId'";
$sql_reciever_chat_table = "SELECT `chat_table` FROM `user_table` WHERE `id` = '$rId'";


if ($res_S = mysqli_query($conn, $sql_sender_chat_table) and $res_R = mysqli_query($conn, $sql_reciever_chat_table)) {
    $sender_T = mysqli_fetch_assoc($res_S)['chat_table'];
    $rec_T = mysqli_fetch_assoc($res_R)['chat_table'];

    $writeMsg = "INSERT INTO `$msgTable`(`from`, `msg`, `time`) VALUES ('$sId','$message','$time')";
    $updateSenderTable = "UPDATE `$sender_T` SET `sent_time`= '$time' WHERE `message_table` = '$msgTable'";
    //echo $updateSenderTable;
    $updateRecieverTable = "UPDATE `$rec_T` SET `sent_time`= '$time' WHERE `message_table` = '$msgTable'";
    //echo $updateRecieverTable;

    if (mysqli_query($conn, $writeMsg)) {
        if (mysqli_query($conn, $updateSenderTable) and mysqli_query($conn, $updateRecieverTable)) {
            echo "Sent";
        } else {
            echo "Not sent";
        }
    } else {
        echo "Not Sent";
    }
}
