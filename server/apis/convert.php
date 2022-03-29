<?php
    require(__DIR__ . '/../db/connection.php');

    include("lbp_rate.php");
    $key = "rate";
    $arr = json_decode($json_response, true);
    $rate = $arr[$key];

    
?>