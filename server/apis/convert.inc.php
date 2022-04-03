<?php
    // This is the post api that receives the amount
    // and currency and returns the result and updated rate from lirarate.

    // connecting to the database
    require __DIR__ . '/../db/config.inc.php';
    
    // Getting the $rate => LBP rate
    include "lirarate.inc.php";

    $amount = doubleval($_POST["amount"]);
    $currency = $_POST["currency"];
    $query = $mysqli->prepare("INSERT INTO histories (amount, rate, currency) VALUES (?, ?, ?)");
    $query->bind_param("dds", $amount, $rate, $currency);

    $array_response;

    if (strcmp($currency, "lbp") === 0) {
        $array_response= array("status" => 200, "result" => $amount / $rate, "rate" => $rate);
        $query->execute();
    } else if (strcmp($currency, "usd") === 0) {
        $array_response= array("status" => 200, "result" => $amount * $rate, "rate" => $rate);
        $query->execute();
    } else {
        $array_response= array("status" => 404);
    }
    
    echo json_encode($array_response);
?>