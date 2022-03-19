<?php
    require '../vendor/autoload.php';

    $dotenv = Dotenv\Dotenv::createImmutable(__DIR__ . "./../");
    $dotenv->load();
    echo $_ENV['db_name'];

    // $mysqli = new mysqli($db_host, $db_user, $db_pass, $db_name);

    // if (mysqli_connect_errno()) {
    //     die("Connection failed");
    // }
?>