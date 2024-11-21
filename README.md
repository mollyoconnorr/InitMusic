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
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li>
      <a href="#Using-our-website">Using our website</a>
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

<p align="right">(<a href="#readme-top">back to top</a>)</p>



### Built With

[![Java][java-img]][java-url]

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
* **SLF4J**: Logging throughout our code
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
```

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- Using our website -->
## Using our website
<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- Future Opportunities -->
## Future Opportunities

- [ ] TBD

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
