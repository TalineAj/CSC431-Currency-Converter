<?php
    // This is the get api that returns the rate in a json object
    include "lirarate.inc.php";
    $array_response;
    if ($rate == null) {
        $array_response = array("status" => 404);
    } else {
        $array_response = array("status" => 200, "rate" => $rate);
    }

    echo json_encode($array_response);
?>