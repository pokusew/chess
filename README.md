# Chess

A term project in [CTU FEL B0B36PJV course](https://cw.fel.cvut.cz/wiki/courses/b0b36pjv/start).


## Development ⚙️

**Requirements:**
* **Apache Maven 3.6+**
* **Java 11+** (tested with Java 15, note: Java 8 will NOT work)
* Make sure `JAVA_HOME` is properly set to a JDK 15 installation directory. 

The project can be **run** using Maven CLI as follows:
```bash
mvn clean && mvn compile && mvn javafx:run
```

The project can be also **imported into Intellij IDEA** (_Open or Import_ or _File > New > Project from existing sources ..._ then select Maven ...).

The **executable fat JAR can be built** using:
```bash
./build.sh
```
The executable JAR with all dependencies output is `target/chess-1.0-SNAPSHOT-jar-with-dependencies.jar`.

It can be run as follows:
```bash
java -jar target/chess-1.0-SNAPSHOT-jar-with-dependencies.jar
```


## Goal 🎯

**Main goal:** Create a **fully functional and playable**
implementation of the [**Chess** board game](https://en.wikipedia.org/wiki/Chess) in **Java**.


### Features ✨

* complete and correct implementation of all Chess rules
    (see Wikipedia's [Rules of chess](https://en.wikipedia.org/wiki/Rules_of_chess)
    or/and [FIDE Laws of Chess taking effect from 1 January 2018](https://handbook.fide.com/chapter/E012018))
* platform independent desktop application (support for macOS, Linux, Windows)
* GUI
* game modes
	* offline, local, two-player game 
		* human vs human
		* preparation for human vs computer
		    * idea: implement [UCI (Universal Chess Interface)](https://www.shredderchess.com/chess-features/uci-universal-chess-interface.html)
	* online, network, two-player game
* saving and loading of the game state
* possibility of manually placing the chess pieces before starting the game (game editor)
* chess clock


## Development plan 🗓️

The current development state, all the past, present and future tasks and issues including their time estimates are tracked using GitLab [Issues](https://gitlab.fel.cvut.cz/B182_B0B36PJV/endlemar/issues) and [Milestones](https://gitlab.fel.cvut.cz/B182_B0B36PJV/endlemar/milestones).

👀 See **[project board](https://gitlab.fel.cvut.cz/B182_B0B36PJV/endlemar/boards)** to get an up-to-date overview.

_**Disclaimer:** The issues, milestones, time estimates and docs might be sometimes outdated. However, I'll do my best to keep them up-to-date. 😄_


## Team members

* Martin Endler  
	https://gitlab.fel.cvut.cz/endlemar


## Further information

* [chess project description](https://cw.fel.cvut.cz/wiki/courses/b0b36pjv/semestral/sachy) on the course's website
* see [term project general requirements](https://cw.fel.cvut.cz/wiki/courses/b0b36pjv/semestral/start) on the course's website
