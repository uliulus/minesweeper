# minesweeper-API project

Please check API-SPEC for api documentation


## Implementation

I will use Scala language and akka.http library

In order to start faster, I start copying files from a small tutorial (https://github.com/Codemunity/akkahttp-quickstart).

This tutorial implements an API for handling a TODO list. But the API is not restful, and the repo is in memory.


I will change the code everywhere. I plan to:
* implement games resource, repo still in memory
* reorganize code to separate route handling from handling request and responses (controller)
* add more resources
* implement repositories using slick. DB will be mysql or postgres
* while doing previous points, add unit tests.

