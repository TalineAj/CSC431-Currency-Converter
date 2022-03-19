<?php
    $ch = curl_init();

    curl_setopt($ch, CURLOPT_URL, "https://lirarate.org/");
    curl_setopt($ch, CURLOPT_HEADER, 0);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

    $result = curl_exec($ch);

    preg_match_all("/(.*?)\s(.*?)/", $result, $matches);
    // /1\sUSD\sat\s\d\d,\d\d\d\sLBP/

    print_r($matches);
    curl_close($ch);
?>