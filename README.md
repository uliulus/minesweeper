# minesweeper-API

## Some assumptions, definitions, and disclaimer
 
I will use POST verb for resource creation.
I will use JSON + HAL when I feel it is necessary. (http://stateless.co/hal_specification.html)
I assume that collections of objects are small and wont use pagination
Definition is not complete due to time restriction. But main APIs are described enough. 
If I had more time, I would use swagger to document APIs.

## Games resource
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
* 0 < mines <= x_dimension * y_dimension (mines fit in the board)
* 0 < x_dimension <= x_max
* 0 < y_dimension <= y_max
* 0 < seconds <= max_seconds

In case the API responds with 201. It returns a header Location with the URL of the newly created resource, ie: /games/32193453. The response payload is:
{
	x_dimension: Int,
	y_dimension: Int, 
	mines: Int,
	second: Int
}

The response payload has the parameters that were effectively used to create the game, not necessarily the ones requested by the user.

## Cells resources
A game has cells, they have coordinates.
There are revealed and unrevealed cells. I will treat them as different resources because they have different state and different things can be done with them.

## Unrevealed cells
When a game is created, all cells are unrevealed. Thus, there is no method for creating unrevealed cells.
An unrevealed cell might have a flag. I model this with a flag attribute that has any of these values: {"red", "question-mark", "none"}

An unrevealed-cell example:
{ x: 8, 
  y: 3, 
  flag: "red"
  \_links: { 
    self: {href: "/games/34534534/unrevealed-cells/sdfs"}
   }
}

GET /games/34534534/unrevealed-cells
Gets a collection of unrevealed-cell resource. This API is probably only used when resuming an old game.

Response status code: 404 if the game does not exist, 
Response status code: 200 if the game exists, and response payload is of the form
{
  items: [<unrevelead-cell-object1>, <unrevelead-cell-object1>]
}
	
The client can update a flag attribute.

PUT /games/34534534/unrevealed-cells/sdfs
{ flag: "question-mark" }


## Revealed cells

Revealed cells dont have flags. 
A revealed-cell has information on how many neighbouring mines there are. Also, it tells if it has a mine or not.

A revealed-cell example:
{ x: 8, 
  y: 3, 
  neighbouring-mines: 2,
  has_mine: true,
  \_links: { 
    self: {href: "/games/34534534/revealed-cells/sdfs"}
   }
}

Nothing can be done on revealed cells. 
The only methods supported are GET for collections and for an item
ie:
GET /games/34534534/revealed-cells
GET /games/34534534/revealed-cells/sdfs


## Revealing a cell
In order to reveal a cell, the client has to delete an unrevealed-cell. By defining it this way, I don-t need to send and parse payload for reveling cells.

When revealing a cell, many other cells might be revealed as well. the response has the collection



We could make the cell resource name map somehow the coordinates, ie: /games/312312/cells/08_03. 
I know this is not RESTful, since I am fixing resource names. But I am not that RESTafarian (https://www.ben-morris.com/the-restafarian-flame-wars-common-points-of-disagreement-over-rest-api-design/)
By adopting this naming I will avoid to implement some APIs to list cells (of course with some controlled coupling at the client).

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
