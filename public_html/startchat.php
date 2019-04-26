<?php
require_once 'connection.php';

$senderId = $_POST['sId'];
$recID = $_POST['rId'];

$time = time();
$msg_table = 'msgt_'.$time;

$sql_S = "SELECT `chat_table` FROM `user_table` WHERE `id` = '$senderId'";
$sql_R = "SELECT `chat_table` FROM `user_table` WHERE `id` = '$recID'";

if ($res_S = mysqli_query($conn, $sql_S) and $res_R = mysqli_query($conn, $sql_R)) {
    $sender_T = mysqli_fetch_assoc($res_S)['chat_table'];
    $rec_T = mysqli_fetch_assoc($res_R)['chat_table'];


    if (chatPresent($senderId, $rec_T) or chatPresent($recID, $sender_T)) {
        echo "ChatPresent";
    } else {
        $create_table = "CREATE TABLE `id9391468_chatterbox`.`$msg_table` ( `from` INT NOT NULL , `msg` VARCHAR(10000) NOT NULL , `time` VARCHAR(100) NOT NULL ) ENGINE = InnoDB";
        if ($create_T = mysqli_query($conn, $create_table)) {
            $sql_S = "INSERT INTO `$sender_T`(`user_id`, `message_table`, `sent_time`) VALUES ('$recID','$msg_table','$time')";
            $sql_R = "INSERT INTO `$rec_T`(`user_id`, `message_table`, `sent_time`) VALUES ('$senderId','$msg_table','$time')";
            if ($res_S = mysqli_query($conn, $sql_S) and $res_R = mysqli_query($conn, $sql_R)) {
                echo $msg_table;
            } else {
                echo "dataEntryErr";
            }
        } else {
            echo "TableCreateErr";
        }
    }
} else {
    echo "usrErr";
}





function chatPresent($id, $table)
{
    $sql = "SELECT * from $table where `user_id` = '$id'";
    $res = mysqli_query($GLOBALS['conn'], $sql);
    $row = mysqli_fetch_array($res);
    if (count($row) > 0) {
        return true;
    } else {
        return false;
    }
}
