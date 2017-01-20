# 3D Tic Tac Toe for Sponge

A three dimensional Tic Tac Toe game, playable in the Minecraft chat.

It works like the default Tic Tac Toe game, only in a cube. Also you have to get a chain of 4 crosses/circles in a row to win as the cube is 4x4x4 big.


##Rules
###Game field
The game field is a 4x4x4 big cube, sliced in 4 different 4x4 fields big layers which should be handled as if they were stacked on top of each other. The upper layer is shown in the upper left corner, the lower layer int the lower right corner during a match.

###How to win
Succeed to place 4 crosses/circles in a row to win. This can be done <b>horizontally</b>, <b>vertically</b> and <b>diagonal</b>. Place your crosses/circles in <b>one layer</b> as usual or place them in <b>different layers</b>, which is more tricky but absolutely doable. All variants are allowed as long as they are placed in a row of 4 equal symbols.

##Permissions
* <code>ttt3d.invite</code>: Allows a player to invite another player to a match
* <code>ttt3d.accept</code>: Allows a player to accept an invitation

##API
###Events
There are 4 different Events that a plugin can listen to:
* <code>MatchInviteEvent</code>: Dispatched when a player invites another player to a match. This event <b>can</b> be cancelled.
* <code>MatchStartedEvent</code>: Dispatched when a match begins, after accepting an invitation. This event <b>cannot</b> be cancelled.
* <code>MatchFinishedEvent</code>: Dispathed when a match has been finished, either if a player has won normally or at least one player has left the game. This event <b>cannot</b> be cancelled.
* <code>MatchAbortedEvent</code>: Dispatched when a match has been aborted implicitely by a player. This event <b>cannot</b> be cancelled.

##FAQ
###Why 4x4x4 instead of 3x3x3?
When playing in 3x3x3 mode, a player could place it's symbol in the absolute middle of the cube. After this, the player has an unfair advantage to his opponent. In 4x4x4 mode, there is no absolute middle so a match would take a longer time and every player would have an equal chance.

###Why would one develop a 3D Tic Tac Toe game for the Minecraft chat?
Excellent question, in this case it's the result of being creative in a sleepless night.
