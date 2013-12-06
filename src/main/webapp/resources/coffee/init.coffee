# Require looks here to kick off the application
define [
  "app/controller/tournamentController"
  "app/controller/leaderBoardController"
], (tournamentController,leaderBoardController) ->
  tournamentController.init()
  leaderBoardController.init()
