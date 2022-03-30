<?php
    // connecting to the database
    require __DIR__ . '/../db/config.php';
    // Getting the json_response that includes the currency LBP rate
    include "rate.php";

    $key = "rate";
    $arr = json_decode($json_response, true);
    // Extracting the rate from the array
    $rate = $arr[$key];

    $amount = $_POST["amount"];
    $currency = $_POST["currency"];

    $query = $mysqli->prepare("INSERT INTO histories (amount, rate, currency) VALUES (?, ?, ?)");
    $query->bind_param("iii", $amount, $rate, $currency);
    $query->execute();

    $response = [];
    $response["status"] = "Sucess!";

    $json_response = json_encode($response);
    echo $json_response;