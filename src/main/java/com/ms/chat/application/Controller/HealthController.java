package com.ms.chat.application.Controller;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
// @RequestMapping("")
public class HealthController {

    @GetMapping
    public String DefualRoute() {
        return """
                <!DOCTYPE html>
                      <html>
                      <head>
                          <title>Spring Boot Project</title>
                          <style>
                              body {
                                  font-family: Arial, sans-serif;
                                  background-color: #212121;
                                  color: #ffffff;
                                  text-align: center;
                                  padding: 50px;
                              }
                              h1 {
                                  color: #4CAF50;
                                  font-size: 3em;
                                  animation: fadeInText 5s infinite;
                              }
                              p {
                                  font-size: 18px;
                                  margin-top: 20px;
                              }
                              footer {
                                  margin-top: 20px;
                                  font-size: 14px;
                                  color: #777;
                              }
                              @keyframes fadeInText {
                                  0% { opacity: 0; transform: translateY(-20px); }
                                  50% { opacity: 1; transform: translateY(0); }
                                  100% { opacity: 0; transform: translateY(20px); }
                              }
                          </style>
                      </head>
                      <body>
                          <h1>Welcome to Manish's Spring Boot chat Application Project</h1>
                          <p>Your project is running on port <b>8080</b>.</p>
                          <p>Enjoy coding and have a great day!</p>

                          <footer>Powered by Spring Boot</footer>
                      </body>
                      </html>
                      """;
    }

    @GetMapping("/health")
    public String health() {
        return "All are doing well";
    }

}
