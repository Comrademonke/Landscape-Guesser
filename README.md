# Landscape Guesser

A New Zealand based geography game featuring 10 levels (more levels and locations will be added in the future), each with a unique location. The goal is to guess the correct location based on geographical hints. The closer your guess the higher your score!

**How to play the game:**

- Mouse: Use for panning and zooming on the map and panorama viewer.

![panningTutorial](https://github.com/user-attachments/assets/087cf902-c0f1-4f82-ad25-a1a722fa7dd6)


- Keyboard - Press **q** to toggle the scoreboard (only available in levels and guessing stages).
  
![scoreboardTutorial](https://github.com/user-attachments/assets/34b47190-6756-4a9a-93e9-369eae72ea69)

For more instructions visit the tutorial located in settings.

**Scoring system**

Each level is score based on your distance from the marker, the further you are the less points you earn. The maximum score **per level** is **5,000**, and the maximum total score across **all levels** is **50,000**.

Graph representation of the scoring system:
<img width="1650" height="428" alt="PointsVsDistanceGraph" src="https://github.com/user-attachments/assets/31414bf9-aa25-43c2-adae-186c035ee607" />


# Requirements:

- Java installed
- 4GB ram 
- Stable internet connection for map rendering

- Geoapify key (free)
https://www.geoapify.com/

# How to run the game

<img width="149" height="46" alt="image" src="https://github.com/user-attachments/assets/70274aa9-e4d5-40a3-804c-565f2b2493aa" />

Paste in your geoapify key into the geoapify.config replacing API_KEY_HERE

THEN

Unix/MacOsX:  
`./mvnw clean javafx:run`

Windows:  
`.\mvnw.cmd clean javafx:run`

OR

Click the 'run game' button at the bottom of your status bar

![alt text](image.png)


