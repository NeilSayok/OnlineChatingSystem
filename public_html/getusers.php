<?php
require_once 'connection.php';

if (isset($_GET['key']) and isset($_GET['id'])) {
    $key = $_GET['key'];
    $id = $_GET['id'];
    if (!isset($_GET) || empty($_GET)) {
        $query = 'SELECT * FROM `user_table`';
    } else {
        $query = 'SELECT * FROM `user_table` WHERE  NOT `id` = "'.$id.'" AND (`name` LIKE "%'.$key.'%" OR `email` LIKE "%'.$key.'%")';
        $result = mysqli_query($conn, $query);
        $res = array();

        while ($row = mysqli_fetch_assoc($result)) {
            array_push($res, array('id' => $row['id'],
                              'name' => $row['name'],
                              'email' => $row['email'],
                              'chat_table' => $row['chat_table']));
        }
        echo json_encode($res);
    }
}
