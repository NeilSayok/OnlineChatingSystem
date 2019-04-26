<?php
require_once 'connection.php';

$inpemail = $_POST['email'];
$inppsw = $_POST['password'];

$query = "SELECT * FROM `user_table` WHERE `email` = '".$inpemail."'";

if ($result = mysqli_query($conn, $query)) {
    $row = mysqli_fetch_array($result);
    if (count($row) > 0) {
        if ($row['password'] == $inppsw) {
            $array = array('status' => 'OK','chat_table'=> $row['chat_table'] , 'name' =>  $row['name'], 'id' =>  $row['id']);
        } else {
            $array = array('status' => 'passMiss','chat_table'=> '', 'name' =>  '' , 'id' =>  '');
        }
    } else {
        $array = array('status' => 'credMiss','chat_table'=> '', 'name' =>  '', 'id' =>  '');
    }
    echo json_encode($array);
}
