<?php
require_once 'connection.php';

$msg_table = $_POST['msg_table'];

//$msg_table = 'msgt_1556098428';

$sql = "SELECT * FROM `$msg_table` ORDER BY `$msg_table`.`time` DESC";

$arr = array();

if ($result = mysqli_query($conn, $sql)) {
    while ($row = mysqli_fetch_array($result)) {
        $t = date("h:i A", $row['time']);
        array_push($arr, array('from' => $row['from'],'message' => $row['msg'], 'time' => $t));
    }
    echo json_encode($arr);
}
