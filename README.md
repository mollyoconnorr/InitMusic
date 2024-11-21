<a name="readme-top"></a>

# InitMusic
By '**Init to Win it**' - Molly O'Connor and Nick Clouse

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#main-features">Main Features</a></li>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a>
          <ul>
            <li><a href="#other-technologies-used">Other Technologies Used</a></li>
          </ul>
        </li>
        <li><a href="#installation">Installation</a>
          <ul>
            <li><a href="#setting-up-mysql">Setting up MySql</a></li>
          </ul>
        </li>
      </ul>
    </li>
    <li>
      <a href="#running-our-application">Running our application</a>
    </li>
    <li>
      <a href="using-our-application">Using our application</a>
    </li>
    <li>
      <a href="#testing">Testing</a>
    </li>
    <li><a href="#future-opportunities">Future Opportunities</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->
## About The Project

InitMusic is a music service that connects users to a vast library of thousands of songs, 
powered by the Deezer API. With an intuitive, simple interface, users can easily explore and discover new music across a 
wide variety of genres and artists. The platform allows users to create personalized playlists where they can save 
their favorite tracks, making it easy to enjoy their music collection at any time.

### Main features
* Persisted data, included user information, and created playlists
* Ability to search for thousands of songs using the [Deezer API](https://developers.deezer.com/api)
* Create,Rename,Remove playlists with all your favorite songs
* Security Questions for changing/resetting user data
* Song Queries cached for faster lookups in the future, updated frequently
    * If a query is searched and it's been more than 7 days since it was last retrieved from Deezer, it will be retrieved again to check for any updated/new songs

<p align="right">(<a href="#readme-top">back to top</a>)</p>


### Built With

[![Java][java-img]][java-url]
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)
![HTML](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![CSS](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)  

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)
![JUnit](https://img.shields.io/badge/JUnit-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![SLF4J](https://img.shields.io/badge/SLF4J-008080?style=for-the-badge&logo=java&logoColor=white)



<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Documentation
To see our documentation, go to the [**Documentation**](Documentation) folder in the main directory.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- GETTING STARTED -->
## Getting Started

To get a local copy up and running follow these steps.

### Prerequisites

* **IDE**: Any IDE of your choosing, although we recommend IntelliJ for optimal compatibility.
* **Java**: Java 21.0.4 JDK or higher.
* **MySql**: MySql 9.0.1 or higher. We used `Ver 9.0.1 for macos14 on x86_64 (MySQL Community Server - GPL)`  
**NOTE: Not tested with older versions of dependencies.**

#### Other technologies used:
* **Gradle**: 8.10
* **Spring Boot**: 3.3.3
* Navigate to our [**build.gradle**](build.gradle) file to see the other dependencies we used

### Installation
1. Clone the git repository:
     `git clone https://github.com/mollyoconnorr/InitMusic`
2. If you don't have Java 21.0.4 installed:  
     * [Oracle Download Guide](https://docs.oracle.com/en/java/javase/23/install/overview-jdk-installation.html)  
     * [Oracle Download Java](https://www.oracle.com/java/technologies/downloads/)
3. If you don't have MySql 9.0.1 installed:  
     * [Download Guide](https://dev.mysql.com/doc/mysql-installation-excerpt/5.7/en/)  
     * [Download MySql](https://dev.mysql.com/downloads/installer/)

#### Setting up MySql
Our database is called `initMusic` and is on **Port 3306**. You can access it by setting up a user with username `initDev` and password `password`. To change any of these fields, you can do so in our [application.properties](src/main/resources/application.properties) file.

Run the commands below to set up your MySql user **(Make sure MySql is started first)**:
```mysql
$ mysql -u root -p
mysql> CREATE DATABASE initMusic;
mysql> CREATE USER 'initDev'@'localhost' IDENTIFIED BY 'password';
mysql> CREATE USER 'initDev'@'%' IDENTIFIED BY 'password';
mysql> GRANT ALL PRIVILEGES ON initMusic.* TO 'initDev'@'localhost' WITH GRANT OPTION;
mysql> GRANT ALL PRIVILEGES ON initMusic.* TO 'initDev'@'%' WITH GRANT OPTION;
mysql> exit
```
Connecting to DB after creation:
```mysql
$ mysql -u initDev -p database
mysql> USE initMusic;
```

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- Running our application -->
## Running our application
0. Navigate to the command line
1. Make sure mysql is running:  
  * Starting will depend on how you installed MySql.
2. Navigate to the initMusic directory. Example:
```bash
$ cd initMusic
```
3. Start Gradle by running:
```bash
$ ./gradlew bootRun #MacOS and Linux
$ gradlew bootRun #Windows
```
The first time you run this command, it might show a lot of messages and take a while to start. If it ends in something like:  
```bash
YYYY-MM-DD HH:MM:SS - [INFO] - from edu.carroll.initMusic.InitMusicApplication in restartedMain 
Started InitMusicApplication in 7.696 seconds (process running for 8.301)

<==========---> 80% EXECUTING [10s]
```
The application is now running! It will run until you stop it.  
Stop it by pushing `control + c`  

If the application fails to run and gives something like 
```bash
BUILD SUCCESSFUL in 10s 2 actionable tasks: 2 executed, 2 up-to-date
```
Make sure MySql is started. 
If the application fails to run and gives something like
```bash
BUILD FAILED in 10s
```
This usually means something went wrong internally. Check if any error messages are shown in the console. 

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Using our Application
Once running, our app runs on **Port 8080**, and you can go to `http://localhost:8080/` in a browser of your choice to use it.  

While running, log messages will be shown in the console throughout usage of the app, and can help with debugging if needed. A `logs' folder should be created in your directory, and you can see all archived logs by looking in that folder.

## Testing

We tested our app using JUnit [(User Guide to JUnit)](https://junit.org/junit5/docs/current/user-guide/). To run our tests, you can run the commands below in the console:  
```bash
$ ./gradlew test #MacOS and Linux
$ gradlew test #Windows
```
**Only run tests for a specific package**
```bash
$ ./gradlew test --tests "edu.carroll.initMusic.YourPackage.*" #MacOS and Linux
$ gradlew test --tests "edu.carroll.initMusic.YourPackage.*" #Windows
```
**Only run tests for a specific class**
```bash
$ ./gradlew test --tests "edu.carroll.initMusic.YourPackage.YourClass" #MacOS and Linux
$ gradlew test --tests "edu.carroll.initMusic.YourPackage.YourClass" #Windows
```

<!-- Future Opportunities -->
## Future Opportunities

- [ ] Improve search functionality, find a better API to use
- [ ] Add more features, like saving artist or albums to your profile
- [ ] Ability to see other users' playlists
- [ ] Explore page
- [ ] **Got an idea? Learn how to contribute below!**

<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- CONTRIBUTING -->
## Contributing
If you have a suggestion that would make our application better, please fork the repo and create a pull request.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request


<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTACT -->
## Contact

* **Molly O'Connor** - moconnor@carroll.edu
* **Nick Clouse** - nclouse@carroll.edu

Project Link: [https://github.com/mollyoconnorr/InitMusic](https://github.com/mollyoconnorr/InitMusic)

<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- MARKDOWN LINKS & IMAGES -->
[java-url]:https://www.java.com/en/
[java-img]:https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white
<!-- ![initMusic](https://img.shields.io/badge/initMusic-purple?style=for-the-badge&logoSize=auto) -->
