# CSC431-Currency-Converter

This is an Android app to convert amounts from USD to LBP and vise versa depending on the LBP rate that day.

## Description 📗

This app contains two pages. The first one is a landing page, and the second is the interactive page in which the user converts currencies. 
AndroidStudio is used to develop the frontend, and PHP for the backend.

## How to Install and Run the Project 💻

1. Clone this repository in xampp > htdocs
2. Start Apache & MySQL in XAMPP Control Panel
3. Go to http://localhost/phpmyadmin and create a Database called "converterdb"
4. Create a table called histories and in it create 3 attributes: amount (double), rate (double), and currency (varchar(255))
5. Create a .env file in the server folder and take a look at the environmental variables in .env.example and fill them in the .env 
6. Go to the client and run the android app and interact with it

## Credits 👨‍💻👩‍💻

<img align="left" alt="Java" width="23px" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/github/github-original.svg" style="vertical-align: middle; padding-right: 10px;" />

[@talineaj](https://github.com/TalineAj), [@kevinnammour](https://github.com/kevinnammour)
