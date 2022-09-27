## SJSU CS249 PA1
### Table of Contents
  - [General Info](#general-info)
  - [Files](#files)
  - [Execution](#execution)
  - [Demonstration](#demonstration)
### General Info
1. **Author**: Liqiang (Leo) Mei, 010410140
2. **Description**: A HTTP web server in which listens for TCP connections on a socket and responses to clients' HTTP/1.0 and/or HTTP/1.1 requests. It only supports GET method; when it receives a request, it will look the requested file in folder called document root. Upon success, it will send a HTTP OK message along with the contents of that file to client. Otherwise, it will send the appropriate HTTP error response. The server could concurrently accept and handle as many requests as possible with the help of Java threads. However, a TCP connection will close in five (5) minutes with no new HTTP/1.1 request from the client if the total number of connections are less or equal to CPU cores. If more than CPU cores but less than twice of cores, it can idle for three (3) minutes. The maximum allowance of idling is capped at one (1) minute when the number of connections is equal to or exceeds twice of cores.
### Files
1. Makefile
2. Request.java
3. Response.java
4. Server.java
5. Utils.java
6. README.md
### Execution
```
make clean
make
java Server -document_root "absolute_path_to_your_web_server_files_folder" -port "port_number"
```
### Demonstration
1. The web server uses HTTP/1.0 by default. It closes the TCP connection once it had sent a response. Client can request it to keep the connection alive for up 5 minutes.
![http/1.1](https://drive.google.com/uc?export=view&id=1DEsZ-58mq0UDefvtap7dl_0--W28b3P7)

A snipet taken from terminal output of the server can also prove HTTP/1.1 is used; the connection, which uses port 63366, was responsible of handling two requests: *GET / HTTP/1.1* and *GET /aspis/css/foundation.css HTTP/1.1*. 
```
Total Threads: 2 Thread 13 Socket[addr=/0:0:0:0:0:0:0:1,port=63366,localport=8888] Client: GET / HTTP/1.1
Total Threads: 3 Thread 13 Socket[addr=/0:0:0:0:0:0:0:1,port=63366,localport=8888] Client: Host: localhost:8888
Total Threads: 3 Thread 13 Socket[addr=/0:0:0:0:0:0:0:1,port=63366,localport=8888] Client: Connection: keep-alive
Total Threads: 3 Thread 13 Socket[addr=/0:0:0:0:0:0:0:1,port=63366,localport=8888] Client: sec-ch-ua: "Google Chrome";v="105", "Not)A;Brand";v="8", "Chromium";v="105"
Total Threads: 3 Thread 13 Socket[addr=/0:0:0:0:0:0:0:1,port=63366,localport=8888] Client: sec-ch-ua-mobile: ?0
Total Threads: 3 Thread 13 Socket[addr=/0:0:0:0:0:0:0:1,port=63366,localport=8888] Client: sec-ch-ua-platform: "Windows"
Total Threads: 3 Thread 13 Socket[addr=/0:0:0:0:0:0:0:1,port=63366,localport=8888] Client: Upgrade-Insecure-Requests: 1
Total Threads: 3 Thread 13 Socket[addr=/0:0:0:0:0:0:0:1,port=63366,localport=8888] Client: User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36
Total Threads: 3 Thread 13 Socket[addr=/0:0:0:0:0:0:0:1,port=63366,localport=8888] Client: Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9
Total Threads: 3 Thread 13 Socket[addr=/0:0:0:0:0:0:0:1,port=63366,localport=8888] Client: Sec-Fetch-Site: none
Total Threads: 3 Thread 13 Socket[addr=/0:0:0:0:0:0:0:1,port=63366,localport=8888] Client: Sec-Fetch-Mode: navigate
Total Threads: 3 Thread 13 Socket[addr=/0:0:0:0:0:0:0:1,port=63366,localport=8888] Client: Sec-Fetch-User: ?1
Total Threads: 3 Thread 13 Socket[addr=/0:0:0:0:0:0:0:1,port=63366,localport=8888] Client: Sec-Fetch-Dest: document
Total Threads: 3 Thread 13 Socket[addr=/0:0:0:0:0:0:0:1,port=63366,localport=8888] Client: Accept-Language: en-US,en;q=0.9
Total Threads: 3 Thread 13 Socket[addr=/0:0:0:0:0:0:0:1,port=63366,localport=8888] Client: Accept-Encoding: gzip, deflate
Total Threads: 3 Thread 13 Socket[addr=/0:0:0:0:0:0:0:1,port=63366,localport=8888] Client: GET /aspis/css/foundation.css HTTP/1.1
Total Threads: 3 Thread 13 Socket[addr=/0:0:0:0:0:0:0:1,port=63366,localport=8888] Client: Host: localhost:8888
Total Threads: 3 Thread 13 Socket[addr=/0:0:0:0:0:0:0:1,port=63366,localport=8888] Client: Connection: keep-alive
Total Threads: 3 Thread 13 Socket[addr=/0:0:0:0:0:0:0:1,port=63366,localport=8888] Client: sec-ch-ua: "Google Chrome";v="105", "Not)A;Brand";v="8", "Chromium";v="105"
Total Threads: 3 Thread 13 Socket[addr=/0:0:0:0:0:0:0:1,port=63366,localport=8888] Client: sec-ch-ua-mobile: ?0
Total Threads: 3 Thread 13 Socket[addr=/0:0:0:0:0:0:0:1,port=63366,localport=8888] Client: User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36
Total Threads: 3 Thread 13 Socket[addr=/0:0:0:0:0:0:0:1,port=63366,localport=8888] Client: sec-ch-ua-platform: "Windows"
Total Threads: 3 Thread 13 Socket[addr=/0:0:0:0:0:0:0:1,port=63366,localport=8888] Client: Accept: text/css,*/*;q=0.1
Total Threads: 3 Thread 13 Socket[addr=/0:0:0:0:0:0:0:1,port=63366,localport=8888] Client: Sec-Fetch-Site: same-origin
Total Threads: 3 Thread 13 Socket[addr=/0:0:0:0:0:0:0:1,port=63366,localport=8888] Client: Sec-Fetch-Mode: no-cors
Total Threads: 3 Thread 13 Socket[addr=/0:0:0:0:0:0:0:1,port=63366,localport=8888] Client: Sec-Fetch-Dest: style
Total Threads: 3 Thread 13 Socket[addr=/0:0:0:0:0:0:0:1,port=63366,localport=8888] Client: Referer: http://localhost:8888/
Total Threads: 3 Thread 13 Socket[addr=/0:0:0:0:0:0:0:1,port=63366,localport=8888] Client: Accept-Language: en-US,en;q=0.9
Total Threads: 3 Thread 13 Socket[addr=/0:0:0:0:0:0:0:1,port=63366,localport=8888] Client: Accept-Encoding: gzip, deflate
```
2. Server will close a connection if it idles longer than the timeout allowed. HTTP code 408.
![408](https://drive.google.com/uc?export=view&id=19qLf0o2n4Nm-xK2QJTBHDzTwkqObE9rk)
3. Request is valid and server can fulfill. HTTP code 200.
![200](https://drive.google.com/uc?export=view&id=1WbvkiIZMjnpz2o-M9iOzPrrDekLfggQ0)
4. Request is invalid or server cannot find the requested file. HTTP code 404.
![404](https://drive.google.com/uc?export=view&id=1ZzwZXA9EyGJ-PvPvnqGu_yxGiz0Es89R)
5. If a file is present but the proper permissions are not set, a permission denied error is returned. HTTP code 403.
![403](https://drive.google.com/uc?export=view&id=1OO6i60T-fNdrjfyJffABxBHqKk1vLJF5)
6. Server only supports HTTP GET method. If other other methods are sent, server will return a "Method not allowed" error. HTTP code 405.
![405](https://drive.google.com/uc?export=view&id=1zr9feNnLNggN9RGWCPdcUCyb7zwMzWiE)
7. Server error. e.g. IOException. HTTP Code 500.
![500](https://drive.google.com/uc?export=view&id=1y3ORyeTRyiwI3uZdHFkhb53_j7kCZch9)
8. Bad request from client. HTTP Code 400.
![400](https://drive.google.com/uc?export=view&id=1MynTV1B3th5Bi6By-NAtWYcOcByRpIJy)

