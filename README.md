# Chess

A [**Chess** board game](https://en.wikipedia.org/wiki/Chess) implemented as a Java desktop application  
A term project in [CTU FEE](https://fel.cvut.cz/en/) ([ČVUT FEL](https://fel.cvut.cz/cz/)) [B0B36PJV course](https://cw.fel.cvut.cz/wiki/courses/b0b36pjv/start).

<div style="text-align: center">
<img alt="A chessboard with a piece that is being moved" title="A screenshot from the running app" src="./docs/images/chess-game.png" width="800" />
</div>


## Content

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->

- [Documentation](#documentation)
- [Goal 🎯](#goal-)
  - [Features ✨](#features-)
- [Team members](#team-members)
- [Git repository](#git-repository)
- [Further information](#further-information)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->


## Documentation

* [Technical Manual ⚙️](./docs/TECHNICAL-MANUAL.md)
    * 👉 See its **[Development section](./docs/TECHNICAL-MANUAL.md#development)** that includes notes
        about **building and running the project**.
* [User Manual 📘](./docs/USER-MANUAL.md)
* [Development plan incl. current state 🗓️](./TODO.md)


## Goal 🎯

**Main goal:** Create a **fully functional and playable**
implementation of the [**Chess** board game](https://en.wikipedia.org/wiki/Chess) in **Java**.


### Features ✨

* complete and correct implementation of all Chess rules
    (see Wikipedia's [Rules of chess](https://en.wikipedia.org/wiki/Rules_of_chess)
    or/and [FIDE Laws of Chess taking effect from 1 January 2018](https://handbook.fide.com/chapter/E012018))
    * except fifty-move rule, insufficient material rule,  threefold repetition rule
* platform independent desktop application (support for macOS, Linux, Windows)
* GUI
* game modes
	* offline, local, two-player game 
		* human vs human
		* preparation for human vs computer
		    * idea: implement [UCI (Universal Chess Interface)](https://www.shredderchess.com/chess-features/uci-universal-chess-interface.html)
* saving and loading of the game state
    * including the support for [PGN](https://en.wikipedia.org/wiki/Portable_Game_Notation) chess standard format
* possibility of manually placing the chess pieces before starting the game (game editor)
* chess clock


## Team members

* **Martin Endler**  
  endlemar@fel.cvut.cz  
  [github.com/pokusew](https://github.com/pokusew)


## Git repository

The Git repository for this project is hosted at:
1. **GitHub** [pokusew/chess](https://github.com/pokusew/chess) (my GitHub repository)
2. **CTU FEE (CVUT FEL) GitLab** [B202_B0B36PJV/endlemar](https://gitlab.fel.cvut.cz/B202_B0B36PJV/endlemar) (as required by the assigment description)


## Further information

* [Chess Game Notes](./CHESS-GAME-NOTES.md)
* [Useful Java/JavaFX development info](./USEFUL-INFO.md)
* [chess project description](https://cw.fel.cvut.cz/wiki/courses/b0b36pjv/semestral/sachy) on the course's website
* see [term project general requirements](https://cw.fel.cvut.cz/wiki/courses/b0b36pjv/semestral/start) on the course's website
