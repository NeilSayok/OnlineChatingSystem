<?php
require_once 'connection.php';

$chat_table = $_POST['chat_table'];

$sql = "SELECT * FROM `$chat_table` ORDER BY `$chat_table`.`sent_time` DESC";

$arr = array();

if ($result = mysqli_query($conn, $sql)) {
    while ($row = mysqli_fetch_array($result)) {
        $getdetailsQuery = "SELECT * FROM `user_table` WHERE `id` = '".$row['user_id']."'";
        $req = mysqli_fetch_array(mysqli_query($conn, $getdetailsQuery));
        $temp = array('id' => $row['user_id'] ,'name'=> $req['name'],'email'=> $req['email'],'chat_table'=> $req['chat_table'], 'msg_table' => $row['message_table']);
        array_push($arr, $temp);
    }
    echo json_encode($arr);
}
