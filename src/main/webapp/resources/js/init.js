(function() {

  define(["app/controller/tournamentController", "app/controller/leaderBoardController"], function(tournamentController, leaderBoardController) {
    tournamentController.init();
    return leaderBoardController.init();
  });

}).call(this);
