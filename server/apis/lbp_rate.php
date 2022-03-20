<?php
    $year = intval(date('Y'));
    $month = intval(date('m'));
    $day = intval(date('d'));
    $hour = intval(date('H'));
    $ver = $year . $month . $day . $hour;

    $api_url = "https://lirarate.org/wp-json/lirarate/v2/omt?currency=LBP&_ver=t$ver";

    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $api_url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
    $output = curl_exec($ch);
    curl_close($ch);
    echo $output;
?>