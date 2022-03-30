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
    $query = $mysqli->prepare("INSERT INTO histories (amount, rate, currency) VALUES (?, ?, ?)");
    $query->bind_param("iii", $amount, $rate, $currency);

    $response = [];

    if (strcmp($currency, "lbp") === 0) {
        $response["status"] = "200";
        $response["result"] = $amount / $rate;
        $query->execute();
    } else if (strcmp($currency, "usd") === 0) {
        $response["status"] = "200";
        $response["result"] = $amount * $rate;
        $query->execute();

    } else {
        $response["status"] = "404";
    }

    $json_response = json_encode($response);
    echo $json_response;
?>