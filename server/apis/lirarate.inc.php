<?php
    // Extracting the year, month, day, and hour of the current timestamp
    $year = intval(date('Y'));
    $month = intval(date('m'));
    $day = intval(date('d'));
    $hour = intval(date('H'));

    // Concatinating the year, month, day, and hour in order
    // to put it at the end of the API url
    $ver = $year . $month . $day . $hour;

    $api_url = "https://lirarate.org/wp-json/lirarate/v2/omt?currency=LBP&_ver=t$ver";

    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $api_url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    $json_response = curl_exec($ch);
    curl_close($ch);

    // Decodes the json object and puts it in an stdClass Object
    $array = json_decode($json_response);
    $value;
    foreach ($array as $key => $v) {
        $value = $v;
    }

    // Brings the last object in the array
    $last_item = end($value);

    // Fetches the rate at index 1: Array ( [0] => 1648623462000 [1] => 24100 )
    $rate = intval($last_item[1]);
    $array_response = array("rate" => $rate);
    $json_response = json_encode($array_response);
?>