<?php

    // Setting the timezone to Asia
    date_default_timezone_set('Asia/Beirut');

    // Extracting the year, month, day, and hour of the current timestamp
    $year = intval(date('Y'));
    $month = intval(date('m'));
    $day = intval(date('d'));
    $hour = intval(date('H'));

    // Concatinating the year, month, day, and hour in order
    // to put it at the end of the API url
    $ver = $year . $month . $day . $hour;
    $api_url = "https://lirarate.org/wp-json/lirarate/v2/omt?currency=LBP&_ver=t$ver";

    // Calling the api and storing the result in a json format
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $api_url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    $json_response = curl_exec($ch);
    curl_close($ch);

    // Decodes the $json_response and puts it in an stdClass Object
    $std_obj = json_decode($json_response);

    // Turns the $std_obj to an array
    // Turning the $json_response direclty to an array won't give the same format
    $array = json_decode(json_encode($std_obj), true);
    $length = count($array['omt']);

    $rate = null;

    if ($length >= 1) {
        $rate = $array['omt'][$length - 1][1];
    }
?>