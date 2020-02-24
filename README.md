# minesweeper-API

## Resources and rationale

I will use POST verb for resource creation

## Games resource:
A game is created with board dimensions, duration and quantity of mines selected by the user. Alternatively these parameters could be optional, and some predefined default values are used if not provided by the user.

POST /games: creates a new game.
Json Body:
{
	x_dimension: Int,
	y_dimension: Int, 
	mines: Int,
	seconds: Int
}

responses: 201 (created) or 400 (bad request). 
The API responds with 400 if any of the following conditions is NOT met:
1. 0 < mines <= x_dimension * y_dimension (mines fit in the board)
2. 0 < x_dimension <= x_max
3. 0 < y_dimension <= y_max
4. 0 < seconds <= max_seconds

In case the API responds with 201. It returns a header Location with the URL of the newly created resource, ie: /games/32193453. The response payload is:
{
	x_dimension: Int,
	y_dimension: Int, 
	mines: Int,
	second: Int
}

The response payload has the parameters that were effectively used to create the game, not necessarily the ones requested by the user.

## Cells resource:
A game has cells. Cells are not created using cells resource. They are created jointly with a game. Then, there is no method POST for cells resources.

Cells have coordinates. They can have mines, thou asking for cells with mines for un unfinished game is not allowed.

We could make the cell resource name map somehow the coordinates, ie: /games/312312/cells/08_03. 

I know this is not RESTful, since I am fixing resource names. But I am not that RESTafarian (https://www.ben-morris.com/the-restafarian-flame-wars-common-points-of-disagreement-over-rest-api-design/)

By adopting this naming I will avoid to implement APIs to list cells (of course with some controlled coupling at the client).

Cells have some state hidden to the user in unfinished games: {has_mine: Bool}
Also they have some other state known by the user. One of the following {revealed, red-flagged, question-mark-flagged, exploded, none}

The user cannot ask for a cell state. It can only act on a cell by trying to modify its state.

Request: 
PUT /games/342423/cells/08_03
{ state: "revealed"}

Response code 404 if the cell or game does not exist
Response code 400 if state in request is not in {revealed, red-flagged, question-mark-flagged, none}.
	So, you cannot ask for a cell to transition to exploded.
Response code 400 if you try to act on a revealed cell

Response code 200 otherwise, but the resource might have not changed to the intended state, ie: the request is for revealing, but the response tells the cell has exploded.
Response payload:
{ x:8, y:3, state:"exploded"}

## Game status
A game has any of the following status: "in-progress", "timeout", "cleared", "exploded"
Also, a game has a start-time timestamp.

status and start-time can  be modelling as extra attributes in the game resource.

Cells state can only be modified if the game status is "in-progress"

After the game has finished, we'll need a way to tell the client where mines are. This could be done accessing to a mined_cells: resource collection.

GET /games/3434534/mined_cells

Response code: 400 if the game is "in-progress"
Response code: 200 if the game is finished. In this case, the response payload is a collection of URLs of cells that have a mine. If this collection were to big, we would need pagination. But, lets assume this response is small and simply have an array as result.
I would put reasonable limits to the 

## Users
We could put user authentication info in request headers, so the described APIs wont change much.
A game is created for an auntheticated user. We could opt to allow a user to see games of other users, but changing a cell is only allowed to the game owner. If a user tries to change another user cell, it gets a response with status code 403.
