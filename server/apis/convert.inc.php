<?php
    // This is the post api that gets the amount
    // and currency and returns the result and rate.

    // connecting to the database
    require __DIR__ . '/../db/config.inc.php';
    
    // Getting the json_response that includes the currency LBP rate
    include "lirarate.inc.php";

    $amount = $_POST["amount"];
    $currency = $_POST["currency"];
    echo $currency;
    $query = $mysqli->prepare("INSERT INTO histories (amount, rate, currency) VALUES (?, ?, ?)");
    $query->bind_param("iis", $amount, $rate, $currency);

    $response = [];

    if (strcmp($currency, "lbp") === 0) {
        $response["status"] = "200";
        $response["result"] = $amount / $rate;
        $response["rate"] = $rate;
        $query->execute();
    } else if (strcmp($currency, "usd") === 0) {
        $response["status"] = "200";
        $response["result"] = $amount * $rate;
        $response["rate"] = $rate;
        $query->execute();

    } else {
        $response["status"] = "404";
    }

    $json_response = json_encode($response);
    echo $json_response;
?>