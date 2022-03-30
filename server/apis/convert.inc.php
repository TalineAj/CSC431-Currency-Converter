<?php
    // connecting to the database
    require __DIR__ . '/../db/config.inc.php';
    // Getting the json_response that includes the currency LBP rate
    include "rate.inc.php";

    $key = "rate";
    $arr = json_decode($json_response, true);
    // Extracting the rate from the array
    $rate = $arr[$key];

    $amount = $_POST["amount"];
    $currency = $_POST["currency"];

    $response = [];

    if(strcmp($currency, "lbp") === 0) {
        echo "This is lbp";
        $response["status"] = "200";
        $response["result"] = $amount / $rate;
    } else if(strcmp($currency, "usd") === 0) {
        echo "This is usd";
        $response["status"] = "200";
        $response["result"] = $amount * $rate;
    } else {
        $response["status"] = "404";
    }

    $query = $mysqli->prepare("INSERT INTO histories (amount, rate, currency) VALUES (?, ?, ?)");
    $query->bind_param("iii", $amount, $rate, $currency);
    $query->execute();

    $json_response = json_encode($response);
    echo $json_response;